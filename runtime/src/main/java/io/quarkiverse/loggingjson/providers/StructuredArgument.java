package io.quarkiverse.loggingjson.providers;

import io.quarkiverse.loggingjson.JsonGenerator;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * A wrapper for an argument passed to a log method (e.g. {@link Logger#info(String, Object...)})
 * that adds data to the JSON event (via {@link ArgumentsJsonProvider}).
 */
public interface StructuredArgument {

    /**
     * Writes the data associated with this argument to the given {@link JsonGenerator}.
     */
    void writeTo(JsonGenerator generator) throws IOException;

    /**
     * Writes the data associated with this argument to a {@link String} to be
     * included in a log event's formatted message (via parameter substitution).
     * <p>
     * Note that this will only be included in the log event's formatted
     * message if the message format includes a parameter for this argument.
     */
    String toString();

}
