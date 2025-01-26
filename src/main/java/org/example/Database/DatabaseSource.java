package org.example.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.freya022.botcommands.api.core.config.BComponentsConfig;
import io.github.freya022.botcommands.api.core.db.HikariSourceSupplier;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import org.example.BotCommands.Config;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@BService
public class DatabaseSource implements HikariSourceSupplier {
    private final HikariDataSource source;

    public DatabaseSource(Config config) {
        final var hikariConfig = new HikariConfig();
        // Suppose those are your config values
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5435/mydb");
        hikariConfig.setUsername("admin");
        hikariConfig.setPassword("admin");

        // At most 2 JDBC connections, the database will block if all connections are used
        hikariConfig.setMaximumPoolSize(2);

        // Emits a warning and does a thread/coroutine dump after the duration (in milliseconds)
        hikariConfig.setLeakDetectionThreshold(10000);

        source = new HikariDataSource(hikariConfig);

        // Flyway migration
        Flyway.configure()
                .dataSource(source)
                .schemas("bc")
                .locations("bc_database_scripts")
                .validateMigrationNaming(true)
                .loggers("slf4j")
                .load()
                .migrate();
    }

    @NotNull
    @Override
    public HikariDataSource getSource() {
        return source;
    }
}