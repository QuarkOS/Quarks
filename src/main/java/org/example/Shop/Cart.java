package org.example.Shop;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("The cart is empty.");
        } else {
            System.out.println("Items in the cart:");
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }

    public List<Item> getItems() {
        return items;
    }
}
