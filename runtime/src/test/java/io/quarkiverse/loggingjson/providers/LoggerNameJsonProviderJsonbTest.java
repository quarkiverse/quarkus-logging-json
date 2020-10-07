package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;

public class LoggerNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
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
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("logger");
        config.enabled = Optional.of(false);
        final LoggerNameJsonProvider provider = new LoggerNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setLoggerName("LoggerNameJsonProviderTest");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String logger = result.findValue("logger").asText();
        Assertions.assertNotNull(logger);
        Assertions.assertFalse(logger.isEmpty());
        Assertions.assertEquals("LoggerNameJsonProviderTest", logger);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new LoggerNameJsonProvider(config).isEnabled());
    }
}
