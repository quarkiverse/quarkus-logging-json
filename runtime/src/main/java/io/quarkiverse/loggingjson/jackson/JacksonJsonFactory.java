package io.quarkiverse.loggingjson.jackson;

import java.io.IOException;
import java.util.ServiceConfigurationError;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;

public class JacksonJsonFactory implements JsonFactory {

    private final com.fasterxml.jackson.core.JsonFactory jsonFactory;

    public JacksonJsonFactory() {
        jsonFactory = createJsonFactory();
    }

    private com.fasterxml.jackson.core.JsonFactory createJsonFactory() {
        ObjectMapper objectMapper = new ObjectMapper()
                /*
                 * Assume empty beans are ok.
                 */
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            objectMapper.findAndRegisterModules();
        } catch (ServiceConfigurationError serviceConfigurationError) {
            // TODO Fix output of error message

            //                addError("Error occurred while dynamically loading jackson modules", serviceConfigurationError);
            System.err.println("Error occurred while dynamically loading jackson modules");
            serviceConfigurationError.printStackTrace();
        }

        return objectMapper
                .getFactory()
                /*
                 * When generators are flushed, don't flush the underlying outputStream.
                 *
                 * This allows some streaming optimizations when using an encoder.
                 *
                 * The encoder generally determines when the stream should be flushed
                 * by an 'immediateFlush' property.
                 *
                 * The 'immediateFlush' property of the encoder can be set to false
                 * when the appender performs the flushes at appropriate times
                 * (such as the end of a batch in the AbstractLogstashTcpSocketAppender).
                 */
                .disable(com.fasterxml.jackson.core.JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) throws IOException {
        return new JacksonJsonGenerator(jsonFactory.createGenerator(writer));
    }
}
