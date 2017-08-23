package se.apegroup.pizzaapp.infrastructure.rest;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import retrofit2.Response;
import se.apegroup.google.client.DistanceMatrixRestClient;
import se.apegroup.google.client.domain.distancematrix.DistanceResponse;
import se.apegroup.pizzaapp.exception.ApiException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Repository
public class GoogleDistanceRepository {

    private static final Logger LOGGER = LogManager.getLogger(GoogleDistanceRepository.class);

    private final DistanceMatrixRestClient distanceMatrixRestClient;
    private final String apiKey;

    @Autowired
    public GoogleDistanceRepository(DistanceMatrixRestClient distanceMatrixRestClient,
                                    @Value("${integration.google.key}") @NotNull String apiKey) {
        this.distanceMatrixRestClient = distanceMatrixRestClient;
        this.apiKey = apiKey;
    }

    public DistanceResponse getDistance(String origins, String destinations) throws ApiException {
        try {
            Response<DistanceResponse> distanceResponse = distanceMatrixRestClient.getDistance(
                    queryParamMap(origins, destinations)
            ).execute();

            return Optional.of(distanceResponse)
                    .filter(Response::isSuccessful)
                    .map(Response::body)
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            LOGGER.info("error :",e);
            throw new ApiException(502, "Unable to find restaurants");
        }
    }

    private Map<String, String> queryParamMap(String origins, String destinations) {
        return ImmutableMap.of(
                "origins", origins,
                "destinations", destinations,
                "key", apiKey);
    }
}
