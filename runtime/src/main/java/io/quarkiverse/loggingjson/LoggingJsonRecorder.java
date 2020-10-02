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
import io.quarkiverse.loggingjson.providers.ThreadIDJsonProvider;
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
    public RuntimeValue<Optional<Formatter>> initializeJsonLogging(JsonStructuredConfig config, boolean useJackson) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }
        List<JsonProvider> providers = new ArrayList<>();
        providers.add(new TimestampJsonProvider(config.dateFormat));
        providers.add(new SequenceJsonProvider());
        providers.add(new LoggerClassNameJsonProvider());
        providers.add(new LoggerNameJsonProvider());
        providers.add(new LogLevelJsonProvider());
        providers.add(new MessageJsonProvider());
        providers.add(new ThreadNameJsonProvider());
        providers.add(new ThreadIDJsonProvider());
        providers.add(new MDCJsonProvider());
        providers.add(new NDCJsonProvider());
        providers.add(new HostNameJsonProvider());
        providers.add(new ProcessNameJsonProvider());
        providers.add(new ProcessIdJsonProvider());
        providers.add(new StackTraceJsonProvider());
        providers.add(new ArgumentsJsonProvider(config));

        JsonFactory jsonFactory;
        if (useJackson) {
            jsonFactory = new JacksonJsonFactory();
        } else {
            jsonFactory = new JsonbJsonFactory();
        }

        return new RuntimeValue<>(Optional.of(new JsonFormatter(providers, jsonFactory, config)));
    }
}
