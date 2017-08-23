package se.apegroup.pizzaapp.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.apegroup.pizzaapp.application.RestaurantMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import se.apegroup.pizza.client.domain.MenuItem;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/v1/pizza")
public class RestaurantMenuApi {

    private final RestaurantMenuService restaurantMenuService;

    @Autowired
    public RestaurantMenuApi(RestaurantMenuService restaurantMenuService) {
        this.restaurantMenuService = restaurantMenuService;
    }

    @ApiOperation(value = "get restaurant menu", response = MenuItem.class, responseContainer = "List")
    @RequestMapping(value = "/restaurants/{id}/menu", method = RequestMethod.GET)
    public ResponseEntity getMenu(@PathVariable(value = "id") String restaurantId) throws Exception {
        return ResponseEntity.ok(restaurantMenuService.getMenu(Integer.parseInt(restaurantId)));
    }
}
