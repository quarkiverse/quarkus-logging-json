package io.quarkiverse.loggingjson;

import java.io.IOException;

import io.quarkiverse.loggingjson.config.Config;

public interface JsonFactory {
    void setConfig(Config config);

    JsonGenerator createGenerator(StringBuilderWriter writer) throws IOException;
}
