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
public class PaymentAutoComplete extends ApplicationCommand {

    public static final String PAYMENT_AUTOCOMPLETE_NAME = "PaymentAutocomplete:PaymentMethod";

    String[] paymentMethods = {"Bank", "Bar"};

    @AutocompleteHandler(PAYMENT_AUTOCOMPLETE_NAME)
    public List<String> onBundleAutocomplete(CommandAutoCompleteInteractionEvent event) {
        String userInput = event.getFocusedOption().getValue();
        return List.of(paymentMethods).stream()
                .filter(name -> name.toLowerCase().contains(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }
}
