package se.apegroup.google.client.domain.geocode;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressComponent {

    @SerializedName("long_name")
    public final String longName;
    @SerializedName("short_name")
    public final String shortName;
    public final List<String> types;

    public AddressComponent(String longName, String shortName, List<String> types) {
        this.longName = longName;
        this.shortName = shortName;
        this.types = types;
    }
}