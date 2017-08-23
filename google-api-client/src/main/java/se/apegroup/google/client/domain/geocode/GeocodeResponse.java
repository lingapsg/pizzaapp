package se.apegroup.google.client.domain.geocode;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;


public class GeocodeResponse {

    public final List<Result> results;
    public final String status;

    public GeocodeResponse(List<Result> results, String status) {
        this.results = results;
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}