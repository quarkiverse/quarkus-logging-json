package io.quarkiverse.loggingjson;

import java.util.Optional;
import java.util.logging.Formatter;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LoggingJsonbJsonRecorder {

    private final RuntimeValue<Config> config;

    public LoggingJsonbJsonRecorder(RuntimeValue<Config> config) {
        this.config = config;
    }

    public RuntimeValue<Optional<Formatter>> initializeConsoleJsonLoggingFormatter(JsonbJsonFactory jsonbJsonFactory) {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().console(), config.getValue(),
                jsonbJsonFactory);
    }

    public RuntimeValue<Optional<Formatter>> initializeFileJsonLoggingFormatter(JsonbJsonFactory jsonbJsonFactory) {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().file(), config.getValue(),
                jsonbJsonFactory);
    }

    public RuntimeValue<Optional<Formatter>> initializeSocketJsonLoggingFormatter(JsonbJsonFactory jsonbJsonFactory) {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().socket(), config.getValue(),
                jsonbJsonFactory);
    }

}
