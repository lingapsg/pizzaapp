package se.apegroup.pizza.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class Cart {

    @Min(1)
    public final Integer menuItemId;

    @Min(1)
    public final Integer quantity;

    @JsonCreator
    public Cart(@JsonProperty("menuItemId") Integer menuItemId,
                @JsonProperty("quantity") Integer quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }
}