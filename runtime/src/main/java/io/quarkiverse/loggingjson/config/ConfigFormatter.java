package io.quarkiverse.loggingjson.config;

import io.quarkus.runtime.annotations.ConfigDocIgnore;

public interface ConfigFormatter {

    @ConfigDocIgnore
    boolean enable();
}
