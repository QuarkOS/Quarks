package org.example.Commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import org.example.Shop.*;

import java.util.List;

@Command
public class ShopCommand extends ApplicationCommand {

    private Shop shop;

    public ShopCommand() {
        // Initialize shop with some items
        List<Item> shopInventory = List.of(
                new Item("Apple", 0.50, 10),
                new Item("Banana", 0.30, 20),
                new Item("Orange", 0.70, 15)
        );
        this.shop = new Shop(shopInventory);
    }

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "shop", subcommand = "help", description = "Shop related commands")
    public void onShopCommand(GuildSlashEvent event) {
        event.deferReply(true).queue();
        event.getHook().editOriginal("Shop related commands:\n" +
                "/shop viewitems - View available items in the shop\n" +
                "/shop additem <item> - Add an item to the cart\n" +
                "/shop viewcart - View items in the cart\n" +
                "/shop checkout - Confirm the checkout\n" +
                "/shop cancel - Cancel the checkout").queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "viewitems", description = "View available items in the shop")
    public void onViewItems(GuildSlashEvent event) {
        event.deferReply(true).queue();
        StringBuilder response = new StringBuilder("Available items in the shop:\n");
        for (Item item : shop.getShopInventory()) {
            response.append(item).append("\n");
        }
        event.getHook().editOriginal(response.toString()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "additem", description = "Add an item to the cart")
    public void onAddItem(GuildSlashEvent event, @SlashOption String itemName) {
        event.deferReply(true).queue();
        Item item = shop.getShopInventory().stream()
                .filter(i -> i.getName().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);
        if (item != null) {
            shop.addItem(item);
            event.getHook().editOriginal(itemName + " has been added to the cart.").queue();
        } else {
            event.getHook().editOriginal("Item not found in the shop.").queue();
        }
    }

    @JDASlashCommand(name = "shop", subcommand = "viewcart", description = "View items in the cart")
    public void onViewCart(GuildSlashEvent event) {
        event.deferReply(true).queue();
        StringBuilder response = new StringBuilder("Items in your cart:\n");
        if (shop.getCart().isEmpty()) {
            response.append("The cart is empty.");
        } else {
            for (Item item : shop.getCart().getItems()) {
                response.append(item).append("\n");
            }
        }
        event.getHook().editOriginal(response.toString()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "checkout", description = "Confirm the checkout")
    public void onCheckout(GuildSlashEvent event) {
        event.deferReply(true).queue();
        double total = shop.getCart().calculateTotal();
        shop.confirmCheckout();
        event.getHook().editOriginal("Checkout confirmed. Total amount: $" + total).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "cancel", description = "Cancel the checkout")
    public void onCancelCheckout(GuildSlashEvent event) {
        event.deferReply(true).queue();
        shop.cancelCheckout();
        event.getHook().editOriginal("Checkout canceled. The cart has been emptied.").queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "save", description = "Save shop data")
    public void onSaveShopData(GuildSlashEvent event) {
        event.deferReply(true).queue();
        shop.saveAsJSON();
        event.getHook().editOriginal("Shop data saved successfully.").queue();
    }
}
