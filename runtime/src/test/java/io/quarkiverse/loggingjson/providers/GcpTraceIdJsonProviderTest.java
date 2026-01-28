package io.quarkiverse.loggingjson.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpTraceIdJsonProvider;

public class GcpTraceIdJsonProviderTest extends JsonProviderBaseTest {

    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.GcpTraceIdConfig config = new Config.GcpTraceIdConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.empty();
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.empty();
            }

            @Override
            public Optional<String> projectId() {
                return Optional.empty();
            }

            @Override
            public Optional<String> mdcKey() {
                return Optional.empty();
            }
        };

        final GcpTraceIdJsonProvider provider = new GcpTraceIdJsonProvider(config);

        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map<String, String> mdc = new HashMap<>();
        mdc.put("x-b3-traceid", "71c24209290d12caa3a2743bf050bdcc");
        event.setMdc(mdc);

        final JsonNode result = getResultAsJsonNode(provider, event);
        String traceId = result.findValue(GcpTraceIdJsonProvider.TRACE_ID_ATTRIBUTE).asText();

        Assertions.assertEquals(
                "projects/default/traces/71c24209290d12caa3a2743bf050bdcc",
                traceId);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.GcpTraceIdConfig config = new Config.GcpTraceIdConfig() {
            @Override
            public Optional<String> fieldName() {
                return Optional.of("trace");
            }

            @Override
            public Optional<Boolean> enabled() {
                return Optional.of(true);
            }

            @Override
            public Optional<String> projectId() {
                return Optional.of("my-project");
            }

            @Override
            public Optional<String> mdcKey() {
                return Optional.of("my-trace-id");
            }
        };

        final GcpTraceIdJsonProvider provider = new GcpTraceIdJsonProvider(config);

        ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        Map<String, String> mdc = new HashMap<>();
        mdc.put("my-trace-id", "abcdef1234567890");
        event.setMdc(mdc);

        final JsonNode result = getResultAsJsonNode(provider, event);
        String trace = result.findValue("trace").asText();

        Assertions.assertEquals(
                "projects/my-project/traces/0000000000000000abcdef1234567890",
                trace);
        Assertions.assertTrue(provider.isEnabled());
    }
}
