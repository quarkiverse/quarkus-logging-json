package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpLogLevelJsonProvider;

public class GcpLogLevelJsonProviderTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        final GcpLogLevelJsonProvider provider = new GcpLogLevelJsonProvider(config);
        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        final JsonNode result = getResultAsJsonNode(provider, event);
        String level = result.findValue(GcpLogLevelJsonProvider.SEVERITY_ATTRIBUTE).asText();
        Assertions.assertEquals(level, "DEFAULT");
    }
}
