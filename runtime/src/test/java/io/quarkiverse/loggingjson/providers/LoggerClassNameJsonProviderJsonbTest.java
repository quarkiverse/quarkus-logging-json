package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class LoggerClassNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final LoggerClassNameJsonProvider provider = new LoggerClassNameJsonProvider(config);

        final JsonNode result = getResultAsJsonNode(provider,
                new ExtLogRecord(Level.ALL, "", "LoggerClassNameJsonProviderTest"));

        String loggerClassName = result.findValue("loggerClassName").asText();
        Assertions.assertNotNull(loggerClassName);
        Assertions.assertFalse(loggerClassName.isEmpty());
        Assertions.assertEquals("LoggerClassNameJsonProviderTest", loggerClassName);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("loggerClass"), Optional.of(false));
        final LoggerClassNameJsonProvider provider = new LoggerClassNameJsonProvider(config);

        final JsonNode result = getResultAsJsonNode(provider,
                new ExtLogRecord(Level.ALL, "", "LoggerClassNameJsonProviderTest"));

        String loggerClass = result.findValue("loggerClass").asText();
        Assertions.assertNotNull(loggerClass);
        Assertions.assertFalse(loggerClass.isEmpty());
        Assertions.assertEquals("LoggerClassNameJsonProviderTest", loggerClass);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("loggerClass"), Optional.of(true));
        Assertions.assertTrue(new LoggerClassNameJsonProvider(config).isEnabled());
    }
}
