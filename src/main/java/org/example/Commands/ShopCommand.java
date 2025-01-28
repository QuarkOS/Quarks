package org.example.Commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import io.github.freya022.botcommands.api.components.Button;
import io.github.freya022.botcommands.api.components.Buttons;
import io.github.freya022.botcommands.api.components.annotations.ComponentData;
import io.github.freya022.botcommands.api.components.annotations.JDAButtonListener;
import io.github.freya022.botcommands.api.components.event.ButtonEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.Commands.AutoCompleters.PaymentAutoComplete;
import org.example.Commands.AutoCompleters.ProductAutoComplete;
import org.example.Shop.Item;
import org.example.Shop.ItemCategory;
import org.example.Shop.Shop;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command
public class ShopCommand extends ApplicationCommand {

    private final Shop shop;
    private final Buttons buttons;
    private static final String SHOW_ALL = "SHOW_ALL";
    private static final String SHOW_DRINKS = "SHOW_DRINKS";
    private static final String SHOW_SNACKS = "SHOW_SNACKS";

    public ShopCommand(Shop shop, Buttons buttons) {
        this.shop = shop;
        this.buttons = buttons;
    }

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "shop", subcommand = "help", description = "Shop related commands")
    public void onShopCommand(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        // Set the title with emojis
        embed.setTitle("\uD83C\uDFEA **Shop Commands** \uD83C\uDFEA");
        embed.setDescription("**Explore the available shop commands below:**\n\n");

        // Add fields with more refined formatting and emojis
        embed.addField("**/shop list-items**", "View available items in the shop.", false);
        embed.addField("**/shop add-to-cart [item name]**", "Add an item to the cart.", false);
        embed.addField("**/shop show-cart**", "View items in the cart.", false);
        embed.addField("**/shop confirm-purchase**", "Confirm the checkout.", false);
        embed.addField("**/shop cancel-order**", "Cancel the checkout.", false);
        embed.addField("**/shop save-shop-data**", "Save shop data.", false);

        // Customize colors for a more dynamic look
        embed.setColor(new Color(0x00BFFF)); // Use a refreshing blue color

        // Add a thumbnail to represent the shop
        embed.setThumbnail("https://cdn.discordapp.com/attachments/1330554717155364916/1330554920214200321/9fd7eedeab0ec7f829ef2af6328ab6a9.png?ex=678e6755&is=678d15d5&hm=ec4ae3b6062e94e3ecd6a132dfd9bff045c62458e33a6a4802d10fb7366a6b27&");

        event.replyEmbeds(embed.build()).queue();
    }

//    @JDASlashCommand(name = "shop", subcommand = "list-items", description = "View available items in the shop")
//    public void onViewItems(GuildSlashEvent event) {
//        EmbedBuilder embed = new EmbedBuilder();
//        embed.setTitle("\uD83D\uDCC8 **Shop Items** \uD83D\uDCC8");
//        embed.setDescription("Explore the array of items available in our shop:");
//        embed.setColor(Color.BLUE);
//
//        // Categorize items
//        StringBuilder energyDrinks = new StringBuilder();
//        StringBuilder snacks = new StringBuilder();
//
//        for (Item item : shop.getShopInventory()) {
//            switch (item.getCategory()) {
//                case ItemCategory.DRINKS:
//                    energyDrinks.append("**").append(item.getName()).append("**\n")
//                            .append("Price: $").append(item.getPrice()).append("\n")
//                            .append("Stock: ").append(item.getQuantity()).append("\n\n");
//                    break;
//                case ItemCategory.SNACKS:
//                    snacks.append("**").append(item.getName()).append("**\n")
//                            .append("Price: $").append(item.getPrice()).append("\n")
//                            .append("Stock: ").append(item.getQuantity()).append("\n\n");
//                    break;
//            }
//        }
//
//        if (energyDrinks.length() > 0) {
//            embed.addField("**Energy Drinks:**", energyDrinks.toString(), false);
//        }
//
//        if (snacks.length() > 0) {
//            embed.addField("**Snacks:**", snacks.toString(), false);
//        }
//
//        embed.setFooter("Happy shopping! 🛒");
//        event.replyEmbeds(embed.build()).queue();
//    }

    @JDASlashCommand(name = "shop", subcommand = "add-to-cart", description = "Add an item to the cart")
    public void onAddItem(GuildSlashEvent event, @SlashOption(name = "product", description = "Product", autocomplete = ProductAutoComplete.PRODUCT_AUTOCOMPLETE_NAME) String itemName, @SlashOption(name = "quantity") int quantity) {
        boolean success = shop.addItem(itemName, quantity);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDED2 **Add Item to Cart** \uD83D\uDED2");
        if (success) {
            embed.setDescription("**" + itemName + " " + quantity + "x ** has been added to your cart. 🛒");
            embed.setColor(Color.GREEN);
        } else {
            embed.setDescription("Item not found or out of stock: **" + itemName + "**");
            embed.setColor(Color.RED);
        }
        embed.setFooter("Check your cart with /shop show-cart");
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "show-cart", description = "View items in the cart")
    public void onViewCart(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDCE6 **Cart Items** \uD83D\uDCE6");
        embed.setDescription("Here's what's in your cart:");
        embed.setColor(Color.ORANGE);

        if (shop.getCart().isEmpty()) {
            embed.setDescription("Your cart is currently empty. Add items using `/shop add-to-cart`.");
        } else {
            Map<String, Integer> itemQuantities = new HashMap<>();
            Map<String, Double> itemPrices = new HashMap<>();
            double total = 0.0;

            // Aggregate quantities and prices
            for (Item item : shop.getCart().getItems()) {
                itemQuantities.put(item.getName(), itemQuantities.getOrDefault(item.getName(), 0) + 1);
                itemPrices.put(item.getName(), item.getPrice());
            }

            // Display aggregated items and calculate total
            for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double price = itemPrices.get(itemName);
                embed.addField(itemName, "**Price**: $" + price + "\n**Quantity**: " + quantity, false);
                total += price * quantity;
            }

            embed.addField("**Total**", "$" + String.format("%.2f", total), false);
            embed.setFooter("Ready to checkout? Use `/shop confirm-purchase`");
        }

        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "confirm-purchase", description = "Confirm the checkout")
    public void onCheckout(GuildSlashEvent event, @SlashOption(name = "location", description = "Location") String location, @SlashOption(name = "payment-method", description = "Payment Method", autocomplete = PaymentAutoComplete.PAYMENT_AUTOCOMPLETE_NAME) String paymentMethod, @SlashOption(name = "name", description = "Name") String name) {
        double total = shop.confirmCheckout(event.getGuild().getTextChannelById("1298223556655714374"), location, paymentMethod, name);

        if (total == -1.0) { // One or More Items not available
            EmbedBuilder embedUnavailable = new EmbedBuilder();
            embedUnavailable.setTitle("🚫 **Checkout Error** 🚫");
            embedUnavailable.setDescription("One or more items in your cart are currently unavailable. Please review your cart and try again.");
            embedUnavailable.setColor(Color.RED);
            embedUnavailable.setFooter("We apologize for the inconvenience.");
            event.replyEmbeds(embedUnavailable.build()).queue();
        } else if (total == -2.0) { // Empty Cart
            EmbedBuilder embedEmptyCart = new EmbedBuilder();
            embedEmptyCart.setTitle("🛒 **Empty Cart** 🛒");
            embedEmptyCart.setDescription("Your cart is empty. Please add items to your cart before checking out.");
            embedEmptyCart.setColor(Color.ORANGE);
            embedEmptyCart.setFooter("Happy shopping!");
            event.replyEmbeds(embedEmptyCart.build()).queue();
        } else { // Normal Checkout
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("\uD83D\uDCB0 **Checkout** \uD83D\uDCB0");
            embed.setDescription(String.format("Your total is: **$%.2f**. Thank you for shopping with us!", total));
            embed.setColor(Color.GREEN);
            embed.setFooter("We hope to see you again!");
            event.replyEmbeds(embed.build()).queue();
        }
    }

    @JDASlashCommand(name = "shop", subcommand = "cancel-purchase", description = "Cancel the checkout")
    public void onCancelCheckout(GuildSlashEvent event) {
        shop.cancelCheckout();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\u274C **Cancel Checkout** \u274C");
        embed.setDescription("Your checkout has been canceled.");
        embed.setColor(Color.RED);
        embed.setFooter("Need help? Use /shop help");
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "save-shop-data", description = "Save shop data")
    public void onSaveShopData(GuildSlashEvent event) {
        shop.saveAsJSON();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDCD1 **Save Shop Data** \uD83D\uDCD1");
        embed.setDescription("Shop data has been saved.");
        embed.setColor(Color.GREEN);
        embed.setFooter("All set! Use /shop help for more commands.");
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "list-items", description = "View shop inventory")
    public void onShopInventory(GuildSlashEvent event) {
        EmbedBuilder embed = getShopItemsEmbed("SHOW_ALL");
        event.replyEmbeds(embed.build())
                .addActionRow(
                        buttons.primary("All Products", Emoji.fromUnicode("U+1F6D2")).persistent().bindTo(SHOW_ALL).build(),
                        buttons.primary("Drinks", Emoji.fromUnicode("U+1F964")).persistent().bindTo(SHOW_DRINKS).build(),
                        buttons.primary("Snacks", Emoji.fromUnicode("U+1F36A")).persistent().bindTo(SHOW_SNACKS).build()
                ).queue();
    }

    @JDAButtonListener(SHOW_ALL)
    public void showAll(ButtonEvent event) {
        EmbedBuilder embed = getShopItemsEmbed("SHOW_ALL");
        event.editMessageEmbeds(embed.build()).queue();
    }

    @JDAButtonListener(SHOW_DRINKS)
    public void showDrinks(ButtonEvent event) {
        EmbedBuilder embed = getShopItemsEmbed("SHOW_DRINKS");
        event.editMessageEmbeds(embed.build()).queue();
    }

    @JDAButtonListener(SHOW_SNACKS)
    public void showSnacks(ButtonEvent event) {
        EmbedBuilder embed = getShopItemsEmbed("SHOW_SNACKS");
        event.editMessageEmbeds(embed.build()).queue();
    }

    private EmbedBuilder getShopItemsEmbed(String category) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDED2 **Shop Inventory** \uD83D\uDED2");
        embed.setColor(Color.CYAN);

        List<Item> items = shop.getShopInventory();
        if (category.equals("SHOW_DRINKS")) {
            items = items.stream().filter(item -> item.getCategory() == ItemCategory.DRINKS).toList();
            embed.setTitle("**Drinks**");
        } else if (category.equals("SHOW_SNACKS")) {
            items = items.stream().filter(item -> item.getCategory() == ItemCategory.SNACKS).toList();
            embed.setTitle("**Snacks**");
        }

        if (items.isEmpty()) {
            embed.setDescription("No items available in this category.");
        } else {
            StringBuilder description = new StringBuilder();
            for (Item item : items) {
                description.append("**").append(item.getName()).append("**\n")
                        .append("Price: $").append(item.getPrice()).append("\n")
                        .append("Stock: ").append(item.getQuantity()).append("\n\n");
            }
            embed.setDescription(description.toString());
        }

        return embed;
    }
}
