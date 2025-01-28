package org.example.Shop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Bundle {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("items")
    private final Map<Integer, Integer> items;
    @JsonProperty("price")
    private double price;

    public Bundle(String name) {
        this.name = name;
        this.items = new HashMap<>();
    }

    @JsonCreator
    private Bundle(@JsonProperty("name") String name, @JsonProperty("items") Map<Integer, Integer> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void addItem(Item item, int quantity) {
        Integer currentQuantity = items.getOrDefault(item.getId(), 0);
        int newQuantity = currentQuantity + quantity;
        items.put(item.getId(), newQuantity);
    }

    public void removeItem(Item item) {
        items.remove(item.getId());
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
