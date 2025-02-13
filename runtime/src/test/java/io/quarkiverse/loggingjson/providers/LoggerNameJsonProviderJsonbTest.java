package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class LoggerNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.empty();
            }
        };
        final LoggerNameJsonProvider provider = new LoggerNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setLoggerName("LoggerNameJsonProviderTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String loggerName = result.findValue("loggerName").asText();
        Assertions.assertNotNull(loggerName);
        Assertions.assertFalse(loggerName.isEmpty());
        Assertions.assertEquals("LoggerNameJsonProviderTest", loggerName);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.of("logger");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(false);
            }
        };
        final LoggerNameJsonProvider provider = new LoggerNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setLoggerName("LoggerNameJsonProviderTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String logger = result.findValue("logger").asText();
        Assertions.assertNotNull(logger);
        Assertions.assertFalse(logger.isEmpty());
        Assertions.assertEquals("LoggerNameJsonProviderTest", logger);
        Assertions.assertFalse(provider.isEnabled());

        config = new Config.FieldConfig() {

            @Override
            public Optional<String> fieldName() {
                return Optional.of("logger");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(true);
            }
        };
        Assertions.assertTrue(new LoggerNameJsonProvider(config).isEnabled());
    }
}
