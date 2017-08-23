package se.apegroup.pizzaapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import se.apegroup.pizza.client.domain.MenuItem
import se.apegroup.pizzaapp.api.RestaurantMenuApi
import spock.lang.Specification

@ContextConfiguration(classes = PizzaappApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantMenuApiIT extends Specification {

    @Autowired
    RestaurantMenuApi restaurantMenuApi

    def "Should get menus for the given restaurant"() {
        given:
        def id = "1"

        when:
        def result = restaurantMenuApi.getMenu(id)

        then:
        result.statusCode.value() == 200
        result.body instanceof List<MenuItem>
        ((List<MenuItem>)result.body).size() > 0
    }
}
