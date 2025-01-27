package org.example.Commands.AutoCompleters;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.example.Shop.Item;
import org.example.Shop.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@Handler
public class ProductAutoComplete extends ApplicationCommand {

    private final ItemService service;
    public static final String PRODUCT_AUTOCOMPLETE_NAME = "ProductAutocomplete:product";

    public ProductAutoComplete(ItemService service) {
        this.service = service;
    }

    @AutocompleteHandler(PRODUCT_AUTOCOMPLETE_NAME)
    public List<String> onProductAutocomplete(CommandAutoCompleteInteractionEvent event) {
        String userInput = event.getFocusedOption().getValue();
        return service.getItems().stream()
                .map(Item::getName)
                .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }
}
