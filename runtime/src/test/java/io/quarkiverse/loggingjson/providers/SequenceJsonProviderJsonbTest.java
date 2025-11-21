package io.quarkiverse.loggingjson.providers;

import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class SequenceJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(null, null);
        final SequenceJsonProvider provider = new SequenceJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSequenceNumber(4356);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long sequence = result.findValue("sequence").asLong();
        Assertions.assertEquals(4356, sequence);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig("seq", false);
        final SequenceJsonProvider provider = new SequenceJsonProvider(config);

        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setSequenceNumber(4356);
        final JsonNode result = getResultAsJsonNode(provider, event);

        long seq = result.findValue("seq").asLong();
        Assertions.assertEquals(4356, seq);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig("seq", true);
        Assertions.assertTrue(new SequenceJsonProvider(config).isEnabled());
    }
}
