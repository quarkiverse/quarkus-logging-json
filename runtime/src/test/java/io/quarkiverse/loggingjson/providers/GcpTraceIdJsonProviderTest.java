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
import io.quarkiverse.loggingjson.providers.gcp.GcpTraceIdJsonProvider;

public class GcpTraceIdJsonProviderTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.GcpTraceIdConfig config = new Config.GcpTraceIdConfig();
        config.fieldName = Optional.empty();
        config.mdcKey = Optional.empty();
        config.projectId = Optional.empty();
        final GcpTraceIdJsonProvider provider = new GcpTraceIdJsonProvider(config);
        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map mdc = new HashMap();
        mdc.put("x-b3-traceid", "71c24209290d12caa3a2743bf050bdcc");
        event.setMdc(mdc);
        final JsonNode result = getResultAsJsonNode(provider, event);
        String traceId = result.findValue(GcpTraceIdJsonProvider.TRACE_ID_ATTRIBUTE).asText();
        Assertions.assertEquals(traceId, "projects/default/traces/71c24209290d12caa3a2743bf050bdcc");
    }
}
