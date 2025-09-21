package io.quarkiverse.loggingjson.providers;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public class ObjectMapperProvider {

    @Inject
    ObjectMapper mapper;

    public ObjectMapper get() {
        return this.mapper;
    }

}
