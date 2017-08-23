package se.apegroup.pizzaapp.infrastructure.rest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import se.apegroup.pizzaapp.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import retrofit2.Response;
import se.apegroup.pizza.client.PizzaRestClient;
import se.apegroup.pizza.client.domain.Restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantRepository {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantRepository.class);

    private final PizzaRestClient pizzaRestClient;

    @Autowired
    public RestaurantRepository(PizzaRestClient pizzaRestClient) {
        this.pizzaRestClient = pizzaRestClient;
    }

    public List<Restaurant> getRestaurants() throws ApiException {
        try {
            Response<List<Restaurant>> restaurantsResponse = pizzaRestClient.getRestaurants().execute();

            return Optional.of(restaurantsResponse)
                    .filter(listResponse -> restaurantsResponse.isSuccessful())
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            LOGGER.error("Error occurred :",e);
            throw new ApiException(502, "Unable to find restaurants");
        }
    }

    public Restaurant getRestaurant(Integer restaurantId) throws ApiException {
        try {
            Response<Restaurant> restaurantResponse = pizzaRestClient.getRestaurant(restaurantId).execute();

            return Optional.of(restaurantResponse)
                    .filter(listResponse -> restaurantResponse.isSuccessful())
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            throw new ApiException(502, "Unable to find restaurants");
        }
    }
}
