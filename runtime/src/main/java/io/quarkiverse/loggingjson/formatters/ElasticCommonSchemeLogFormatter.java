package io.quarkiverse.loggingjson.formatters;

import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.LogFormatter;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Elastic Common Scheme Log Formatter
 */
public class ElasticCommonSchemeLogFormatter implements LogFormatter {

    public List<JsonProvider> providers (Config config) {
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
