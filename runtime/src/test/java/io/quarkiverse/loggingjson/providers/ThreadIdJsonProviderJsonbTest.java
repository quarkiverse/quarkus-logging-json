package io.quarkiverse.loggingjson.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Level;

public class ThreadIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        config.enabled = Optional.empty();
        final ThreadIdJsonProvider provider = new ThreadIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThreadID(3249);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long threadId = result.findValue("threadId").asLong();
        Assertions.assertEquals(3249, threadId);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.of("tid");
        config.enabled = Optional.of(false);
        final ThreadIdJsonProvider provider = new ThreadIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThreadID(3249);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long tid = result.findValue("tid").asLong();
        Assertions.assertEquals(3249, tid);
        Assertions.assertFalse(provider.isEnabled());

        config.enabled = Optional.of(true);
        Assertions.assertTrue(new ThreadIdJsonProvider(config).isEnabled());
    }
}
