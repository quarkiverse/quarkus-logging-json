package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

/**
 * Map as best we can from the java.util.logging levels to the OTel severity number
 */
public class OtelSeverityNumberJsonProvider implements JsonProvider, Enabled {
    private final String fieldName;

    public OtelSeverityNumberJsonProvider(String defaultName) {
        this.fieldName = defaultName;
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        int severityNumber = 0;
        switch (event.getLevel().toString()) {
            case "SEVERE":
                severityNumber = 17;
                break;
            case "WARNING":
                severityNumber = 13;
                break;
            case "INFO":
                severityNumber = 9;
                break;
            case "CONFIG":
                severityNumber = 6;
                break;
            case "FINE":
                severityNumber = 5;
                break;
            case "FINER":
                severityNumber = 3;
                break;
            case "FINEST":
                severityNumber = 2;
                break;
            case "ALL":
                severityNumber = 1;
                break;
            default:
                severityNumber = 1;
                break;

        }
        JsonWritingUtils.writeNumberField(generator, fieldName, severityNumber);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
