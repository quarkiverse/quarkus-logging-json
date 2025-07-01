package io.quarkiverse.loggingjson.config;

import java.util.Map;

import io.smallrye.config.RelocateConfigSourceInterceptor;

@Deprecated(forRemoval = true, since = "3.2.0")
public class LoggingConfigRelocateConfigSourceInterceptor extends RelocateConfigSourceInterceptor {

    static final Map<String, String> RELOCATIONS = Map.of(
            "quarkus.log.json.console.enable", "quarkus.log.json.console.enabled",
            "quarkus.log.json.file.enable", "quarkus.log.json.file.enabled");

    public LoggingConfigRelocateConfigSourceInterceptor() {
        super(RELOCATIONS);
    }
}
