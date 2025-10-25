package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ThreadNameJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(Optional.empty(), Optional.empty());
        final ThreadNameJsonProvider provider = new ThreadNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThreadName("TestThread");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String threadName = result.findValue("threadName").asText();
        Assertions.assertNotNull(threadName);
        Assertions.assertFalse(threadName.isEmpty());
        Assertions.assertEquals("TestThread", threadName);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig(Optional.of("tn"), Optional.of(false));
        final ThreadNameJsonProvider provider = new ThreadNameJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThreadName("TestThread");
        final JsonNode result = getResultAsJsonNode(provider, event);

        String tn = result.findValue("tn").asText();
        Assertions.assertNotNull(tn);
        Assertions.assertFalse(tn.isEmpty());
        Assertions.assertEquals("TestThread", tn);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig(Optional.of("tn"), Optional.of(true));
        Assertions.assertTrue(new ThreadNameJsonProvider(config).isEnabled());
    }
}
