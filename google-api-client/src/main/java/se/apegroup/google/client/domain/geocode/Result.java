package se.apegroup.google.client.domain.geocode;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Result {

    @SerializedName("address_components")
    public final List<AddressComponent> addressComponents;
    @SerializedName("formatted_address")
    public final String formattedAddress;
    public final Geometry geometry;
    @SerializedName("place_id")
    public final String placeId;
    public final List<String> types;

    public Result(List<AddressComponent> addressComponents, String formattedAddress, Geometry geometry, String placeId, List<String> types) {
        this.addressComponents = addressComponents;
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.placeId = placeId;
        this.types = types;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}