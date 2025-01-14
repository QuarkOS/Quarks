package org.example.Shop;

import java.util.List;

public class Shop {

    private List<Item> shopInventory;
    private Cart cart;

    public Shop(List<Item> shopInventory) {
        this.shopInventory = shopInventory;
        this.cart = new Cart();
    }

    public void confirmCheckout() {
        if (cart.isEmpty()) {
            System.out.println("The cart is empty. Cannot confirm checkout.");
        } else {
            double total = cart.calculateTotal();
            System.out.println("Checkout confirmed. Total amount: $" + total);
            cart.clearCart();
        }
    }

    public void cancelCheckout() {
        cart.clearCart();
        System.out.println("Checkout canceled. The cart has been emptied.");
    }

    public void addItem(Item item) {
        cart.addItem(item);
        System.out.println(item.getName() + " has been added to the cart.");
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
}
