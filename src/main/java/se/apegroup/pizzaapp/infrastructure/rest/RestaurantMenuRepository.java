package se.apegroup.pizzaapp.infrastructure.rest;

import se.apegroup.pizzaapp.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import retrofit2.Response;
import se.apegroup.pizza.client.PizzaRestClient;
import se.apegroup.pizza.client.domain.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RestaurantMenuRepository {

    private final PizzaRestClient pizzaRestClient;

    @Autowired
    public RestaurantMenuRepository(PizzaRestClient pizzaRestClient) {
        this.pizzaRestClient = pizzaRestClient;
    }

    public List<MenuItem> GetRestaurantMenu(Integer restaurantId, Map<String, String> menuParams) throws ApiException {
        try {
            Response<List<MenuItem>> menuResponse =
                    pizzaRestClient.GetRestaurantMenu(restaurantId, menuParams).execute();
            return Optional.of(menuResponse)
                    .filter(Response::isSuccessful)
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            throw new ApiException(502, "Unable to get menu");
        }
    }
}
