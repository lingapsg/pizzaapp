package se.apegroup.pizzaapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import se.apegroup.pizza.client.domain.Restaurant
import se.apegroup.pizzaapp.api.RestaurantApi
import se.apegroup.pizzaapp.exception.ApiException
import spock.lang.Specification

@ContextConfiguration(classes = PizzaappApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantApiIT extends Specification {

    @Autowired
    RestaurantApi restaurantApi

    def "Should find the closest restaurant for given address"() {
        given:
        def address = "Midskeppsgatan 8 12066"

        when:
        def result = restaurantApi.getClosestRestaurant(address, 10000)

        then:
        result.statusCode.value() == 200
        result.body instanceof Restaurant
    }

    def "Should get 404 for distance range within 5000 metres"() {
        given:
        def address = "Midskeppsgatan 8 12066"

        when:
        restaurantApi.getClosestRestaurant(address, 5000)

        then:
        def result = thrown(ApiException)
        result.errorCode == 404
        result.errorMessage == "No restaurant Found"
    }

    def "Should get restaurants"() {
        when:
        def result = restaurantApi.getRestaurants()

        then:
        result.statusCode.value() == 200
        result.body instanceof List<Restaurant>
        ((List<Restaurant>) result.body).size() > 0
    }
}
