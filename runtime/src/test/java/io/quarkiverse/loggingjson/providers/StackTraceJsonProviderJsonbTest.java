package io.quarkiverse.loggingjson.providers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.config.Config;

public class StackTraceJsonProviderJsonbTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = fieldConfig(null, null);
        final StackTraceJsonProvider provider = new StackTraceJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing stackTrace");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String stackTrace = result.findValue("stackTrace").asText();
        Assertions.assertNotNull(stackTrace);
        Assertions.assertFalse(stackTrace.isEmpty());

        final StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));

        Assertions.assertEquals(out.toString(), stackTrace);
        Assertions.assertTrue(provider.isEnabled());
    }

    @Test
    void testCustomConfig() throws Exception {
        Config.FieldConfig config = fieldConfig("st", false);
        final StackTraceJsonProvider provider = new StackTraceJsonProvider(config);

        final RuntimeException t = new RuntimeException("Testing stackTrace");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);

        String st = result.findValue("st").asText();
        Assertions.assertNotNull(st);
        Assertions.assertFalse(st.isEmpty());

        final StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));

        Assertions.assertEquals(out.toString(), st);
        Assertions.assertFalse(provider.isEnabled());

        config = fieldConfig("st", true);
        Assertions.assertTrue(new StackTraceJsonProvider(config).isEnabled());
    }
}
