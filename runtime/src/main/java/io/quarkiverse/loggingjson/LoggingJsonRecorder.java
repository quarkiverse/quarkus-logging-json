package io.quarkiverse.loggingjson;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.config.ConfigFormatter;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableInstance;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.logging.Formatter;
import java.util.stream.Collectors;

@Recorder
public class LoggingJsonRecorder {
    private static final Logger log = LoggerFactory.getLogger(LoggingJsonRecorder.class);

    public RuntimeValue<Optional<Formatter>> initializeConsoleJsonLogging(Config config,
            JsonFactory jsonFactory) {
        return initializeJsonLogging(config.console, config, jsonFactory);
    }

    public RuntimeValue<Optional<Formatter>> initializeFileJsonLogging(Config config,
            JsonFactory jsonFactory) {
        return initializeJsonLogging(config.file, config, jsonFactory);
    }

    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(ConfigFormatter formatter, Config config,
            JsonFactory jsonFactory) {
        if (formatter == null || !formatter.isEnabled()) {
            return new RuntimeValue<>(Optional.empty());
        }

        // Add the providers defined by the log formatter (DEFAULT, ECS or OTEL)
        List<JsonProvider> providers = config.logFormat.getFormatter().providers(config);
        InjectableInstance<JsonProvider> instance = Arc.container().select(JsonProvider.class);
        instance.forEach(providers::add);

        providers.removeIf(p -> {
            if (p instanceof Enabled) {
                return !((Enabled) p).isEnabled();
            } else {
                return false;
            }
        });

        if (log.isDebugEnabled()) {
            String installedProviders = providers.stream().map(p -> p.getClass().toString())
                    .collect(Collectors.joining(", ", "[", "]"));
            log.debug("Installed json providers {}", installedProviders);
        }

        return new RuntimeValue<>(Optional.of(new JsonFormatter(providers, jsonFactory, config)));
    }
}
