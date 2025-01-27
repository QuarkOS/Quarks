package org.example.Commands.AutoCompleters;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.example.Shop.Bundle;
import org.example.Shop.Item;
import org.example.Shop.Shop;

import java.util.List;
import java.util.stream.Collectors;

@Handler
public class BundleAutoComplete extends ApplicationCommand {

    private final Shop shop;

    public BundleAutoComplete(Shop shop) {
        this.shop = shop;
    }

    public static final String BUNDLE_AUTOCOMPLETE_NAME = "BundleAutocomplete:bundle";

    @AutocompleteHandler(BUNDLE_AUTOCOMPLETE_NAME)
    public List<String> onBundleAutocomplete(CommandAutoCompleteInteractionEvent event) {
        String userInput = event.getFocusedOption().getValue();
        return shop.getBundles().stream()
                .map(Bundle::getName)
                .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static final String ITEM_AUTOCOMPLETE_NAME = "BundleItemAutocomplete:item";

    @AutocompleteHandler(ITEM_AUTOCOMPLETE_NAME)
    public List<String> onItemAutocomplete(CommandAutoCompleteInteractionEvent event) {
        String userInput = event.getFocusedOption().getValue();
        return shop.getShopInventory().stream()
                .map(Item::getName)
                .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }
}
