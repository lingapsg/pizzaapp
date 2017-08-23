package se.apegroup.pizzaapp.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.apegroup.pizza.client.domain.Restaurant;
import se.apegroup.pizzaapp.application.RestaurantService;

@RestController
@RequestMapping(value = "/v1/pizza/restaurants")
public class RestaurantApi {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantApi(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @ApiOperation(value = "get closest restaurant", response = Restaurant.class)
    @RequestMapping(value = "/closest", method = RequestMethod.GET)
    public ResponseEntity getClosestRestaurant(@RequestParam("address") String address,
                                               @RequestParam(value = "radius", required = false, defaultValue = "15000") Integer radius) throws Exception {
        return ResponseEntity.ok().body(restaurantService.findClosest(address, radius));
    }

    @ApiOperation(value = "get restaurants", response = Restaurant.class, responseContainer = "List")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getRestaurants() throws Exception {
        return ResponseEntity.ok().body(restaurantService.getRestaurants());
    }
}