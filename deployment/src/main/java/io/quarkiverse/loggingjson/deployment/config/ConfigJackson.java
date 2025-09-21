package io.quarkiverse.loggingjson.deployment.config;

import java.util.List;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
@ConfigMapping(prefix = "quarkus.log.json.jackson.object.mapper")
public interface ConfigJackson {

    /**
     * List of jackson modules to register.
     *
     * @return module names list
     */
    @WithName("modules")
    @WithDefault("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule,com.fasterxml.jackson.datatype.jdk8.Jdk8Module,com.fasterxml.jackson.module.paramnames.ParameterNamesModule")
    List<String> modules();

}
