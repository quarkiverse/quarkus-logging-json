package io.quarkiverse.loggingjson.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpSpanIdJsonProvider;

public class GcpSpanIdJsonProviderTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.GcpSpanIdConfig config = new Config.GcpSpanIdConfig();
        config.fieldName = Optional.empty();
        config.mdcKey = Optional.empty();
        final GcpSpanIdJsonProvider provider = new GcpSpanIdJsonProvider(config);
        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map mdc = new HashMap();
        mdc.put("x-b3-spanid", "261dcf2aa53fd2b8");
        event.setMdc(mdc);
        final JsonNode result = getResultAsJsonNode(provider, event);
        String spanId = result.findValue(GcpSpanIdJsonProvider.SPAN_ID_ATTRIBUTE).asText();
        Assertions.assertEquals(spanId, "261dcf2aa53fd2b8");
    }
}
