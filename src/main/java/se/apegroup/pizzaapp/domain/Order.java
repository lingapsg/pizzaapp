package se.apegroup.pizzaapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import se.apegroup.pizza.client.domain.Cart;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class Order {

    @NotEmpty
    @Size(min = 1)
    public final List<Cart> cart;

    @Min(1)
    @NotNull
    public final Integer restuarantId;

    @NotNull
    public final String deliveryAddress;

    @JsonCreator
    public Order(@JsonProperty("cart") List<Cart> cart,
                 @JsonProperty("restuarantId") Integer restuarantId,
                 @JsonProperty("deliveryAddress") String deliveryAddress) {
        this.cart = cart;
        this.restuarantId = restuarantId;
        this.deliveryAddress = deliveryAddress;
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