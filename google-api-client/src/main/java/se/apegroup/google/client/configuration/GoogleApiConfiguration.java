package se.apegroup.google.client.configuration;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.apegroup.google.client.DistanceMatrixRestClient;
import se.apegroup.google.client.GeocodeRestClient;

import javax.validation.constraints.NotNull;

@Configuration
public class GoogleApiConfiguration {

    @NotNull
    @Value("${integration.google.geocode-api.url}")
    private String geocodeUrl;

    @NotNull
    @Value("${integration.google.distance-api.url}")
    private String distanceMatrixUrl;

    @Bean
    public GeocodeRestClient geocodeRestClient() {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(geocodeUrl)
                        .addConverterFactory(GsonConverterFactory.create());

        return builder.client(okHttpClient())
                        .build().create(GeocodeRestClient.class);
    }

    @Bean
    public DistanceMatrixRestClient distanceMatrixRestClient() {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(distanceMatrixUrl)
                        .addConverterFactory(GsonConverterFactory.create());

        return builder.client(okHttpClient())
                .build().create(DistanceMatrixRestClient.class);
    }

    private OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
