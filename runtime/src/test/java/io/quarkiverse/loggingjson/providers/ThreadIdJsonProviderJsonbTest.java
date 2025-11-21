package io.quarkiverse.loggingjson.providers;

import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class ThreadIdJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(null, null);
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
        Config.FieldConfig config = fieldConfig("tid", false);
        final ThreadIdJsonProvider provider = new ThreadIdJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThreadID(3249);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long tid = result.findValue("tid").asLong();
        Assertions.assertEquals(3249, tid);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig("tid", true);
        Assertions.assertTrue(new ThreadIdJsonProvider(config).isEnabled());
    }
}
