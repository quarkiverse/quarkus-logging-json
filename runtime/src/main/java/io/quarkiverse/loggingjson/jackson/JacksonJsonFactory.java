package io.quarkiverse.loggingjson.jackson;

import java.io.IOException;
import java.util.ServiceConfigurationError;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.StringBuilderWriter;
import io.quarkiverse.loggingjson.config.Config;

public class JacksonJsonFactory implements JsonFactory {
    private static final Logger log = Logger.getLogger(JacksonJsonFactory.class);
    private final ObjectMapper objectMapper;
    private com.fasterxml.jackson.core.JsonFactory jsonFactory;

    public JacksonJsonFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? initializeObjectMapper() : objectMapper; //just in case
        this.jsonFactory = createJsonFactory(false);
    }

    public JacksonJsonFactory() {
        this.objectMapper = initializeObjectMapper();
        this.jsonFactory = createJsonFactory(false);
    }

    private ObjectMapper initializeObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            objectMapper.findAndRegisterModules();
        } catch (ServiceConfigurationError serviceConfigurationError) {
            log.error("Error occurred while dynamically loading jackson modules.", serviceConfigurationError);
        }
        return objectMapper;
    }

    private com.fasterxml.jackson.core.JsonFactory createJsonFactory(boolean prettyPrint) {
        final JsonFactoryBuilder builder = new JsonFactoryBuilder(objectMapper.getFactory());
        if (prettyPrint) {
            builder.addDecorator((factory, generator) -> generator.useDefaultPrettyPrinter());
        }
        return builder
                .build()
                .setCodec(objectMapper)
                .disable(com.fasterxml.jackson.core.JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);
    }

    @Override
    public void setConfig(Config config) {
        jsonFactory = createJsonFactory(config.prettyPrint());
    }

    @Override
    public JsonGenerator createGenerator(StringBuilderWriter writer) throws IOException {
        return new JacksonJsonGenerator(jsonFactory.createGenerator(writer));
    }
}
