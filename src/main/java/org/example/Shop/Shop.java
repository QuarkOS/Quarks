package org.example.Shop;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.JSONManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BService
public class Shop {

    private List<Item> shopInventory;
    private Cart cart;

    public Shop() {
        if (Files.exists(Path.of("jsonoutput.json"))) {
            this.shopInventory = JSONManager.loadShopData("jsonoutput.json");
        } else {
            this.shopInventory = List.of(
                    new Item("Red Bull - Orange Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Red Bull - Original", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Red Bull - Yellow Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Red Bull - Blue Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Red Bull - Green Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Red Bull - White Edition", 1.50, 10, ItemCategory.DRINKS),
                    new Item("Jumpy's Paprika", 2.00, 10, ItemCategory.SNACKS),
                    new Item("Pringle's Original", 2.50, 10, ItemCategory.SNACKS),
                    new Item("Pringle's Sour Cream & Onion", 2.50, 10, ItemCategory.SNACKS),
                    new Item("Pringle's Paprika", 2.50, 10, ItemCategory.SNACKS),
                    new Item("Skittles", 1.00, 10, ItemCategory.SNACKS),
                    new Item("NicNac's", 2.00, 10, ItemCategory.SNACKS)
            );
        }
        this.cart = new Cart();
    }

    public double confirmCheckout(TextChannel channel) {
        if (cart.isEmpty()) {
            System.out.println("The cart is empty. Cannot confirm checkout.");
            return -2.0;
        }
        if (checkIfAnyItemInCartIsNotAvailableInShopInventory(cart)) {
            System.out.println("One or more items in the cart are not available in the shop inventory. Cannot confirm checkout.");
            return -1.0;
        }

        else {
            double total = cart.calculateTotal();
            EmbedBuilder embed = cart.getCartList(cart);
            embed.setTitle("Checkout Confirmation");
            embed.setDescription("Total amount: $" + total);
            embed.setFooter("Thank you for shopping with us!", null);
            channel.sendMessageEmbeds(embed.build()).queue();
            System.out.println("Checkout confirmed. Total amount: $" + total);

            for (Item item : cart.getItems()) {
                for (Item shopItem : shopInventory) {
                    if (item.getName().equals(shopItem.getName())) {
                        shopItem.setQuantity(shopItem.getQuantity() - 1);
                    }
                }
            }

            cart.clearCart();
            return total;
        }
    }

    private boolean checkIfAnyItemInCartIsNotAvailableInShopInventory(Cart cart) {
        summarizeCart(cart);
        for (Item item : cart.getItems()) {
            for (Item shopItem : shopInventory) {
                if (item.getName().equals(shopItem.getName())) {
                    if (shopItem.getQuantity() == 0 || item.getQuantity() > shopItem.getQuantity()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void summarizeCart(Cart cart) {
        Map<String, Item> itemSummary = new HashMap<>();

        // Combine quantities and ensure prices are considered
        for (Item item : cart.getItems()) {
            if (itemSummary.containsKey(item.getName())) {
                Item existingItem = itemSummary.get(item.getName());
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                itemSummary.put(item.getName(), new Item(item.getName(), item.getPrice(), item.getQuantity(), item.getCategory()));
            }
        }

        // Clear the current cart and add the summarized items
        cart.clearCart();
        for (Item summarizedItem : itemSummary.values()) {
            cart.addItem(summarizedItem, summarizedItem.getQuantity());
        }
    }

    private String listToString(List<Item> itemList) {
        StringBuilder sb = new StringBuilder();
        for (Item item : itemList) {
            sb.append(item.getName()).append(" - $").append(item.getPrice()).append(" - Stock: ").append(item.getQuantity()).append("\n");
        }
        return sb.toString();
    }

    public void cancelCheckout() {
        cart.clearCart();
        System.out.println("Checkout canceled. The cart has been emptied.");
    }

    public boolean addItem(String item, int quantity) {
        if (getItemByString(item) == null) {
            System.out.println("Item not found in the shop inventory.");
            return false;
        } else {
            cart.addItem(getItemByString(item), quantity);
            System.out.println(item + " " + quantity + "x has been added to the cart.");
            return getItemByString(item) != null;
        }
    }

    private Item getItemByString(String input) {
        for (Item item : shopInventory) {
            if (item.getName().equalsIgnoreCase(input)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        cart.removeItem(item);
        System.out.println(item.getName() + " has been removed from the cart.");
    }

    public void viewCart() {
        cart.viewCart();
    }

    public void viewItems() {
        System.out.println("Available items in the shop:");
        for (Item item : shopInventory) {
            System.out.println(item);
        }
    }

    public List<Item> getShopInventory() {
        return shopInventory;
    }

    public Cart getCart() {
        return cart;
    }

    public void saveAsJSON() {
        // Save shop data as JSON
        JSONManager.saveShopData(this.shopInventory);
    }
}
