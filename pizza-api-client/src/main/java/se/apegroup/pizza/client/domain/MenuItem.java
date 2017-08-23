package se.apegroup.pizza.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class MenuItem {

    public final Integer id;
    public final String category;
    public final String name;
    public final List<String> topping;
    public final Integer price;
    public final int rank;

    @JsonCreator
    public MenuItem(Integer id, String category, String name, List<String> topping,
                    Integer price, int rank) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.topping = topping;
        this.price = price;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}