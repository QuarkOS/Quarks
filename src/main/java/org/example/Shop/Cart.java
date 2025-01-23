package org.example.Shop;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
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
            total += item.getPrice();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public EmbedBuilder getCartList(Cart cart) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDCE6 **Cart Items** \uD83D\uDCE6");
        embed.setDescription("Here's what's in your cart:");
        embed.setColor(Color.ORANGE);

        if (cart.isEmpty()) {
            embed.setDescription("Your cart is currently empty. Add items using `/shop add-to-cart`.");
        } else {
            Map<String, Integer> itemQuantities = new HashMap<>();

            // Aggregate quantities
            for (Item item : cart.getItems()) {
                itemQuantities.put(item.getName(), itemQuantities.getOrDefault(item.getName(), 0) + 1);
            }

            // Create formatted list
            for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
                embed.addField(entry.getKey(), entry.getValue() + "x", false);
            }

            embed.setFooter("Ready to checkout? Use `/shop confirm-purchase`");
        }

        return embed;
    }
}
