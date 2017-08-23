package se.apegroup.pizzaapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class OrderedItemWrapper {

    public final List<OrderedItem> orderedItems;
    public final Integer totalPrice;

    @JsonCreator
    public OrderedItemWrapper(@JsonProperty("orderedItems") List<OrderedItem> orderedItems,
                              @JsonProperty("totalPrice") Integer totalPrice) {
        this.orderedItems = orderedItems;
        this.totalPrice = totalPrice;
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
