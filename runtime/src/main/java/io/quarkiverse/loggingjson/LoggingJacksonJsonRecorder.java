package io.quarkiverse.loggingjson;

import java.util.Optional;
import java.util.logging.Formatter;

import io.quarkiverse.loggingjson.config.Config;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactoryHolder;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LoggingJacksonJsonRecorder {

    private final RuntimeValue<Config> config;

    public LoggingJacksonJsonRecorder(RuntimeValue<Config> config) {
        this.config = config;
    }

    public RuntimeValue<Optional<Formatter>> initializeConsoleJsonLoggingFormatter() {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().console(), config.getValue(),
                JacksonJsonFactoryHolder.getFactory());
    }

    public RuntimeValue<Optional<Formatter>> initializeFileJsonLoggingFormatter() {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().file(), config.getValue(),
                JacksonJsonFactoryHolder.getFactory());
    }

    public RuntimeValue<Optional<Formatter>> initializeSocketJsonLoggingFormatter() {
        return LoggingJsonRecorderUtils.initializeJsonLoggingFormatter(config.getValue().socket(), config.getValue(),
                JacksonJsonFactoryHolder.getFactory());
    }

}
