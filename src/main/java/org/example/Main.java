package org.example;

import io.github.freya022.botcommands.api.core.BotCommands;
import org.example.BotCommands.Config;
import org.flywaydb.core.Flyway;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    public static void main(String[] args) {
        final var config = Config.getInstance();

        BotCommands.create(builder -> {
            builder.addSearchPath("org.example");

            builder.textCommands(textCommands -> {
                textCommands.usePingAsPrefix(true);
            });

            builder.getComponentsConfig().enable(true);
        });
    }
}