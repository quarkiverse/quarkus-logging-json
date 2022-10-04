package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class ConfigFile implements ConfigFormatter {
    /**
     * Determine whether to enable the JSON file formatting extension, which disables "normal" file formatting.
     */
    @ConfigItem(defaultValue = "false")
    boolean enable;

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
