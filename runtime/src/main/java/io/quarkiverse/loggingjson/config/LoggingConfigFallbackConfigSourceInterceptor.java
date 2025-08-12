package io.quarkiverse.loggingjson.config;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.smallrye.config.FallbackConfigSourceInterceptor;

/**
 * @deprecated maps the old config to the new config, should be removed at some point
 */
@Deprecated(forRemoval = true, since = "3.2.0")
public class LoggingConfigFallbackConfigSourceInterceptor extends FallbackConfigSourceInterceptor {

    private static final Map<String, String> FALLBACKS = LoggingConfigRelocateConfigSourceInterceptor.RELOCATIONS.entrySet()
            .stream().collect(Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey));

    public LoggingConfigFallbackConfigSourceInterceptor() {
        super(FALLBACKS);
    }
}
