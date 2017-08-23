package se.apegroup.google.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import se.apegroup.google.client.domain.geocode.GeocodeResponse;

import java.util.Map;

public interface GeocodeRestClient {

    @GET("json")
    Call<GeocodeResponse> getLocation(@QueryMap Map<String, String> queryMap);
}
