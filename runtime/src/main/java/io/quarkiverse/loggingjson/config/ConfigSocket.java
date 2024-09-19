package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class ConfigSocket implements ConfigFormatter {
    /**
     * Determine whether to enable the JSON socket formatting extension, which disables "normal" socket formatting.
     */
    @ConfigItem(defaultValue = "true")
    boolean enable;

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
