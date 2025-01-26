package org.example.Commands;

import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.TopLevelSlashCommandData;
import org.example.Shop.Bundle;
import org.example.Shop.Item;

import java.util.List;
import java.util.Map;

@Command
public class BundleCommand extends ApplicationCommand {

    List<Bundle> bundles;
    Bundle todayBundle;

    @JDASlashCommand(name = "bundle", subcommand = "create", description = "Create a bundle")
    public void createBundle(Map<Item, Integer> items) {
        Bundle bundle = new Bundle(items);
        bundles.add(bundle);
    }

    @JDASlashCommand(name = "bundle", subcommand = "add", description = "Add an item to a bundle")
    public void addBundleItem(Bundle bundle, Item item, int quantity) {
        bundle.addItem(item, quantity);
    }

    @JDASlashCommand(name = "bundle", subcommand = "remove", description = "Remove an item from a bundle")
    public void removeBundleItem(Bundle bundle, Item item) {
        bundle.removeItem(item);
    }

    @JDASlashCommand(name = "bundle", subcommand = "today", description = "View today's bundle")
    public void viewTodayBundle() {
        if (todayBundle == null) {
            System.out.println("There is no bundle today.");
        } else {
            System.out.println("Today's bundle:");
            for (Map.Entry<Item, Integer> entry : todayBundle.getItems().entrySet()) {
                System.out.println(entry.getKey() + " x" + entry.getValue());
            }
        }
    }

    @JDASlashCommand(name = "bundle", subcommand = "list", description = "List all bundles")
    public void listBundles() {
        System.out.println("Available bundles:");
        for (Bundle bundle : bundles) {
            System.out.println(bundle);
        }
    }

    @JDASlashCommand(name = "bundle", subcommand = "settoday", description = "Set today's bundle")
    public void setTodayBundle(Bundle bundle) {
        todayBundle = bundle;
    }

}
