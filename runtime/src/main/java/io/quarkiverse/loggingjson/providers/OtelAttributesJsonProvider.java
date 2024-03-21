package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;
import java.util.Map;

/**
 * Copy the MDC context into the Attributes section of the Otel Log Data Model
 *
 * Also for OTel check to see if we have traceId and spanId and if we
 * do then promote them to the top level also
 *
 */
public class OtelAttributesJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;
    private final Config.MDCConfig config;

    public OtelAttributesJsonProvider(Config.MDCConfig config) {
        this.config = config;
        if (config.flatFields) {
            this.fieldName = null;
        } else {
            this.fieldName = config.fieldName.orElse("attributes");
        }
    }

    /**
     * Write the MDC context to the json log output under the Attributes section
     * Also promote traceId and spanId to the top level if they are present
     * @param generator Used to add data to the json log output.
     * @param event The log event to handle.
     * @throws IOException
     */
    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        Map<String, String> mdcCopy = event.getMdcCopy();
        if ( mdcCopy.containsKey("traceId") ) {
            JsonWritingUtils.writeStringField(generator, "trace_id", mdcCopy.get("traceId"));
            mdcCopy.remove("traceId");
        }
        if ( mdcCopy.containsKey("spanId") ) {
            JsonWritingUtils.writeStringField(generator, "span_id", mdcCopy.get("spanId"));
            mdcCopy.remove("spanId");
        }
        JsonWritingUtils.writeMapStringFields(generator, fieldName, event.getMdcCopy());
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }
}
