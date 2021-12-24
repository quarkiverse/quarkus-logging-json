package io.quarkiverse.loggingjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Formatter;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkiverse.loggingjson.providers.*;
import io.quarkiverse.loggingjson.providers.gcp.*;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableInstance;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LoggingJsonRecorder {
    private static final Logger log = LoggerFactory.getLogger(LoggingJsonRecorder.class);

    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(Config config, boolean useJackson) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }

        List<JsonProvider> providers;

        if (config.logFormat == Config.LogFormat.ECS) {
            providers = ecsFormat(config);
        } else if (config.logFormat == Config.LogFormat.GCP) {
            providers = gcpFormat(config);
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
            log.debug("Installed json providers {}", installedProviders);
        }

        JsonFactory jsonFactory;
        if (useJackson) {
            log.debug("Using Jackson as the json implementation");
            jsonFactory = new JacksonJsonFactory();
        } else {
            log.debug("Using Jsonb as the json implementation");
            jsonFactory = new JsonbJsonFactory();
        }

        return new RuntimeValue<>(Optional.of(new JsonFormatter(providers, jsonFactory, config)));

    }

    private List<JsonProvider> defaultFormat(Config config) {
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new TimestampJsonProvider(config.fields.timestamp));
        providers.add(new SequenceJsonProvider(config.fields.sequence));
        providers.add(new LoggerClassNameJsonProvider(config.fields.loggerClassName));
        providers.add(new LoggerNameJsonProvider(config.fields.loggerName));
        providers.add(new LogLevelJsonProvider(config.fields.level));
        providers.add(new MessageJsonProvider(config.fields.message));
        providers.add(new ThreadNameJsonProvider(config.fields.threadName));
        providers.add(new ThreadIdJsonProvider(config.fields.threadId));
        providers.add(new MDCJsonProvider(config.fields.mdc));
        providers.add(new NDCJsonProvider(config.fields.ndc));
        providers.add(new HostNameJsonProvider(config.fields.hostname));
        providers.add(new ProcessNameJsonProvider(config.fields.processName));
        providers.add(new ProcessIdJsonProvider(config.fields.processId));
        providers.add(new StackTraceJsonProvider(config.fields.stackTrace));
        providers.add(new ErrorTypeJsonProvider(config.fields.errorType));
        providers.add(new ErrorMessageJsonProvider(config.fields.errorMessage));
        providers.add(new ArgumentsJsonProvider(config.fields.arguments));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField));
        return providers;
    }

    private List<JsonProvider> gcpFormat(Config config) {
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new GcpEpochSecondJsonProvider(config.fields.epochSecond));
        providers.add(new GcpNanoJsonProvider(config.fields.nanoSecond));
        providers.add(new LoggerNameJsonProvider(config.fields.loggerName, "logger"));
        providers.add(new GcpLogLevelJsonProvider(config.fields.level));
        providers.add(new ThreadNameJsonProvider(config.fields.threadName, "thread"));
        providers.add(new GcpMessageWithExceptionJsonProvider(config.fields.message));
        providers.add(new GcpSpanIdJsonProvider(config.fields.gcpSpanId));
        providers.add(new GcpTraceIdJsonProvider(config.fields.gcpTraceId));
        return providers;
    }

    private List<JsonProvider> ecsFormat(Config config) {
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new TimestampJsonProvider(config.fields.timestamp, "@timestamp"));
        providers.add(new LoggerNameJsonProvider(config.fields.loggerName, "log.logger"));
        providers.add(new LogLevelJsonProvider(config.fields.level, "log.level"));
        providers.add(new ThreadNameJsonProvider(config.fields.threadName, "process.thread.name"));
        providers.add(new ThreadIdJsonProvider(config.fields.threadId, "process.thread.id"));
        providers.add(new MDCJsonProvider(config.fields.mdc));
        providers.add(new HostNameJsonProvider(config.fields.hostname, "host.name"));
        providers.add(new StackTraceJsonProvider(config.fields.stackTrace, "error.stack_trace"));
        providers.add(new ErrorTypeJsonProvider(config.fields.errorType, "error.type"));
        providers.add(new ErrorMessageJsonProvider(config.fields.errorMessage, "error.message"));
        providers.add(new ArgumentsJsonProvider(config.fields.arguments));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField));
        providers.add(new MessageJsonProvider(config.fields.message));
        providers.add(new StaticKeyValueProvider("ecs.version", "1.12.1"));
        return providers;
    }
}
