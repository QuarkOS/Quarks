package org.example.Commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import io.github.freya022.botcommands.api.components.Buttons;
import io.github.freya022.botcommands.api.components.annotations.JDAButtonListener;
import io.github.freya022.botcommands.api.components.event.ButtonEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.example.Commands.AutoCompleters.BundleAutoComplete;
import org.example.Shop.Bundle;
import org.example.Shop.Item;
import org.example.Shop.ItemService;
import org.example.Shop.Shop;

import java.awt.Color;
import java.util.*;
import java.util.logging.Logger;

@Command
public class BundleCommand extends ApplicationCommand {

    private static final Logger LOGGER = Logger.getLogger(BundleCommand.class.getName());
    private final Shop shop;
    private Bundle todayBundle;
    private final Buttons buttons;
    private static final String SHOW_BUNDLES = "SHOW_BUNDLES";
    private static final String VIEW_TODAY = "VIEW_TODAY";
    private final ItemService itemService;

    public BundleCommand(Shop shop, Buttons buttons, ItemService itemService) {
        this.shop = shop;
        this.buttons = buttons;
        this.itemService = itemService;
    }

    @TopLevelSlashCommandData
    @JDASlashCommand(name = "bundle", subcommand = "help", description = "Bundle related commands")
    public void onBundleCommand(GuildSlashEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("\uD83C\uDFEA **Bundle Commands** \uD83C\uDFEA");
        embed.setDescription("**Explore the available bundle commands below:**\n\n");
        embed.addField("**/bundle create**", "Create a new bundle.", false);
        embed.addField("**/bundle add-item**", "Add an item to a bundle.", false);
        embed.addField("**/bundle remove-item**", "Remove an item from a bundle.", false);
        embed.addField("**/bundle view-today**", "View today's bundle.", false);
        embed.addField("**/bundle list**", "List all bundles.", false);
        embed.addField("**/bundle set-today**", "Set today's bundle.", false);
        embed.setColor(new Color(0x00BFFF));
        embed.setThumbnail("https://cdn.discordapp.com/attachments/1330554717155364916/1330554920214200321/9fd7eedeab0ec7f829ef2af6328ab6a9.png?ex=678e6755&is=678d15d5&hm=ec4ae3b6062e94e3ecd6a132dfd9bff045c62458e33a6a4802d10fb7366a6b27&");

        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "bundle", subcommand = "create", description = "Create a new bundle")
    public void createBundle(GuildSlashEvent event, @SlashOption(name = "name") String bundleName) {
        Bundle bundle = new Bundle(bundleName);
        shop.addBundle(bundle);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDCC8 **Create Bundle** \uD83D\uDCC8");
        embed.setDescription("A new bundle named **" + bundleName + "** has been created.");
        embed.setColor(Color.GREEN);
        event.replyEmbeds(embed.build()).queue();
    }

    @JDASlashCommand(name = "bundle", subcommand = "add-item", description = "Add an item to a bundle")
    public synchronized void addBundleItem(GuildSlashEvent event, @SlashOption(name = "bundle", autocomplete = BundleAutoComplete.BUNDLE_AUTOCOMPLETE_NAME) String bundleName, @SlashOption(name = "item", autocomplete = BundleAutoComplete.ITEM_AUTOCOMPLETE_NAME) String itemName, @SlashOption(name = "quantity") int quantity) {
        Optional<Bundle> bundle = shop.findBundleByName(bundleName);
        if (bundle.isPresent()) {
            Optional<Item> item = shop.findItemByName(itemName);
            if (item.isPresent()) {
                bundle.get().addItem(item.get(), quantity);
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("\uD83D\uDED2 **Add Item to Bundle** \uD83D\uDED2");
                embed.setDescription("Item **" + itemName + "** has been added to the bundle **" + bundleName + "**.");
                embed.setColor(Color.GREEN);
                event.replyEmbeds(embed.build()).queue();
            } else {
                event.reply("Item **" + itemName + "** not found.").queue();
            }
        } else {
            event.reply("Bundle **" + bundleName + "** not found.").queue();
        }
    }

    @JDASlashCommand(name = "bundle", subcommand = "remove-item", description = "Remove an item from a bundle")
    public synchronized void removeBundleItem(GuildSlashEvent event, @SlashOption(name = "bundle", autocomplete = BundleAutoComplete.BUNDLE_AUTOCOMPLETE_NAME) String bundleName, @SlashOption(name = "item", autocomplete = BundleAutoComplete.ITEM_AUTOCOMPLETE_NAME) String itemName) {
        Optional<Bundle> bundle = shop.findBundleByName(bundleName);
        if (bundle.isPresent()) {
            Optional<Item> item = shop.findItemByName(itemName);
            if (item.isPresent()) {
                bundle.get().removeItem(item.get());
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("\uD83D\uDED2 **Remove Item from Bundle** \uD83D\uDED2");
                embed.setDescription("Item **" + itemName + "** has been removed from the bundle **" + bundleName + "**.");
                embed.setColor(Color.RED);
                event.replyEmbeds(embed.build()).queue();
            } else {
                event.reply("Item **" + itemName + "** not found.").queue();
            }
        } else {
            event.reply("Bundle **" + bundleName + "** not found.").queue();
        }
    }

    @JDASlashCommand(name = "bundle", subcommand = "view-today", description = "View today's bundle")
    public void viewTodayBundle(GuildSlashEvent event) {
        EmbedBuilder embed = getTodayBundleEmbed();
        event.replyEmbeds(embed.build())
                .addActionRow(
                        buttons.primary("View All Bundles", Emoji.fromUnicode("U+1F4E6")).persistent().bindTo(SHOW_BUNDLES).build(),
                        buttons.primary("View Today's Bundle", Emoji.fromUnicode("U+1F4C5")).persistent().bindTo(VIEW_TODAY).build()
                ).queue();
    }

    @JDAButtonListener(SHOW_BUNDLES)
    public void showAllBundles(ButtonEvent event) {
        EmbedBuilder embed = getBundlesEmbed();
        event.editMessageEmbeds(embed.build()).queue();
    }

    @JDAButtonListener(VIEW_TODAY)
    public void showTodayBundle(ButtonEvent event) {
        EmbedBuilder embed = getTodayBundleEmbed();
        event.editMessageEmbeds(embed.build()).queue();
    }

    private EmbedBuilder getTodayBundleEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83C\uDFE0 **Today's Bundle** \uD83C\uDFE0");
        embed.setColor(Color.CYAN);

        if (todayBundle == null) {
            embed.setDescription("There is no bundle today.");
        } else {
            StringBuilder description = new StringBuilder("Items in today's bundle:\n\n");
            for (Map.Entry<Integer, Integer> entry : todayBundle.getItems().entrySet()) {
                int itemId = entry.getKey();
                Item item = itemService.getItemById(itemId).orElseThrow();
                description.append("**")
                        .append(item.getName())
                        .append("** - Quantity: ")
                        .append(entry.getValue())
                        .append("\n");
            }
            embed.setDescription(description.toString());
        }

        return embed;
    }

    private EmbedBuilder getBundlesEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDCB8 **Available Bundles** \uD83D\uDCB8");
        embed.setColor(Color.CYAN);

        if (shop.getBundles().isEmpty()) {
            embed.setDescription("No bundles available.");
        } else {
            StringBuilder description = new StringBuilder("List of all bundles:\n\n");
            for (Bundle bundle : shop.getBundles()) {
                description.append("**").append(bundle.getName()).append("** - ").append(bundle.getItems().size()).append(" items\n");
                for (Map.Entry<Integer, Integer> entry : bundle.getItems().entrySet()) {
                    int itemId = entry.getKey();
                    Item item = itemService.getItemById(itemId).orElseThrow();
                    description
                            .append(" - **")
                            .append(item.getName())
                            .append("** x")
                            .append(entry.getValue())
                            .append("\n");
                }
                description.append("\n");
            }
            embed.setDescription(description.toString());
        }

        return embed;
    }

    @JDASlashCommand(name = "bundle", subcommand = "set-today", description = "Set today's bundle")
    public void setTodayBundle(GuildSlashEvent event, @SlashOption(name = "bundle", autocomplete = BundleAutoComplete.BUNDLE_AUTOCOMPLETE_NAME) String bundleName) {
        Optional<Bundle> bundle = shop.findBundleByName(bundleName);
        if (bundle.isPresent()) {
            todayBundle = bundle.get();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("\uD83C\uDFE0 **Set Today's Bundle** \uD83C\uDFE0");
            embed.setDescription("Today's bundle has been set to **" + bundleName + "**.");
            embed.setColor(Color.GREEN);
            event.replyEmbeds(embed.build()).queue();
        } else {
            event.reply("Bundle **" + bundleName + "** not found.").queue();
        }
    }

    @JDASlashCommand(name = "bundle", subcommand = "list", description = "List all bundles")
    public void listBundles(GuildSlashEvent event) {
        EmbedBuilder embed = getBundlesEmbed();
        event.replyEmbeds(embed.build()).queue();
    }
}
