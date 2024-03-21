package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.JsonWritingUtils;
import io.quarkiverse.loggingjson.config.Config;
import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Put the resource info under the resource section of the json
 * include host, process, thread, source class, source file, source method, source line
 *
 */
public class OtelResourceJsonProvider implements JsonProvider, Enabled {

    private final String fieldName;

    public OtelResourceJsonProvider(String defaultName) {
        this.fieldName = defaultName;
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
        Map<String, String> resourceData = new LinkedHashMap<>();
        resourceData.put("host.hostname", event.getHostName());
        resourceData.put("process.name", event.getProcessName());
        resourceData.put("process.id", ""+event.getProcessId());
        resourceData.put("thread.name", event.getThreadName());
        resourceData.put("source.class.name", event.getSourceClassName());
        resourceData.put("source.file.name", event.getSourceFileName());
        resourceData.put("source.method.name", event.getSourceMethodName());
        resourceData.put("source.line.number", ""+event.getSourceLineNumber());
        JsonWritingUtils.writeMapStringFields(generator, fieldName, resourceData);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
