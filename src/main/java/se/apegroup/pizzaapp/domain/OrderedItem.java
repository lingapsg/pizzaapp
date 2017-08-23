package se.apegroup.pizzaapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.apegroup.pizzaapp.exception.ApiException;

public class OrderedItem {

    public final int menuItemId;
    public final int quantity;
    public final int restaurantId;
    public final int unitPrice;

    @JsonCreator
    public OrderedItem(@JsonProperty("menuItemId") Integer menuItemId,
                       @JsonProperty("quantity") Integer quantity,
                       @JsonProperty("restaurantId") Integer restaurantId,
                       @JsonProperty("unitPrice") Integer unitPrice) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.restaurantId = restaurantId;
        this.unitPrice = unitPrice;
        validateItem();
    }

    private void validateItem() {
        if (menuItemId <= 0 || quantity <= 0 || restaurantId <= 0 || unitPrice <= 0) {
            throw new ApiException(400, "Invalid Input");
        }
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
