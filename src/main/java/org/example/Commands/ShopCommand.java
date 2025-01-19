package org.example.Commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import net.dv8tion.jda.api.EmbedBuilder;
import org.example.Commands.AutoCompleters.ProductAutoComplete;
import org.example.Shop.*;

import java.awt.Color;
import java.util.List;

@Command
public class ShopCommand extends ApplicationCommand {

    private final Shop shop;

    public ShopCommand(Shop shop) {
        this.shop = shop;
    }

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "shop", subcommand = "help", description = "Shop related commands")
    public void onShopCommand(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        // Set the title with emojis
        embed.setTitle("\uD83C\uDFEA **Shop Commands** \uD83C\uDFEA");
        embed.setDescription("**Explore the available shop commands below:**\n\n");

        // Add fields with more refined formatting and emojis
        embed.addField("**/shop viewitems**", "View available items in the shop.", false);
        embed.addField("**/shop additem [item name]**", "Add an item to the cart.", false);
        embed.addField("**/shop viewcart**", "View items in the cart.", false);
        embed.addField("**/shop checkout**", "Confirm the checkout.", false);
        embed.addField("**/shop cancel**", "Cancel the checkout.", false);
        embed.addField("**/shop save**", "Save shop data.", false);

        // Customize colors for a more dynamic look
        embed.setColor(new Color(0x00BFFF)); // Use a refreshing blue color

        // Add a thumbnail to represent the shop
        embed.setThumbnail("https://cdn.discordapp.com/attachments/1330554717155364916/1330554920214200321/9fd7eedeab0ec7f829ef2af6328ab6a9.png?ex=678e6755&is=678d15d5&hm=ec4ae3b6062e94e3ecd6a132dfd9bff045c62458e33a6a4802d10fb7366a6b27&");

        event.replyEmbeds(embed.build()).queue();
    }



    @JDASlashCommand(name = "shop", subcommand = "viewitems", description = "View available items in the shop")
    public void onViewItems(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Shop Items");
        embed.setColor(Color.BLUE);
        for (Item item : shop.getShopInventory()) {
            embed.addField(item.getName(), "Price: $" + item.getPrice() + "\nStock: " + item.getQuantity(), false);
        }
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "additem", description = "Add an item to the cart")
    public void onAddItem(GuildSlashEvent event, @SlashOption (name = "product", description = "Product", autocomplete = ProductAutoComplete.PRODUCT_AUTOCOMPLETE_NAME) String itemName) {
        boolean success = shop.addItem(itemName);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Add Item to Cart");
        if (success) {
            embed.setDescription(itemName + " has been added to your cart.");
            embed.setColor(Color.GREEN);
        } else {
            embed.setDescription("Item not found or out of stock: " + itemName);
            embed.setColor(Color.RED);
        }
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "viewcart", description = "View items in the cart")
    public void onViewCart(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Cart Items");
        embed.setColor(Color.ORANGE);
        for (Item item : shop.getCart().getItems()) {
            embed.addField(item.getName(), "Price: $" + item.getPrice() + "\nQuantity: " + item.getQuantity(), false);
        }
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "checkout", description = "Confirm the checkout")
    public void onCheckout(GuildSlashEvent event) {
        double total = shop.confirmCheckout();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Checkout");
        embed.setDescription("Your total is $" + total + ". Thank you for shopping with us!");
        embed.setColor(Color.GREEN);
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "cancel", description = "Cancel the checkout")
    public void onCancelCheckout(GuildSlashEvent event) {
        shop.cancelCheckout();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Cancel Checkout");
        embed.setDescription("Your checkout has been canceled.");
        embed.setColor(Color.RED);
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "shop", subcommand = "save", description = "Save shop data")
    public void onSaveShopData(GuildSlashEvent event) {
        shop.saveAsJSON();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Save Shop Data");
        embed.setDescription("Shop data has been saved.");
        embed.setColor(Color.GREEN);
        event.replyEmbeds(embed.build()).queue();
    }
}
