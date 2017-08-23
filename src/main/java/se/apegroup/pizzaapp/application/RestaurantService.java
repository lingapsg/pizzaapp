package se.apegroup.pizzaapp.application;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.apegroup.google.client.domain.distancematrix.DistanceResponse;
import se.apegroup.google.client.domain.geocode.GeocodeResponse;
import se.apegroup.pizza.client.domain.Restaurant;
import se.apegroup.pizzaapp.domain.RestaurantWrapper;
import se.apegroup.pizzaapp.exception.ApiException;
import se.apegroup.pizzaapp.infrastructure.rest.GoogleDistanceRepository;
import se.apegroup.pizzaapp.infrastructure.rest.GoogleGeocodeRepository;
import se.apegroup.pizzaapp.infrastructure.rest.RestaurantRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RestaurantService {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantService.class);
    private static final String SUCCESS = "OK";
    private final RestaurantRepository restaurantRepository;
    private final GoogleGeocodeRepository googleGeocodeRepository;
    private final GoogleDistanceRepository googleDistanceRepository;
    private final Integer restaurantRadius;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             GoogleGeocodeRepository googleGeocodeRepository,
                             GoogleDistanceRepository googleDistanceRepository,
                             @Value("${restaurant.radius}") Integer restaurantRadius) {
        this.restaurantRepository = restaurantRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        this.googleDistanceRepository = googleDistanceRepository;
        this.restaurantRadius = restaurantRadius;
    }

    /**
     * @param {@String address}
     * @param radius
     * @return
     * @throws Exception
     */
    public Restaurant findClosest(String address, final Integer radius) {
        Optional<GeocodeResponse> userAddress = Optional.ofNullable(googleGeocodeRepository.getLocationByAddress(address));
        userAddress
                .filter(geocodeResponse -> geocodeResponse.status.equalsIgnoreCase(SUCCESS))
                .orElseThrow(() -> new ApiException(404, "Unable to find user address"));

        return getRestaurants()
                .parallelStream()
                .map(restaurant -> getRestaurantDistance(restaurant, userAddress.get()))
                .filter(restaurantWrapper -> Objects.nonNull(radius) ? restaurantWrapper.distance <= radius: restaurantWrapper.distance <= restaurantRadius)
                .sorted(Comparator.comparing(o -> o.distance))
                .map(restaurantWrapper -> restaurantWrapper.restaurant)
                .findFirst().orElseThrow(() -> new ApiException(404, "No restaurant Found"));
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.getRestaurants();
    }

    private RestaurantWrapper getRestaurantDistance(Restaurant restaurant, final GeocodeResponse userAddress) {
        try {
            GeocodeResponse geocodeResponse = googleGeocodeRepository.getLocationByAddress(
                    restaurant.getFullAddress());

            if (geocodeResponse.status.equalsIgnoreCase(SUCCESS)) {
                return new RestaurantWrapper(restaurant,
                        findRestaurantDistance(userAddress.results.get(0).placeId,
                                geocodeResponse.results.get(0).placeId
                        ));
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred :", e);
        }
        return new RestaurantWrapper(restaurant, 0);
    }

    private Integer findRestaurantDistance(String originPlaceId, String destinationPlaceId) throws Exception {
        originPlaceId = "place_id:".concat(originPlaceId);
        destinationPlaceId = "place_id:".concat(destinationPlaceId);
        DistanceResponse distanceResponse = googleDistanceRepository.getDistance(originPlaceId, destinationPlaceId);

        return Optional.of(distanceResponse.status)
                .filter(status -> status.equals(SUCCESS))
                .map(status -> distanceResponse.rows.get(0).elements.get(0).distance.value)
                .orElse(0);
    }
}
