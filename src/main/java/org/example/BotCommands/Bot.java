package org.example.BotCommands;

import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.events.BReadyEvent;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@BService
public class Bot extends JDAService {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);
    private final Config config;

    public Bot(Config config) {
        this.config = config;
    }

    // If you use Spring, you can return values provided by JDAConfiguration in the getters below
    @NotNull
    @Override
    public Set<CacheFlag> getCacheFlags() {
        return Set.of(/* _Additional_ cache flags */);
    }

    @NotNull
    @Override
    public Set<GatewayIntent> getIntents() {
        return defaultIntents(/* _Additional_ intents */);
    }

    @Override
    public void createJDA(@NotNull BReadyEvent event, @NotNull IEventManager eventManager) {
        // This uses JDABuilder#createLight, with the intents and the additional cache flags set above
        // It also sets the EventManager and a special rate limiter
        log.info("Creating JDA instance...");
        createLight(config.getToken())
                .setActivity(Activity.customStatus("In Java with ❤️"))
                .build();
    }
}