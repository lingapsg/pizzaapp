package se.apegroup.pizzaapp.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.*;
import se.apegroup.pizza.client.domain.Cart;
import se.apegroup.pizzaapp.domain.*;
import se.apegroup.pizzaapp.application.RestaurantOrderService;
import se.apegroup.pizzaapp.exception.ApiException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/v1/pizza/orders")
public class RestaurantOrderApi {

    private final RestaurantOrderService restaurantOrderService;
    private final AuthorizationServerTokenServices tokenServices;

    @Autowired
    public RestaurantOrderApi(RestaurantOrderService restaurantOrderService,
                              AuthorizationServerTokenServices tokenServices) {
        this.restaurantOrderService = restaurantOrderService;
        this.tokenServices = tokenServices;
    }

    @ApiOperation(value = "place order", response = OrderResponse.class)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity placeOrder(@ApiIgnore OAuth2Authentication authentication,
                                     @RequestBody Order order) throws ApiException {
        Map<String, Object> userInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        return ResponseEntity.ok(restaurantOrderService.placeOrder((String) userInfo.get("username"),
                order));
    }

    @ApiOperation(value = "get order status", response = OrderStatus.class)
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity getOrderStatus(@ApiIgnore OAuth2Authentication authentication,
                                         @RequestParam Long orderId) throws ApiException {
        Map<String, Object> userInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        return ResponseEntity.ok(restaurantOrderService.getOrderStatus(orderId, (String) userInfo.get("username")));
    }
}
