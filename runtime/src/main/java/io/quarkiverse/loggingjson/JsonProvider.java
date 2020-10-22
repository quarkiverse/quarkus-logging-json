package io.quarkiverse.loggingjson;

import java.io.IOException;

import org.jboss.logmanager.ExtLogRecord;

public interface JsonProvider {

    /**
     * Called every time log output is getting formatted.
     * @param generator Used to add data to the json log output.
     * @param event The log event to handle.
     * @throws IOException When failed to format the event.
     */
    void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException;
}
