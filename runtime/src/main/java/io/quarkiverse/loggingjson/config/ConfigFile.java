package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface ConfigFile extends ConfigFormatter {
    /**
     * Determine whether to enable the JSON file formatting extension, which disables "normal" file formatting.
     */
    @WithDefault("false")
    boolean enable();
}
