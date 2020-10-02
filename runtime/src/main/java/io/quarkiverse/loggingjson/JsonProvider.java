package io.quarkiverse.loggingjson;

import org.jboss.logmanager.ExtLogRecord;

import java.io.IOException;

public interface JsonProvider {

    void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException;
}
