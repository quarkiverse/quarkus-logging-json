package io.quarkiverse.loggingjson.deployment.providers.custom;

import java.io.IOException;

import jakarta.inject.Singleton;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;

@Singleton
public class ThirdCustomJsonProvider implements JsonProvider, Enabled {
    private long isEnabledNumberOfCalls = 0;
    private long writeToNumberOfCalls = 0;

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        writeToNumberOfCalls++;
        generator.writeNumberField("third", 3);
    }

    @Override
    public boolean isEnabled() {
        isEnabledNumberOfCalls++;
        return false;
    }

    public long getIsEnabledNumberOfCalls() {
        return isEnabledNumberOfCalls;
    }

    public long getWriteToNumberOfCalls() {
        return writeToNumberOfCalls;
    }
}
