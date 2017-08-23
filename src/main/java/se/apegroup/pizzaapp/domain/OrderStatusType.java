package se.apegroup.pizzaapp.domain;

public enum OrderStatusType {

    ORDERED("Ordered"), BAKING("Baking"), DELIVERED("Delivered");

    private final String status;

    OrderStatusType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
