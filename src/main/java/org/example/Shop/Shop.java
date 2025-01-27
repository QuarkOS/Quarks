package org.example.Shop;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.BundleDTO;
import org.example.JSONManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

@BService
public class Shop {

    private static final Logger LOGGER = Logger.getLogger(Shop.class.getName());
    private ItemService itemService;
    private List<Bundle> bundles;
    private Cart cart;

    public Shop(ItemService itemService) {
        this.itemService = itemService;
        BundleDTO data = JSONManager.loadBundleJson();
        this.bundles = new ArrayList<>(data.bundles());
        this.cart = new Cart();
    }

    public Optional<Bundle> findBundleByName(String name) {
        return bundles.stream().filter(bundle -> bundle.getName().equalsIgnoreCase(name)).findFirst();
    }

    public void addBundle(Bundle bundle) {
        bundles.add(bundle);
        LOGGER.info("Bundle " + bundle.getName() + " has been added to the shop.");
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public Optional<Item> findItemByName(String name) {
        return itemService.getItemByName(name);
    }

    public double confirmCheckout(TextChannel channel) {
        if (cart.isEmpty()) {
            System.out.println("The cart is empty. Cannot confirm checkout.");
            return -2.0;
        }
        if (checkIfAnyItemInCartIsNotAvailableInShopInventory(cart)) {
            System.out.println("One or more items in the cart are not available in the shop inventory. Cannot confirm checkout.");
            return -1.0;
        } else {
            double total = cart.calculateTotal();
            EmbedBuilder embed = cart.getCartList(cart);
            embed.setTitle("Checkout Confirmation");
            embed.setDescription("Total amount: $" + total);
            embed.setFooter("Thank you for shopping with us!", null);
            channel.sendMessageEmbeds(embed.build()).queue();
            System.out.println("Checkout confirmed. Total amount: $" + total);

            for (Item item : cart.getItems()) {
                for (Item shopItem : itemService.getItems()) {
                    if (item.getName().equals(shopItem.getName())) {
                        shopItem.setQuantity(shopItem.getQuantity() - 1);
                    }
                }
            }

            cart.clearCart();
            saveAsJSON();
            return total;
        }
    }

    private boolean checkIfAnyItemInCartIsNotAvailableInShopInventory(Cart cart) {
        summarizeCart(cart);
        for (Item item : cart.getItems()) {
            for (Item shopItem : itemService.getItems()) {
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
        Map<String, Integer> itemQuantities = new HashMap<>();
        for (Item item : cart.getItems()) {
            itemQuantities.put(item.getName(), itemQuantities.getOrDefault(item.getName(), 0) + 1);
        }
        System.out.println("Items in the cart:");
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            System.out.println(entry.getKey() + " - Quantity: " + entry.getValue());
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

    public Item getItemByString(String input) {
        for (Item item : itemService.getItems()) {
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
        for (Item item : itemService.getItems()) {
            System.out.println(item);
        }
    }

    public List<Item> getShopInventory() {
        return itemService.getItems();
    }

    public Cart getCart() {
        return cart;
    }

    public void saveAsJSON() {
        // Save shop data as JSON
        //TODO change this to make a separate save file for items
        BundleDTO data = new BundleDTO(bundles);
        JSONManager.saveBundleJson(data);
    }
}
