package se.apegroup.pizzaapp.infrastructure.jdbc.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_details")
@SequenceGenerator(name = "order_details_seq", sequenceName = "order_details_seq")
public class OrderDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @Column(name = "menu_item_id", nullable = false)
    private Integer menuItemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderDetails() {
    }

    public OrderDetails(Integer menuItemId, Integer quantity) {
        this.order = order;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }
}
