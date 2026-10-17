package io.quarkiverse.loggingjson.jackson;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public class JacksonObjectMapperSupplier {

    @Inject
    ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
