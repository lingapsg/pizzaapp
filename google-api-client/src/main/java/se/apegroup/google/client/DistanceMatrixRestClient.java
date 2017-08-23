package se.apegroup.google.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import se.apegroup.google.client.domain.distancematrix.DistanceResponse;

import java.util.Map;

public interface DistanceMatrixRestClient {

    @GET("json")
    Call<DistanceResponse> getDistance(@QueryMap Map<String, String> queryMap);
}
