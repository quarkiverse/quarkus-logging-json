package io.quarkiverse.loggingjson.providers.gcp;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class GcpTraceIdJsonProvider implements JsonProvider, Enabled {
    public static final String TRACE_ID_ATTRIBUTE = "logging.googleapis.com/trace";
    private final Config.GcpTraceIdConfig config;
    private final String projectId;
    private final String mdcKey;
    private final String fieldName;

    public GcpTraceIdJsonProvider(Config.GcpTraceIdConfig config) {
        this(config, TRACE_ID_ATTRIBUTE, "default", "x-b3-traceid");
    }

    public GcpTraceIdJsonProvider(Config.GcpTraceIdConfig config, String defaultName, String defaultProjectId,
            String defaultMdcKey) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
        this.mdcKey = config.mdcKey.orElse(defaultMdcKey);
        this.projectId = config.projectId.orElse(defaultProjectId);
    }

    protected String formatTraceId(final String traceId) {
        // Trace IDs are either 64-bit or 128-bit, which is 16-digit hex, or 32-digit hex.
        // If traceId is 64-bit (16-digit hex), then we need to prepend 0's to make a 32-digit hex.
        if (traceId != null && traceId.length() == 16) {
            return "0000000000000000" + traceId;
        }
        return traceId;
    }

    String composeFullTraceName(String traceId) {
        return "projects/" + projectId + "/traces/" + traceId;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        String traceId = event.getMdc(mdcKey);
        if (traceId != null) {
            JsonWritingUtils.writeStringField(generator, fieldName, composeFullTraceName(formatTraceId(traceId)));
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }

}
