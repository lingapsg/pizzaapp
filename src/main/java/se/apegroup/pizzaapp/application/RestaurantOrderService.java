package se.apegroup.pizzaapp.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.apegroup.pizzaapp.domain.*;
import se.apegroup.pizzaapp.exception.ApiException;
import se.apegroup.pizzaapp.infrastructure.jdbc.entity.CustomerOrder;
import se.apegroup.pizzaapp.infrastructure.jdbc.repository.CustomerOrderRepository;
import se.apegroup.pizzaapp.processor.OrderProcessor;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantOrderService {

    private Map<String, List<OrderedItem>> carts = new LinkedHashMap<>();
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderProcessor orderProcessor;

    @Autowired
    public RestaurantOrderService(CustomerOrderRepository customerOrderRepository,
                                  OrderProcessor orderProcessor) {
        this.customerOrderRepository = customerOrderRepository;
        this.orderProcessor = orderProcessor;
    }

    public OrderedItemWrapper addToCart(String username, OrderedItem orderedItem) {
        List<OrderedItem> orderedItems;
        orderedItems = Optional.of(carts)
                .filter(carts -> carts.containsKey(username))
                .map(carts -> carts.get(username))
                .orElse(new ArrayList<>());
        orderedItems.add(orderedItem);
        carts.put(username, orderedItems);
        return new OrderedItemWrapper(orderedItems, calculateTotalPrice(orderedItems));
    }

    public OrderedItemWrapper removeFromCart(String username, OrderedItem orderedItem) {
        Optional.of(carts)
                .filter(carts -> carts.containsKey(username))
                .map(cart -> {
                    List<OrderedItem> orderedItems = cart.get(username);
                    carts.put(username, orderedItems.stream()
                            .filter(item -> !Objects.equals(item.menuItemId, orderedItem.menuItemId))
                            .collect(Collectors.toList()));
                    return null;
                });
        return new OrderedItemWrapper(carts.get(username), calculateTotalPrice(carts.get(username)));
    }

    public OrderResponse placeOrder(String username, Order order) throws ApiException {
        List<OrderedItem> cart = carts.get(username);
        validateOrderAgainstCart(order, cart);

        OrderResponse orderResponse = orderProcessor
                .placeOrder(username,
                        order,
                        calculateTotalPrice(cart));

        return Optional.ofNullable(orderResponse)
                .filter(response -> response.status.equals(OrderStatusType.ORDERED.getStatus()))
                .map(response -> {
                    carts.remove(username);
                    return response;
                })
                .orElseThrow(() -> new ApiException(502, "unable to place order"));
    }

    public OrderStatus getOrderStatus(Long orderId, String username) throws ApiException {
        CustomerOrder customerOrder = customerOrderRepository.findOne(orderId);

        return Optional.ofNullable(customerOrder)
                .filter(order -> order.getUsername().equals(username))
                .map(order -> orderProcessor.getOrderStatus(orderId))
                .orElseThrow(() -> new ApiException(404, "Invalid OrderId"));
    }

    public OrderedItemWrapper getCart(String username) {
        return new OrderedItemWrapper(carts.get(username), calculateTotalPrice(carts.get(username)));
    }

    private Integer calculateTotalPrice(List<OrderedItem> orderedItems) {
        return orderedItems.stream()
                .filter(Objects::nonNull)
                .mapToInt(item -> item.unitPrice * item.quantity)
                .sum();
    }

    private void validateOrderAgainstCart(final Order order, final List<OrderedItem> orderedItems) throws ApiException {
        List<Boolean> errors = new ArrayList<>();
        errors.add(Objects.isNull(orderedItems) || order.cart.size() != orderedItems.size());

        orderedItems
                .forEach(item -> {
                    if (order.cart
                            .stream()
                            .noneMatch(cart ->
                                    Objects.equals(cart.menuItemId, item.menuItemId)
                                            && Objects.equals(cart.quantity, item.quantity)
                            )) {
                        errors.add(true);
                    }
                });
        if (errors.size() > 0 && errors.contains(true)) {
            throw new ApiException(400, "Ordered item not matching with cart");
        }
    }
}
