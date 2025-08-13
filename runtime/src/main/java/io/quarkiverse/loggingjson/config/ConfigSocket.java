package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface ConfigSocket extends ConfigFormatter {
    /**
     * Determine whether to enable the JSON socket formatting extension, which disables "normal" socket formatting.
     */
    @WithDefault("true")
    boolean enabled();

    default boolean isEnabled() {
        return enabled();
    }
}
