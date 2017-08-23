package se.apegroup.pizza.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import se.apegroup.pizza.client.domain.MenuItem;
import se.apegroup.pizza.client.domain.Restaurant;

import java.util.List;
import java.util.Map;

public interface PizzaRestClient {

    @GET("restaurants/")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}")
    Call<Restaurant> getRestaurant(@Path("id") Integer id);

    @GET("restaurants/{id}/menu")
    Call<List<MenuItem>> GetRestaurantMenu(@Path("id") Integer id, @QueryMap Map<String, String> queryParams);
}
