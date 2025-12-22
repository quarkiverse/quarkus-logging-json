package io.quarkiverse.loggingjson;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Formatter;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.config.ConfigFormatter;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkiverse.loggingjson.providers.*;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableInstance;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LoggingJsonRecorder {
    private static final Logger log = Logger.getLogger(LoggingJsonRecorder.class);
    private static final Map<Class<? extends JsonFactory>, Supplier<JsonFactory>> jsonFactories = Map.of(
            JacksonJsonFactory.class, LoggingJsonRecorder::initializeJacksonJsonFactory,
            JsonbJsonFactory.class, JsonbJsonFactory::new);
    private final RuntimeValue<Config> config;

    public LoggingJsonRecorder(RuntimeValue<Config> config) {
        this.config = config;
    }

    public RuntimeValue<Optional<Formatter>> initializeConsoleJsonLogging(Class<? extends JsonFactory> jsonFactoryClass) {
        return initializeJsonLogging(config.getValue().console(), config.getValue(), jsonFactoryClass);
    }

    public RuntimeValue<Optional<Formatter>> initializeFileJsonLogging(Class<? extends JsonFactory> jsonFactoryClass) {
        return initializeJsonLogging(config.getValue().file(), config.getValue(), jsonFactoryClass);
    }

    public RuntimeValue<Optional<Formatter>> initializeSocketJsonLogging(Class<? extends JsonFactory> jsonFactoryClass) {
        return initializeJsonLogging(config.getValue().socket(), config.getValue(), jsonFactoryClass);
    }

    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(ConfigFormatter formatter, Config config,
            Class<? extends JsonFactory> jsonFactoryClass) {
        if (formatter == null || !formatter.isEnabled()) {
            return new RuntimeValue<>(Optional.empty());
        }
        JsonFactory jsonFactory = getJsonFactory(jsonFactoryClass);
        jsonFactory.setConfig(config);

        List<JsonProvider> providers;

        if (config.logFormat() == Config.LogFormat.ECS) {
            providers = ecsFormat(config);
        } else {
            providers = defaultFormat(config);
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

    private JsonFactory getJsonFactory(Class<? extends JsonFactory> jsonFactoryType) {
        Supplier<JsonFactory> jsonFactorySupplier = jsonFactories.get(jsonFactoryType);
        if (jsonFactorySupplier == null) {
            throw new IllegalArgumentException("Unsupported json factory type: " + jsonFactoryType);
        }
        return jsonFactorySupplier.get();
    }

    private static JacksonJsonFactory initializeJacksonJsonFactory() {
        ObjectMapperProvider objectMapperProvider = Arc.container().instance(ObjectMapperProvider.class).orElse(null);
        if (objectMapperProvider != null) {
            return new JacksonJsonFactory(objectMapperProvider.get());
        }
        log.warn("ObjectMapperProvider not found, falling back to default ObjectMapper.");
        return new JacksonJsonFactory();
    }

    private List<JsonProvider> defaultFormat(Config config) {
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new TimestampJsonProvider(config.fields().timestamp()));
        providers.add(new SequenceJsonProvider(config.fields().sequence()));
        providers.add(new LoggerClassNameJsonProvider(config.fields().loggerClassName()));
        providers.add(new LoggerNameJsonProvider(config.fields().loggerName()));
        providers.add(new LogLevelJsonProvider(config.fields().level()));
        providers.add(new MessageJsonProvider(config.fields().message()));
        providers.add(new ThreadNameJsonProvider(config.fields().threadName()));
        providers.add(new ThreadIdJsonProvider(config.fields().threadId()));
        providers.add(new MDCJsonProvider(config.fields().mdc()));
        providers.add(new NDCJsonProvider(config.fields().ndc()));
        providers.add(new HostNameJsonProvider(config.fields().hostname()));
        providers.add(new ProcessNameJsonProvider(config.fields().processName()));
        providers.add(new ProcessIdJsonProvider(config.fields().processId()));
        providers.add(new StackTraceJsonProvider(config.fields().stackTrace()));
        providers.add(new ErrorTypeJsonProvider(config.fields().errorType()));
        providers.add(new ErrorMessageJsonProvider(config.fields().errorMessage()));
        providers.add(new ArgumentsJsonProvider(config.fields().arguments()));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField()));
        return providers;
    }

    private List<JsonProvider> ecsFormat(Config config) {
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new TimestampJsonProvider(config.fields().timestamp(), "@timestamp"));
        providers.add(new LoggerNameJsonProvider(config.fields().loggerName(), "log.logger"));
        providers.add(new LogLevelJsonProvider(config.fields().level(), "log.level"));
        providers.add(new ThreadNameJsonProvider(config.fields().threadName(), "process.thread.name"));
        providers.add(new ThreadIdJsonProvider(config.fields().threadId(), "process.thread.id"));
        providers.add(new MDCJsonProvider(config.fields().mdc()));
        providers.add(new HostNameJsonProvider(config.fields().hostname(), "host.name"));
        providers.add(new StackTraceJsonProvider(config.fields().stackTrace(), "error.stack_trace"));
        providers.add(new ErrorTypeJsonProvider(config.fields().errorType(), "error.type"));
        providers.add(new ErrorMessageJsonProvider(config.fields().errorMessage(), "error.message"));
        providers.add(new ArgumentsJsonProvider(config.fields().arguments()));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField()));
        providers.add(new MessageJsonProvider(config.fields().message()));
        providers.add(new StaticKeyValueProvider("ecs.version", "9.0.0"));
        return providers;
    }
}
