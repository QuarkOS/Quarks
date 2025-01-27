package org.example.Shop;

import io.github.freya022.botcommands.api.core.service.annotations.BService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@BService
public class ItemService {
    private final List<Item> items;

    public ItemService(List<Item> items) {
        if (items == null || items.isEmpty()) {
            this.items = List.of(
                    new Item(1, "Red Bull - Orange Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item(2, "Red Bull - Original", 1.50, 10, ItemCategory.DRINKS),
                    new Item(3, "Red Bull - Yellow Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item(4, "Red Bull - Blue Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item(5, "Red Bull - Green Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item(6, "Red Bull - White Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item(7, "Jumpy's Paprika", 2.00, 10, ItemCategory.SNACKS),
                    new Item(8, "Pringle's Original", 2.50, 10, ItemCategory.SNACKS),
                    new Item(9, "Pringle's Sour Cream & Onion", 2.50, 10, ItemCategory.SNACKS),
                    new Item(10, "Pringle's Paprika", 2.50, 10, ItemCategory.SNACKS),
                    new Item(11, "Skittles", 1.00, 10, ItemCategory.SNACKS),
                    new Item(12, "NicNac's", 2.00, 10, ItemCategory.SNACKS)
            );
        } else {
            this.items = items;
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public Optional<Item> getItemById(int id) {
        return items.parallelStream()
                .filter(item -> item.getId() == id)
                .findFirst();
    }

    public Optional<Item> getItemByName(String name) {
        return items.parallelStream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst();
    }

}
