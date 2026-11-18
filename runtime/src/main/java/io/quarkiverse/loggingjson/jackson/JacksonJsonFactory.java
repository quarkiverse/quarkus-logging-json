package io.quarkiverse.loggingjson.jackson;

import java.io.IOException;
import java.util.ServiceConfigurationError;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.config.Config;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class JacksonJsonFactory implements JsonFactory {

    private ObjectWriter objectWriter;

    public JacksonJsonFactory() {
        objectWriter = createObjectWriter(false);
    }

    private ObjectWriter createObjectWriter(boolean prettyPrint) {
        JsonMapper.Builder builder = JsonMapper.builder()
                /*
                 * Assume empty beans are ok.
                 */
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(StreamWriteFeature.FLUSH_PASSED_TO_STREAM);

        try {
            builder.findAndAddModules();
        } catch (ServiceConfigurationError serviceConfigurationError) {
            // TODO Fix output of error message

            System.err.println("Error occurred while dynamically loading jackson modules");
            serviceConfigurationError.printStackTrace();
        }

        JsonMapper mapper = builder.build();
        return prettyPrint ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
    }

    @Override
    public void setConfig(Config config) {
        objectWriter = createObjectWriter(config.prettyPrint());
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) throws IOException {
        return new JacksonJsonGenerator(objectWriter.createGenerator(writer));
    }
}
