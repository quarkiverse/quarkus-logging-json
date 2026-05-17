package io.quarkiverse.loggingjson.deployment;

import io.quarkiverse.loggingjson.LoggingJsonbJsonRecorder;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;
import io.quarkus.deployment.builditem.LogFileFormatBuildItem;
import io.quarkus.deployment.builditem.LogSocketFormatBuildItem;

public class LoggingJsonbJsonProcessor {

    private static final String FEATURE = "logging-jsonb-json";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void setUpFormatters(
            Capabilities capabilities,
            LoggingJsonbJsonRecorder recorder,
            BuildProducer<LogConsoleFormatBuildItem> consoleFormatProducer,
            BuildProducer<LogFileFormatBuildItem> fileFormatProducer,
            BuildProducer<LogSocketFormatBuildItem> socketFormatProducer) {
        if (capabilities.isPresent(Capability.JSONB)) {
            JsonbJsonFactory jsonbJsonFactory = new JsonbJsonFactory();
            consoleFormatProducer
                    .produce(new LogConsoleFormatBuildItem(recorder.initializeConsoleJsonLoggingFormatter(jsonbJsonFactory)));
            fileFormatProducer
                    .produce(new LogFileFormatBuildItem(recorder.initializeFileJsonLoggingFormatter(jsonbJsonFactory)));
            socketFormatProducer
                    .produce(new LogSocketFormatBuildItem(recorder.initializeSocketJsonLoggingFormatter(jsonbJsonFactory)));
        }
    }

}
