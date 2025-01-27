package org.example.Shop;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private ItemCategory category;

    public Item(@JsonProperty("id") int id, @JsonProperty("name") String name,@JsonProperty("price") double price, @JsonProperty("quantity") int quantity, @JsonProperty("category") ItemCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public Item() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
