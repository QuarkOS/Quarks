package org.example.Resolvers;

import io.github.freya022.botcommands.api.commands.application.slash.options.SlashCommandOption;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.example.Shop.PaymentMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

@Resolver
public class PaymentMethodResolver extends ClassParameterResolver<PaymentMethodResolver, PaymentMethod>
        implements SlashParameterResolver<PaymentMethodResolver, PaymentMethod> {

    public PaymentMethodResolver() {
        super(PaymentMethod.class);
    }

    @NotNull
    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @NotNull
    @Override
    public Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
        return Arrays.stream(PaymentMethod.values())
                .map(pm -> new Command.Choice(pm.getDisplayName(), pm.name()))
                .toList();
    }

    @Nullable
    @Override
    public PaymentMethod resolve(@NotNull SlashCommandOption option,
                                 @NotNull CommandInteractionPayload event,
                                 @NotNull OptionMapping optionMapping) {
        String value = optionMapping.getAsString();
        return PaymentMethod.valueOf(value); // Convert String to enum
    }
}