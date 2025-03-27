package io.quarkiverse.loggingjson.config;

import io.smallrye.config.WithDefault;

public interface ConfigFormatter {

    /**
     * Determine whether the formatter is enabled.
     *
     * @return true if the formatter is enabled, false otherwise
     */
    @WithDefault("false")
    default boolean isEnabled() {
        return false;
    }
}
