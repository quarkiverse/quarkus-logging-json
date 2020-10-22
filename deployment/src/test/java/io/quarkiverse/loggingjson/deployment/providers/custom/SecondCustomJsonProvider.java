package io.quarkiverse.loggingjson.deployment.providers.custom;

import java.io.IOException;

import javax.inject.Singleton;

import org.jboss.logmanager.ExtLogRecord;

import io.quarkiverse.loggingjson.Enabled;
import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;

@Singleton
public class SecondCustomJsonProvider implements JsonProvider, Enabled {
    private volatile long isEnabledNumberOfCalls = 0;
    private volatile long writeToNumberOfCalls = 0;

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {
        writeToNumberOfCalls++;
        generator.writeNumberField("second", 2);
    }

    @Override
    public boolean isEnabled() {
        isEnabledNumberOfCalls++;
        return true;
    }

    public long getIsEnabledNumberOfCalls() {
        return isEnabledNumberOfCalls;
    }

    public long getWriteToNumberOfCalls() {
        return writeToNumberOfCalls;
    }
}
