package se.apegroup.pizzaapp.infrastructure.jdbc.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer_order")
@SequenceGenerator(name = "order_seq", sequenceName = "order_seq")
public class CustomerOrder implements Serializable {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_seq")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ordered_at", nullable = false)
    private Date orderedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "estimated_delivery", nullable = false)
    private Date esitmatedDelivery;

    @Column(name = "status", nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetails> orderDetailsList;

    public CustomerOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Date orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Date getEsitmatedDelivery() {
        return esitmatedDelivery;
    }

    public void setEsitmatedDelivery(Date esitmatedDelivery) {
        this.esitmatedDelivery = esitmatedDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
