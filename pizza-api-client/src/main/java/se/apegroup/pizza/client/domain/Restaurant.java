package se.apegroup.pizza.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Restaurant {

    public final Integer id;
    public final String name;
    public final String address1;
    public final String address2;
    public final Double latitude;
    public final Double longitude;

    @JsonCreator
    public Restaurant(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                      @JsonProperty("address1") String address1, @JsonProperty("address2") String address2,
                      @JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFullAddress() {
        return address1.concat(" ").concat(address2);
    }
}