package se.apegroup.pizzaapp.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.apegroup.google.client.domain.distancematrix.DistanceResponse;
import se.apegroup.google.client.domain.geocode.GeocodeResponse;
import se.apegroup.pizza.client.domain.Cart;
import se.apegroup.pizza.client.domain.Restaurant;
import se.apegroup.pizzaapp.domain.Order;
import se.apegroup.pizzaapp.domain.OrderResponse;
import se.apegroup.pizzaapp.domain.OrderStatus;
import se.apegroup.pizzaapp.domain.OrderStatusType;
import se.apegroup.pizzaapp.infrastructure.jdbc.entity.CustomerOrder;
import se.apegroup.pizzaapp.infrastructure.jdbc.entity.OrderDetails;
import se.apegroup.pizzaapp.infrastructure.jdbc.repository.CustomerOrderRepository;
import se.apegroup.pizzaapp.infrastructure.jdbc.repository.OrderDetailsRepository;
import se.apegroup.pizzaapp.infrastructure.rest.GoogleDistanceRepository;
import se.apegroup.pizzaapp.infrastructure.rest.GoogleGeocodeRepository;
import se.apegroup.pizzaapp.infrastructure.rest.RestaurantRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static se.apegroup.pizzaapp.util.AppUtil.getDateFromLocalDateTime;
import static se.apegroup.pizzaapp.util.AppUtil.getLocalDateTimeFromDate;

@Service
public class OrderProcessor {

    private final GoogleGeocodeRepository googleGeocodeRepository;
    private final GoogleDistanceRepository googleDistanceRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public OrderProcessor(GoogleGeocodeRepository googleGeocodeRepository,
                          GoogleDistanceRepository googleDistanceRepository,
                          CustomerOrderRepository customerOrderRepository,
                          RestaurantRepository restaurantRepository,
                          OrderDetailsRepository orderDetailsRepository) {
        this.googleGeocodeRepository = googleGeocodeRepository;
        this.googleDistanceRepository = googleDistanceRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Transactional
    public OrderResponse placeOrder(String username, Order order, Integer totalPrice) {
        Restaurant restaurant = restaurantRepository.getRestaurant(order.restuarantId);

        DistanceResponse distanceResponse = getDistance(order.deliveryAddress, restaurant.getFullAddress());

        LocalDateTime orderedAt = LocalDateTime.now();
        LocalDateTime estimatedDelivery = getEstimatedDeliveryTime(orderedAt,distanceResponse.rows.get(0).elements.get(0).duration.value);
        final List<OrderDetails> orderDetailsList = new ArrayList<>();

        final CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setUsername(username);
        customerOrder.setRestaurantId(order.restuarantId);
        customerOrder.setTotalPrice(totalPrice);
        customerOrder.setOrderedAt(getDateFromLocalDateTime(orderedAt));
        customerOrder.setEsitmatedDelivery(getDateFromLocalDateTime(estimatedDelivery));
        customerOrder.setStatus(OrderStatusType.ORDERED.getStatus());
        customerOrder.setCreatedAt(new Date());

        customerOrderRepository.save(customerOrder);
        order.cart.forEach(cart -> {
            OrderDetails orderDetails = new OrderDetails(cart.menuItemId, cart.quantity);
            orderDetails.setOrder(customerOrder);
            orderDetailsList.add(orderDetails);
        });
        orderDetailsRepository.save(orderDetailsList);
        return new OrderResponse(customerOrder.getId(), totalPrice, orderedAt, estimatedDelivery, OrderStatusType.ORDERED.getStatus());
    }

    /**
     * @param orderId
     * @return
     */
    public OrderStatus getOrderStatus(Long orderId) {
        CustomerOrder order = customerOrderRepository.findOne(orderId);
        List<Cart> carts = new ArrayList<>();
        order.getOrderDetailsList().forEach(orderDetails -> carts.add(new Cart(orderDetails.getMenuItemId(), orderDetails.getQuantity())));

        LocalDateTime orderedAt = getLocalDateTimeFromDate(order.getOrderedAt());
        LocalDateTime estimatedDelivery = getLocalDateTimeFromDate(order.getEsitmatedDelivery());
        OrderStatusType orderStatusType;

        if (LocalDateTime.now().isAfter(estimatedDelivery)) {
            orderStatusType = OrderStatusType.DELIVERED;
        } else {
            orderStatusType = OrderStatusType.BAKING;
        }
        return new OrderStatus(orderId, order.getTotalPrice(),
                orderedAt,
                estimatedDelivery,
                orderStatusType.getStatus(), carts);
    }

    private DistanceResponse getDistance(String deliveryAddress, String restaurantAddress) {
        GeocodeResponse userLocation = googleGeocodeRepository.getLocationByAddress(deliveryAddress);
        GeocodeResponse restaurantLocation = googleGeocodeRepository.getLocationByAddress(restaurantAddress);
        String originPlaceId = "place_id:".concat(restaurantLocation.results.get(0).placeId);
        String destinationPlaceId = "place_id:".concat(userLocation.results.get(0).placeId);
        return googleDistanceRepository.getDistance(originPlaceId, destinationPlaceId);
    }

    private LocalDateTime getEstimatedDeliveryTime(LocalDateTime orderedAt, Integer timeToTravel) {
        return orderedAt.plusMinutes(20).plusSeconds(timeToTravel);
    }
}
