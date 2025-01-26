package org.example.Shop;

import java.util.Map;

public class Bundle {
    private Map<Item, Integer> items;

    public Bundle(Map<Item, Integer> items) {
        this.items = items;
    }

    public void addItem(Item item, int quantity) {
        items.put(item, quantity);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Map<Item, Integer> getItems() {
        return items;
    }
}
