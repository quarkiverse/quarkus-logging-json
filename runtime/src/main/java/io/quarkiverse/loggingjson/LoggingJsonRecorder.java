package io.quarkiverse.loggingjson;

import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkiverse.loggingjson.providers.ArgumentsJsonProvider;
import io.quarkiverse.loggingjson.providers.HostNameJsonProvider;
import io.quarkiverse.loggingjson.providers.LogLevelJsonProvider;
import io.quarkiverse.loggingjson.providers.LoggerClassNameJsonProvider;
import io.quarkiverse.loggingjson.providers.LoggerNameJsonProvider;
import io.quarkiverse.loggingjson.providers.MDCJsonProvider;
import io.quarkiverse.loggingjson.providers.MessageJsonProvider;
import io.quarkiverse.loggingjson.providers.NDCJsonProvider;
import io.quarkiverse.loggingjson.providers.ProcessIdJsonProvider;
import io.quarkiverse.loggingjson.providers.ProcessNameJsonProvider;
import io.quarkiverse.loggingjson.providers.SequenceJsonProvider;
import io.quarkiverse.loggingjson.providers.StackTraceJsonProvider;
import io.quarkiverse.loggingjson.providers.ThreadIdJsonProvider;
import io.quarkiverse.loggingjson.providers.ThreadNameJsonProvider;
import io.quarkiverse.loggingjson.providers.TimestampJsonProvider;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Formatter;

@Recorder
public class LoggingJsonRecorder {
    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(Config config, boolean useJackson) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }
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
        providers.add(new ArgumentsJsonProvider(config.fields.arguments));

        providers.removeIf(p -> {
            if (p instanceof Enabled) {
                return !((Enabled) p).isEnabled();
            } else {
                return false;
            }
        });

        JsonFactory jsonFactory;
        if (useJackson) {
            jsonFactory = new JacksonJsonFactory();
        } else {
            jsonFactory = new JsonbJsonFactory();
        }

        return new RuntimeValue<>(Optional.of(new JsonFormatter(providers, jsonFactory, config)));
    }
}
