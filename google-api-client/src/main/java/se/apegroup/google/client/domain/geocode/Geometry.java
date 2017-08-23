package se.apegroup.google.client.domain.geocode;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    private Location location;
    @SerializedName("location_type")
    private String locationType;
    private Viewport viewport;
    private Bounds bounds;
}