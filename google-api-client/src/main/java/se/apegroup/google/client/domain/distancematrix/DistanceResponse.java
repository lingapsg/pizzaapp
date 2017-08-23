package se.apegroup.google.client.domain.distancematrix;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DistanceResponse {

    @SerializedName("destination_addresses")
    public final List<String> destinationAddresses;
    @SerializedName("origin_addresses")
    public final List<String> originAddresses;
    public final List<Row> rows;
    public final String status;

    public DistanceResponse(List<String> destinationAddresses, List<String> originAddresses, List<Row> rows, String status) {
        this.destinationAddresses = destinationAddresses;
        this.originAddresses = originAddresses;
        this.rows = rows;
        this.status = status;
    }

    public class Row {

        public final List<Element> elements;

        public Row(List<Element> elements) {
            this.elements = elements;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public class Element {

        public final Distance distance;
        public final Duration duration;
        public final String status;

        public Element(Distance distance, Duration duration, String status) {
            this.distance = distance;
            this.duration = duration;
            this.status = status;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public class Distance {

        public final String text;
        public final Integer value;

        public Distance(String text, Integer value) {
            this.text = text;
            this.value = value;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public class Duration {

        public final String text;
        public final Integer value;

        public Duration(String text, Integer value) {
            this.text = text;
            this.value = value;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
