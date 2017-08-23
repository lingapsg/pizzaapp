package se.apegroup.pizza.client.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.apegroup.pizza.client.PizzaRestClient;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Configuration
public class PizzaRestClientConfiguration {

    @NotNull
    @Value("${integration.pizzaapi.url}")
    private String clientUrl;

    @Bean
    public PizzaRestClient pizzaRestClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime()
                )
                .create();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                .baseUrl(clientUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit =
                builder.client(httpClient.build())
                .build();

        return retrofit.create(PizzaRestClient.class);
    }

}
