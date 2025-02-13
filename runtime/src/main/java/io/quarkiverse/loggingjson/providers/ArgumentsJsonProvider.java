package io.quarkiverse.loggingjson.providers;

import java.io.IOException;
import java.util.Optional;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import io.quarkiverse.loggingjson.config.Config;

public class ArgumentsJsonProvider implements JsonProvider, Enabled {
    private final boolean includeStructuredArguments;
    private final boolean includeNonStructuredArguments;
    private final String nonStructuredArgumentsFieldPrefix;
    private final Optional<String> fieldName;

    public ArgumentsJsonProvider(Config.ArgumentsConfig config) {
        this.fieldName = config.fieldName();
        this.includeStructuredArguments = config.includeStructuredArguments();
        this.includeNonStructuredArguments = config.includeNonStructuredArguments();
        this.nonStructuredArgumentsFieldPrefix = config.nonStructuredArgumentsFieldPrefix();
    }

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {

        if (!includeStructuredArguments && !includeNonStructuredArguments) {
            // Short-circuit if nothing is included
            return;
        }

        Object[] args = event.getParameters();

        if (args == null || args.length == 0) {
            return;
        }

        boolean hasWrittenFieldName = false;

        for (int argIndex = 0; argIndex < args.length; argIndex++) {

            Object arg = args[argIndex];

            if (arg instanceof Throwable) {
                continue;
            }

            if (arg instanceof StructuredArgument) {
                if (includeStructuredArguments) {
                    if (!hasWrittenFieldName && fieldName.isPresent()) {
                        generator.writeObjectFieldStart(fieldName.get());
                        hasWrittenFieldName = true;
                    }
                    StructuredArgument structuredArgument = (StructuredArgument) arg;
                    structuredArgument.writeTo(generator);
                }
            } else if (includeNonStructuredArguments) {
                if (!hasWrittenFieldName && fieldName.isPresent()) {
                    generator.writeObjectFieldStart(fieldName.get());
                    hasWrittenFieldName = true;
                }
                String innerFieldName = nonStructuredArgumentsFieldPrefix + argIndex;
                generator.writeObjectField(innerFieldName, arg);
            }
        }

        if (hasWrittenFieldName) {
            generator.writeEndObject();
        }
    }

    @Override
    public boolean isEnabled() {
        return includeStructuredArguments || includeNonStructuredArguments;
    }
}
