package se.apegroup.pizzaapp.application;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.apegroup.pizza.client.domain.MenuItem;
import se.apegroup.pizzaapp.infrastructure.rest.RestaurantMenuRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantMenuService {

    private final RestaurantMenuRepository restaurantMenuRepository;

    @Autowired
    public RestaurantMenuService(RestaurantMenuRepository restaurantMenuRepository) {
        this.restaurantMenuRepository = restaurantMenuRepository;
    }

    public List<MenuItem> getMenu(Integer restaurantId) throws Exception {
        return restaurantMenuRepository.GetRestaurantMenu(restaurantId, menuParams())
                .stream()
                .collect(Collectors.toList());
    }

    private Map<String, String> menuParams() {
        return ImmutableMap.of(
                "category", "Pizza",
                "orderBy", "Rank"
        );
    }
}
