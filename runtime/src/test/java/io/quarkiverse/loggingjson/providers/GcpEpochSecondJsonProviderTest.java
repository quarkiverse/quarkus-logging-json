package io.quarkiverse.loggingjson.providers;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpEpochSecondJsonProvider;

public class GcpEpochSecondJsonProviderTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        final GcpEpochSecondJsonProvider timestampJsonProvider = new GcpEpochSecondJsonProvider(config);
        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(timestampJsonProvider, event);
        long timestamp = result.findValue(GcpEpochSecondJsonProvider.TIMESTAMP_SECONDS_ATTRIBUTE).asLong();
        Assertions.assertEquals(timestamp, Instant.ofEpochMilli(event.getMillis()).getEpochSecond());
    }
}
