package io.quarkiverse.loggingjson.providers;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkus.runtime.util.StringUtil;

public class SpanIdJsonProvider implements JsonProvider, Enabled {

    private static final String MDC_FIELD_NAME = "spanId";
    private static final String DEFAULT_FIELD_NAME = "spanId";
    private final String fieldName;
    private final Config.FieldConfig config;

    public SpanIdJsonProvider(Config.FieldConfig config) {
        this(config, DEFAULT_FIELD_NAME);
    }

    public SpanIdJsonProvider(Config.FieldConfig config, String defaultName) {
        this.config = config;
        this.fieldName = config.fieldName().orElse(defaultName);
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        String spanId = event.getMdc(MDC_FIELD_NAME);
        if (!StringUtil.isNullOrEmpty(spanId)) {
            JsonWritingUtils.writeStringField(generator, fieldName, spanId);
        }
    }

    @Override
    public boolean isEnabled() {
        return config.enabled().orElse(true);
    }
}
