package se.apegroup.pizzaapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.apegroup.pizza.client.domain.Cart;

import java.time.LocalDateTime;
import java.util.List;

public class OrderStatus {

    public final Long orderId;
    public final Integer totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public final LocalDateTime orderedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public final LocalDateTime esitmatedDelivery;
    public final String status;
    public final List<Cart> cart;

    @JsonCreator
    public OrderStatus(@JsonProperty("orderId") Long orderId, @JsonProperty("totalPrice") Integer totalPrice,
                       @JsonProperty("orderedAt") LocalDateTime orderedAt, @JsonProperty("esitmatedDelivery") LocalDateTime esitmatedDelivery,
                       @JsonProperty("status") String status, @JsonProperty("cart") List<Cart> cart) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderedAt = orderedAt;
        this.esitmatedDelivery = esitmatedDelivery;
        this.status = status;
        this.cart = cart;
    }
}
