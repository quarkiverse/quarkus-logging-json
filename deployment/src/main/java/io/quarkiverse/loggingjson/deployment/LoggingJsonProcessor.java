package io.quarkiverse.loggingjson.deployment;

import io.quarkiverse.loggingjson.JsonStructuredConfig;
import io.quarkiverse.loggingjson.LoggingJsonRecorder;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;

class LoggingJsonProcessor {

    private static final String FEATURE = "quarkiverse-logging-json";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogConsoleFormatBuildItem setUpFormatter(Capabilities capabilities, LoggingJsonRecorder recorder, JsonStructuredConfig config) {
        boolean useJackson;
        if (capabilities.isPresent(Capability.JACKSON)) {
            useJackson = true;
        } else if (capabilities.isPresent(Capability.JSONB)) {
            useJackson = false;
        } else {
            throw new RuntimeException("Missing json implementation to use for logging-json. Supported: [quarkus-jackson, quarkus-jsonb]");
        }
        return new LogConsoleFormatBuildItem(recorder.initializeJsonLogging(config, useJackson));
    }
}
