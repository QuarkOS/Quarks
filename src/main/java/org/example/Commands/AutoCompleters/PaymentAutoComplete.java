package org.example.Commands.AutoCompleters;

import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.autocomplete.annotations.AutocompleteHandler;
import io.github.freya022.botcommands.api.core.annotations.Handler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.example.Shop.PaymentMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Handler
public class PaymentAutoComplete extends ApplicationCommand {
    public static final String PAYMENT_AUTOCOMPLETE_NAME = "PaymentAutocomplete:payment-method";

    @AutocompleteHandler(PAYMENT_AUTOCOMPLETE_NAME)
    public List<Command.Choice> onPaymentAutocomplete(CommandAutoCompleteInteractionEvent event) {
        final String userInput = event.getFocusedOption().getValue().toLowerCase();

        return Stream.of(PaymentMethod.values())
                .filter(pm -> pm.getDisplayName().toLowerCase().contains(userInput))
                .map(pm -> new Command.Choice(pm.getDisplayName(), pm.name())) // Show "Bar" but send "BAR"
                .collect(Collectors.toList());
    }
}