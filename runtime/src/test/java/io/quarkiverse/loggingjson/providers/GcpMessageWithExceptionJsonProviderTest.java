package io.quarkiverse.loggingjson.providers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.logging.Level;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkiverse.loggingjson.Config;
import io.quarkiverse.loggingjson.providers.gcp.GcpMessageWithExceptionJsonProvider;

public class GcpMessageWithExceptionJsonProviderTest extends JsonProviderBaseTest {
    @Override
    protected Type type() {
        return Type.JSONB;
    }

    @Test
    void testDefaultConfig() throws Exception {
        final Config.FieldConfig config = new Config.FieldConfig();
        config.fieldName = Optional.empty();
        final GcpMessageWithExceptionJsonProvider provider = new GcpMessageWithExceptionJsonProvider(config);
        final RuntimeException t = new RuntimeException("Testing stackTrace");
        final ExtLogRecord event = new ExtLogRecord(Level.ALL, "", "");
        event.setThrown(t);
        final JsonNode result = getResultAsJsonNode(provider, event);
        String message = result.findValue("message").asText();
        final StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));
        Assertions.assertTrue(message.indexOf(t.toString()) > 0);
    }
}
