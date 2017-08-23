package se.apegroup.pizzaapp.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OrderResponse {

    public final Long orderId;
    public final Integer totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public final LocalDateTime orderedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public final LocalDateTime esitmatedDelivery;
    public final String status;

    @JsonCreator
    public OrderResponse(@JsonProperty("orderId") Long orderId, @JsonProperty("totalPrice") Integer totalPrice,
                         @JsonProperty("orderedAt") LocalDateTime orderedAt, @JsonProperty("esitmatedDelivery") LocalDateTime esitmatedDelivery,
                         @JsonProperty("status") String status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderedAt = orderedAt;
        this.esitmatedDelivery = esitmatedDelivery;
        this.status = status;
    }
}
