package org.example;

import io.github.freya022.botcommands.api.commands.application.slash.options.SlashCommandOption;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Resolver
public class TimeUnitParameterResolver extends ClassParameterResolver<TimeUnitParameterResolver, TimeUnit> implements SlashParameterResolver<TimeUnitParameterResolver, TimeUnit> {

    public TimeUnitParameterResolver() {
        super(TimeUnit.class);
    }

    @NotNull
    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @NotNull
    @Override
    public Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
        Command.Choice[] choices = new Command.Choice[TimeUnit.values().length];
        for (int i = 0; i < TimeUnit.values().length; i++) {
            TimeUnit unit = TimeUnit.values()[i];
            choices[i] = new Command.Choice(unit.name(), unit.name());
        }
        return Set.of(choices);
    }

    @Nullable
    @Override
    public TimeUnit resolve(@NotNull SlashCommandOption option, @NotNull CommandInteractionPayload event, @NotNull OptionMapping optionMapping) {
        return TimeUnit.valueOf(optionMapping.getAsString());
    }
}