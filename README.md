# PizzaApp

PizzaApp is a backend application built using spring boot for ordering pizza online. Implemented authentication and authorization using OAuth2.

##APIs

The application has implementation of the following REST APIs.
* /v1/pizza/restaurants/ to fetch pizza restaurants
* /v1/pizza/restaurants/closest to find the closest restaurant to the given address.
* /v1/pizza/restaurants/{id}/menu to fetch menu for the given restaurant.
* /v1/pizza/carts/item to add item to shopping cart
* /v1/pizza/carts/item to remove item from shopping cart
* /v1/pizza/carts to get all items in the cart
* /v1/pizza/orders/ to place pizza order
* /v1/pizza/orders/status to find the status of placed order

##Modules
* Main module contains all the APIs and business logic for ordering pizza.
* google-api-client contains client to interact with geo APIs for finding location and distance.
* pizza-api-client contains client to interact with pizzaapp.apiary specification.

###Note
The following apiary specification mock APIs have been re-used for simplification purpose and 
to focus more on security and order processing. 

* Get Restaurants
* Get Restaurant By Id
* Get Restaurant Menu

## How to Build and Run

Java 8 is required to build and run this project.

Compile and test:

	gradlew build
	
Run:

	gradlew bootrun
	
###Test

REST APIs are secured using OAuth.

To test the application, we would need to get the oauth access token.

Client Id: pizza-trusted-client

Client secret: secret

Authorization: cGl6emEtdHJ1c3RlZC1jbGllbnQ6c2VjcmV0 - base64 encoded

     $ curl -X POST --header "Authorization: Basic cGl6emEtdHJ1c3RlZC1jbGllbnQ6c2VjcmV0" --insecure 'https://localhost:8010/oauth/token?grant_type=password&username=testuser&password=testuser'

