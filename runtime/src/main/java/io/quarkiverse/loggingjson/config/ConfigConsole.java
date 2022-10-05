package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class ConfigConsole implements ConfigFormatter {
    /**
     * Determine whether to enable the JSON console formatting extension, which disables "normal" console formatting.
     */
    @ConfigItem(defaultValue = "true")
    boolean enable;

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
