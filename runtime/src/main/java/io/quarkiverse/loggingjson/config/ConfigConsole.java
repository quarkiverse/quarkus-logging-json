package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface ConfigConsole extends ConfigFormatter {
    /**
     * Determine whether to enable the JSON console formatting extension, which disables "normal" console formatting.
     */
    @WithDefault("true")
    boolean enable();
}
