package org.example.Commands.AutoCompleters;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.example.Shop.Item;
import org.example.Shop.Shop;

import java.util.List;
import java.util.stream.Collectors;

@Handler
public class ProductAutoComplete extends ApplicationCommand {

    public ProductAutoComplete(Shop shop) {
        items = ItemListToStringList(shop.getShopInventory());
    }

    private final List<String> items;

    public static final String PRODUCT_AUTOCOMPLETE_NAME = "ProductAutocomplete:product";

    @AutocompleteHandler(PRODUCT_AUTOCOMPLETE_NAME)
    public List<String> onProductAutocomplete(CommandAutoCompleteInteractionEvent event) {
        String userInput = event.getFocusedOption().getValue();
        return items.stream()
                .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }

    private static final List<String> ItemListToStringList(List<Item> items) {
        return items.stream()
                .map(Item::getName)
                .collect(Collectors.toList());
    }
}
