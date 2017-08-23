package se.apegroup.pizzaapp.infrastructure.rest;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import retrofit2.Response;
import se.apegroup.google.client.GeocodeRestClient;
import se.apegroup.google.client.domain.geocode.GeocodeResponse;
import se.apegroup.pizzaapp.exception.ApiException;

import java.io.IOException;
import java.util.Optional;

@Repository
public class GoogleGeocodeRepository {

    private static final Logger LOGGER = LogManager.getLogger(GoogleGeocodeRepository.class);

    private final GeocodeRestClient geocodeRestClient;

    @Autowired
    public GoogleGeocodeRepository(GeocodeRestClient geocodeRestClient) {
        this.geocodeRestClient = geocodeRestClient;
    }

    public GeocodeResponse getLocationByAddress(String address) throws ApiException {
        try {
            Response<GeocodeResponse> geocodeResponse =
                    geocodeRestClient.getLocation(ImmutableMap.of("address", address)).execute();

            return Optional.of(geocodeResponse)
                    .filter(Response::isSuccessful)
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            LOGGER.error("error ",e);
            throw new ApiException(502, "Unable to find restaurants");
        }
    }

    public GeocodeResponse getLocationByLatLng(Double lat, Double lng) throws ApiException {
        try {
            Response<GeocodeResponse> geocodeResponse =
                    geocodeRestClient.getLocation(ImmutableMap.of("latlng", lat+","+lng)).execute();

            return Optional.of(geocodeResponse)
                    .filter(Response::isSuccessful)
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            throw new ApiException(502, "Unable to find restaurants");
        }
    }
}
