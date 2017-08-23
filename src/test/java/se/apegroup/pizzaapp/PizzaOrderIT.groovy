package se.apegroup.pizzaapp

import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang3.StringUtils
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import se.apegroup.pizza.client.domain.Cart
import se.apegroup.pizza.client.domain.MenuItem
import se.apegroup.pizza.client.domain.Restaurant
import se.apegroup.pizzaapp.domain.Order
import se.apegroup.pizzaapp.domain.OrderResponse
import se.apegroup.pizzaapp.domain.OrderStatus
import se.apegroup.pizzaapp.domain.OrderStatusType
import se.apegroup.pizzaapp.domain.OrderedItem
import se.apegroup.pizzaapp.domain.OrderedItemWrapper
import spock.lang.Specification

import javax.net.ssl.SSLContext
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import static org.apache.commons.lang3.StringUtils.isNotEmpty

@ContextConfiguration(classes = PizzaappApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PizzaOrderIT extends Specification {

    RestTemplate restTemplate

    def setup() {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true
            }
        }
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build()

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier())

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build()

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory()

        requestFactory.setHttpClient(httpClient)
        restTemplate = new RestTemplate(requestFactory)
    }

    @LocalServerPort
    int serverPort

    def "Should place order"() {
        given:
        HttpHeaders headers = createHttpHeaders("pizza-trusted-client", "secret")
        HttpEntity<String> entity = new HttpEntity<>(headers)
        def address = "Midskeppsgatan 8 12066"

        when://get access token
        def result = restTemplate.exchange(urls("oauth", null), HttpMethod.POST, entity, OauthResponse.class)

        then:
        result.statusCode.value() == 200
        isNotEmpty(result.body.access_token)

        when://get closest restaurant
        def restaurantUrl = "https://localhost:"+serverPort+"/v1/pizza/restaurants/closest?access_token="+result.body.access_token+"&address="+address
        def restaurantEntity = restTemplate.getForEntity(restaurantUrl, Restaurant.class)

        then:
        restaurantEntity.statusCode.value() == 200
        isNotEmpty(restaurantEntity.body.name)

        when://get restaurant menu
        def id = restaurantEntity.body.id
        def menuUrl = "https://localhost:"+serverPort+"/v1/pizza/restaurants/"+id+"/menu?access_token="+result.body.access_token
        def menuEntity = restTemplate.getForEntity(menuUrl, List.class)

        then:
        menuEntity.statusCode.value() == 200
        menuEntity.body.size() > 0


        when://add item to cart
        def item1 = (Map<String, Object>)menuEntity.body.get(0)
        def itemId1 = item1.get("id")
        def itemPrice1 = item1.get("price")
        def orderedItem = new OrderedItem(itemId1, 2, restaurantEntity.body.id, itemPrice1)
        def cartUrl = "https://localhost:"+serverPort+"/v1/pizza/carts/item?access_token="+result.body.access_token
        HttpEntity<OrderedItem> httpEntity = new HttpEntity<>(orderedItem)
        def cartEntity = restTemplate.exchange(cartUrl, HttpMethod.PUT, httpEntity, OrderedItemWrapper.class)

        then:
        cartEntity.statusCode.value() == 200
        cartEntity.body.totalPrice == itemPrice1 * 2

        when: //add another item into cart
        def item2 = (Map<String, Object>)menuEntity.body.get(1)
        def itemId2 = item2.get("id")
        def itemPrice2 = item2.get("price")
        orderedItem = new OrderedItem(itemId2, 2, restaurantEntity.body.id, itemPrice2)
        httpEntity = new HttpEntity<>(orderedItem)
        cartEntity = restTemplate.exchange(cartUrl, HttpMethod.PUT, httpEntity, OrderedItemWrapper.class)

        then:
        cartEntity.statusCode.value() == 200
        cartEntity.body.totalPrice == itemPrice1 * 2 + itemPrice2 * 2

        when: //remove item from cart
        orderedItem = new OrderedItem(item2.id, 2, restaurantEntity.body.id, item2.price)
        httpEntity = new HttpEntity<>(orderedItem)
        cartEntity = restTemplate.exchange(cartUrl, HttpMethod.DELETE, httpEntity, OrderedItemWrapper.class)

        then:
        cartEntity.statusCode.value() == 200
        cartEntity.body.totalPrice == item1.price * 2

        when: //get cart items
        def getCartUrl = "https://localhost:"+serverPort+"/v1/pizza/carts/?access_token="+result.body.access_token
        def getCartEntity = restTemplate.getForEntity(getCartUrl, OrderedItemWrapper.class)

        then:
        getCartEntity.statusCode.value() == 200
        getCartEntity.body == cartEntity.body

        when: //place order
        def orderUrl = "https://localhost:"+serverPort+"/v1/pizza/orders/?access_token="+result.body.access_token
        List<Cart> carts = new ArrayList<>()
        getCartEntity.body.orderedItems.each { item ->
            carts.add(new Cart(item.menuItemId, item.quantity))
        }
        def order = new Order(carts, restaurantEntity.body.id, address)
        def orderEntity = restTemplate.postForEntity(orderUrl, order, OrderResponse.class)

        then:
        orderEntity.statusCode.value() == 200
        orderEntity.body.orderId != 0
        orderEntity.body.totalPrice == getCartEntity.body.totalPrice
        orderEntity.body.status == OrderStatusType.ORDERED.status

        when: //check for order status
        def orderStatusUrl = "https://localhost:"+serverPort+"/v1/pizza/orders/status?access_token="+result.body.access_token+"&orderId="+orderEntity.body.orderId
        def orderStatusEntity = restTemplate.getForEntity(orderStatusUrl, OrderStatus.class)

        then:
        orderStatusEntity.statusCode.value() == 200
        orderStatusEntity.body.status == OrderStatusType.BAKING.status

    }

    private static createHttpHeaders(String client, String clientSecret) {
        String basicAuth = client + ":" + clientSecret
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(basicAuth.getBytes("UTF-8"))
        HttpHeaders headers = new HttpHeaders()
        headers.add("Authorization", "Basic "+new String(encodedAuth))
        headers
    }

    private urls(String key, String accessToken) {
        Map<String, String> urls = new HashMap<>()
        urls.put("oauth", "https://localhost:"+serverPort+"/oauth/token?grant_type=password&username=testuser&password=testuser")
        urls.put("closest", "https://localhost:"+serverPort+"/v1/pizza/restaurants/closest?access_token="+accessToken)
        urls.get(key)
    }
}
