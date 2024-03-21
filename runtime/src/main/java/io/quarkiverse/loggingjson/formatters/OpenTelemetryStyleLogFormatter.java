package io.quarkiverse.loggingjson.formatters;

import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.LogFormatter;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.providers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Open Telemetry doesn't define a JSON schema for logs but this is a close
 * approximation according to their Log Data Model as defined here:
 * https://opentelemetry.io/docs/specs/otel/logs/data-model/
 */
public class OpenTelemetryStyleLogFormatter implements LogFormatter {

    @Override
    public List<JsonProvider> providers(Config config) {
        List<JsonProvider> providers = new ArrayList<>();

        // Timestamp is in Unix Epoch milliseconds
        providers.add(new UnixEpochTimestampJsonProvider(config.fields.timestamp, "timestamp"));

        // ObservedTimestamp will be added by the collector

        // TraceId and SpanId are assumed to come from the mdc context
        // We will add them to the Attributes section
        providers.add(new OtelAttributesJsonProvider(config.fields.mdc));

        // severity text is the log level
        providers.add(new OtelSeverityTextJsonProvider(config.fields.level, "severity.text"));

        // severity number is the log level as a number
        providers.add(new OtelSeverityNumberJsonProvider("severity.number"));

        // body is the log message
        providers.add(new MessageJsonProvider(config.fields.message, "body"));

        // resource consists of


        providers.add(new ThreadNameJsonProvider(config.fields.threadName, "process.thread.name"));
        providers.add(new ThreadIdJsonProvider(config.fields.threadId, "process.thread.id"));
        providers.add(new MDCJsonProvider(config.fields.mdc));
        providers.add(new HostNameJsonProvider(config.fields.hostname, "host.name"));
        providers.add(new StackTraceJsonProvider(config.fields.stackTrace, "error.stack_trace"));
        providers.add(new ErrorTypeJsonProvider(config.fields.errorType, "error.type"));
        providers.add(new ErrorMessageJsonProvider(config.fields.errorMessage, "error.message"));
        providers.add(new ArgumentsJsonProvider(config.fields.arguments));
        providers.add(new AdditionalFieldsJsonProvider(config.additionalField));
        return providers;
    }
}
