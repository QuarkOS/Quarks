package org.example.BotCommands;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.freya022.botcommands.api.core.service.annotations.BService;

import javax.sql.RowSet;
import java.util.List;

public class Config {
    private static Config INSTANCE;

    private String token = Dotenv.configure().load().get("DISCORD_TOKEN");
    private List<Long> ownerIds = List.of(Long.parseLong(Dotenv.configure().load().get("OWNER_ID")));

    public String getToken() { return token; }
    public List<Long> getOwnerIds() { return ownerIds; }

    @BService // Makes this method a service factory that outputs Config objects
    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public RowSet getDatabaseConfig() {
        return null;
    }
}