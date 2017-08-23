package se.apegroup.pizzaapp.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import se.apegroup.pizza.client.domain.Restaurant;

public class RestaurantWrapper {

    public final Restaurant restaurant;
    public final Integer distance;

    public RestaurantWrapper(Restaurant restaurant, Integer distance) {
        this.restaurant = restaurant;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
