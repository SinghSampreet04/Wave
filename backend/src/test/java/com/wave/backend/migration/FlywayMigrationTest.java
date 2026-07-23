package com.wave.backend.migration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Test
    void cleanPostgresMigratesAndContainsReleaseTables() throws Exception {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        POSTGRES.getJdbcUrl(),
                        POSTGRES.getUsername(),
                        POSTGRES.getPassword()
                )
                .locations("classpath:db/migration")
                .load();

        assertThat(flyway.migrate().success).isTrue();

        try (Connection connection = POSTGRES.createConnection("");
             ResultSet result = connection.createStatement().executeQuery("""
                     SELECT
                         to_regclass('public.refresh_sessions') IS NOT NULL,
                         EXISTS (
                             SELECT 1
                             FROM information_schema.columns
                             WHERE table_name = 'users'
                             AND column_name = 'presence_preference'
                         )
                     """)) {
            assertThat(result.next()).isTrue();
            assertThat(result.getBoolean(1)).isTrue();
            assertThat(result.getBoolean(2)).isTrue();
        }
    }
}
