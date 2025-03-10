package org.example;

import io.github.freya022.botcommands.api.commands.CommandPath;
import io.github.freya022.botcommands.api.commands.annotations.Command;
import io.github.freya022.botcommands.api.commands.application.ApplicationCommand;
import io.github.freya022.botcommands.api.commands.application.slash.GuildSlashEvent;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.JDASlashCommand;
import io.github.freya022.botcommands.api.commands.application.slash.annotations.SlashOption;
import io.github.freya022.botcommands.api.parameters.Resolvers;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Command
public class SlashConvert extends ApplicationCommand {
    @NotNull
    @Override
    public List<net.dv8tion.jda.api.interactions.commands.Command.Choice> getOptionChoices(@Nullable Guild guild, @NotNull CommandPath commandPath, @NotNull String optionName) {
        if (commandPath.getName().equals("convert")) {
            if (optionName.equals("from") || optionName.equals("to")) {
                return Stream.of(TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS)
                        // The Resolvers class helps us by providing resolvers for any enum type.
                        // We're just using the helper method to change an enum value to a more natural name.
                        .map(u -> new net.dv8tion.jda.api.interactions.commands.Command.Choice(Resolvers.toHumanName(u), u.name()))
                        .toList();
            }
        }

        return super.getOptionChoices(guild, commandPath, optionName);
    }

    @JDASlashCommand(name = "convert", description = "Convert time to another unit")
    public void onSlashConvert(
            GuildSlashEvent event,
            @SlashOption(description = "The time to convert") long time,
            @SlashOption(description = "The unit to convert from",usePredefinedChoices = true) TimeUnit from,
            @SlashOption(description = "The unit to convert to",usePredefinedChoices = true) TimeUnit to
    ) {
        event.reply(to.convert(time, from) + " " + to.name().toLowerCase()).queue();
    }
}