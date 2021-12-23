package io.quarkiverse.loggingjson.providers.gcp;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.*;

public class GcpSpanIdJsonProvider implements JsonProvider, Enabled {
    public static final String SPAN_ID_ATTRIBUTE = "logging.googleapis.com/spanId";
    private final Config.GcpSpanIdConfig config;
    private final String fieldName;
    private final String mdcKey;

    public GcpSpanIdJsonProvider(Config.GcpSpanIdConfig config) {
        this(config, SPAN_ID_ATTRIBUTE, "x-b3-spanid");
    }

    public GcpSpanIdJsonProvider(Config.GcpSpanIdConfig config, String defaultName, String defaultMdcKey) {
        this.config = config;
        this.fieldName = config.fieldName.orElse(defaultName);
        this.mdcKey = config.mdcKey.orElse(defaultMdcKey);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {

        String spanId = event.getMdc(mdcKey);
        if (spanId != null) {
            JsonWritingUtils.writeStringField(generator, fieldName, spanId);
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled.orElse(true);
    }

}
