package io.quarkiverse.loggingjson.providers;

import java.util.Optional;
import java.util.logging.Level;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkiverse.loggingjson.Config;
import org.jboss.logmanager.ExtLogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrettyPrintingJsonProviderJsonbTest extends KeyValueStructuredArgumentJsonbTest {

    @Override
    protected boolean prettyPrint() {
        return true;
    }

}
