package io.quarkiverse.loggingjson.format;

public class DefaultLogFormatFields implements LogFormatFields {
    @Override
    public String logLevel() {
        return "level";
    }

    @Override
    public String loggerName() {
        return "loggerName";
    }

    @Override
    public String loggerClassName() {
        return "loggerClassName";
    }

    @Override
    public String hostName() {
        return "hostName";
    }
}
