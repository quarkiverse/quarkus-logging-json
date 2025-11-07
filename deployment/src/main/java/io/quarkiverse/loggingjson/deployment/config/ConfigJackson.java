package io.quarkiverse.loggingjson.deployment.config;

import java.util.List;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
@ConfigMapping(prefix = "quarkus.log.json.jackson.object.mapper")
public interface ConfigJackson {

    /**
     * List of default jackson modules to register always.
     *
     * @return module names list
     */
    @WithName("base-modules")
    @WithDefault("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule,com.fasterxml.jackson.datatype.jdk8.Jdk8Module,com.fasterxml.jackson.module.paramnames.ParameterNamesModule")
    List<String> baseModules();

    /**
     * List of additional jackson modules to register.
     *
     * @return module names list
     */
    @WithName("additional-modules")
    Optional<List<String>> additionalModules();

}
