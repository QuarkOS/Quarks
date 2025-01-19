package org.example.Shop;

import io.github.freya022.botcommands.api.core.service.annotations.BService;
import org.example.JSONManager;

import java.util.List;

@BService
public class Shop {

    private List<Item> shopInventory;
    private Cart cart;

    public Shop() {
        this.shopInventory = List.of(
                new Item("Apple", 0.50, 10),
                new Item("Banana", 0.30, 20),
                new Item("Orange", 0.70, 15)
        );
        this.cart = new Cart();
    }

    public double confirmCheckout() {
        if (cart.isEmpty()) {
            System.out.println("The cart is empty. Cannot confirm checkout.");
            return 0.0;
        } else {
            double total = cart.calculateTotal();
            System.out.println("Checkout confirmed. Total amount: $" + total);
            cart.clearCart();
            return total;
        }
    }

    public void cancelCheckout() {
        cart.clearCart();
        System.out.println("Checkout canceled. The cart has been emptied.");
    }

    public boolean addItem(String item) {
        cart.addItem(getItemByString(item));
        System.out.println(item + " has been added to the cart.");
        if (getItemByString(item) == null) {
            return false;
        }
        return true;
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
