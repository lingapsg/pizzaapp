package se.apegroup.pizzaapp.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.apegroup.pizzaapp.application.RestaurantOrderService;
import se.apegroup.pizzaapp.domain.OrderedItem;
import se.apegroup.pizzaapp.domain.OrderedItemWrapper;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/v1/pizza/carts")
public class CartApi {

    private final RestaurantOrderService restaurantOrderService;
    private final AuthorizationServerTokenServices tokenServices;

    @Autowired
    public CartApi(RestaurantOrderService restaurantOrderService,
                              AuthorizationServerTokenServices tokenServices) {
        this.restaurantOrderService = restaurantOrderService;
        this.tokenServices = tokenServices;
    }

    @ApiOperation(value = "add item to cart", response = OrderedItemWrapper.class)
    @RequestMapping(value = "/item", method = RequestMethod.PUT)
    public ResponseEntity addToCart(@ApiIgnore OAuth2Authentication authentication,
                                    @RequestBody OrderedItem orderedItem) {
        Map<String, Object> userInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        return ResponseEntity.ok(restaurantOrderService.addToCart((String) userInfo.get("username"), orderedItem));
    }

    @ApiOperation(value = "remove item from cart", response = OrderedItemWrapper.class)
    @RequestMapping(value = "/item", method = RequestMethod.DELETE)
    public ResponseEntity removeFromCart(@ApiIgnore OAuth2Authentication authentication,
                                         @RequestBody OrderedItem orderedItem) {
        Map<String, Object> userInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        return ResponseEntity.ok(restaurantOrderService.removeFromCart((String) userInfo.get("username"), orderedItem));
    }

    @ApiOperation(value = "get cart items", response = OrderedItemWrapper.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getCart(@ApiIgnore OAuth2Authentication authentication) {
        Map<String, Object> userInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        return ResponseEntity.ok(restaurantOrderService.getCart((String) userInfo.get("username")));
    }
}
