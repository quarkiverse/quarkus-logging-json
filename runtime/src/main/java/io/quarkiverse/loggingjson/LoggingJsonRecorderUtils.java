package io.quarkiverse.loggingjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Formatter;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.config.ConfigFormatter;
import io.quarkiverse.loggingjson.providers.*;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableInstance;
import io.quarkus.runtime.RuntimeValue;

public class LoggingJsonRecorderUtils {

    private static final Logger log = Logger.getLogger(LoggingJsonRecorderUtils.class);

    private LoggingJsonRecorderUtils() {
    }

    public static RuntimeValue<Optional<Formatter>> initializeJsonLoggingFormatter(ConfigFormatter formatter, Config config,
            JsonFactory jsonFactory) {
        if (formatter == null || !formatter.isEnabled()) {
            return new RuntimeValue<>(Optional.empty());
        }

        jsonFactory.setConfig(config);

        List<JsonProvider> providers;

        if (config.logFormat() == Config.LogFormat.ECS) {
            providers = getProvidersInEcsFormat(config);
        } else {
            providers = getProvidersInDefaultFormat(config);
        }

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
            log.debug("Installed json providers " + installedProviders);
        }

        return new RuntimeValue<>(Optional.of(new JsonFormatter(providers, jsonFactory, config)));
    }

    public static List<JsonProvider> getProvidersInDefaultFormat(Config config) {
        Config.FieldsConfig fields = config.fields();
        List<JsonProvider> providers = new ArrayList<>();

        providers.add(new TimestampJsonProvider(fields.timestamp()));
        providers.add(new SequenceJsonProvider(fields.sequence()));
        providers.add(new LoggerClassNameJsonProvider(fields.loggerClassName()));
        providers.add(new LoggerNameJsonProvider(fields.loggerName()));
        providers.add(new LogLevelJsonProvider(fields.level()));
        providers.add(new MessageJsonProvider(fields.message()));
        providers.add(new ThreadNameJsonProvider(fields.threadName()));
        providers.add(new ThreadIdJsonProvider(fields.threadId()));
        providers.add(new MDCJsonProvider(fields.mdc()));
        providers.add(new NDCJsonProvider(fields.ndc()));
        providers.add(new HostNameJsonProvider(fields.hostname()));
        providers.add(new ProcessNameJsonProvider(fields.processName()));
        providers.add(new ProcessIdJsonProvider(fields.processId()));
        providers.add(new StackTraceJsonProvider(fields.stackTrace()));
        providers.add(new ErrorTypeJsonProvider(fields.errorType()));
        providers.add(new ErrorMessageJsonProvider(fields.errorMessage()));
        providers.add(new ArgumentsJsonProvider(fields.arguments()));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField()));

        return providers;
    }

    public static List<JsonProvider> getProvidersInEcsFormat(Config config) {
        Config.FieldsConfig fields = config.fields();
        List<JsonProvider> providers = new ArrayList<>();

        providers.add(new TimestampJsonProvider(fields.timestamp(), "@timestamp"));
        providers.add(new LoggerNameJsonProvider(fields.loggerName(), "log.logger"));
        providers.add(new LogLevelJsonProvider(fields.level(), "log.level"));
        providers.add(new ThreadNameJsonProvider(fields.threadName(), "process.thread.name"));
        providers.add(new ThreadIdJsonProvider(fields.threadId(), "process.thread.id"));
        providers.add(new MDCJsonProvider(fields.mdc()));
        providers.add(new HostNameJsonProvider(fields.hostname(), "host.name"));
        providers.add(new StackTraceJsonProvider(fields.stackTrace(), "error.stack_trace"));
        providers.add(new ErrorTypeJsonProvider(fields.errorType(), "error.type"));
        providers.add(new ErrorMessageJsonProvider(fields.errorMessage(), "error.message"));
        providers.add(new ArgumentsJsonProvider(fields.arguments()));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField()));
        providers.add(new MessageJsonProvider(fields.message()));
        providers.add(new StaticKeyValueProvider("ecs.version", "9.0.0"));

        return providers;
    }

}
