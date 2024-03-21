package io.quarkiverse.loggingjson.formatters;

import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.LogFormatter;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the default JSON format for Quarkus Logging
 */
public class DefaultLogFormatter implements LogFormatter {
    @Override
    public List<JsonProvider> providers(Config config) {
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
}
