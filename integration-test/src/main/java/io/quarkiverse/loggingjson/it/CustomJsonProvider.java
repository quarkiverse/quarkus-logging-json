package io.quarkiverse.loggingjson.it;

import java.io.IOException;

import javax.inject.Singleton;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;

@Singleton
public class CustomJsonProvider implements JsonProvider {

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        generator.writeStringField("myCustomField", "and my custom value"); // Will be added to every log, as a field on the json.
    }
}
