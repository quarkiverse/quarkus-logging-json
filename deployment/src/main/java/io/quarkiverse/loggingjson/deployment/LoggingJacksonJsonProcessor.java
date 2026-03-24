package io.quarkiverse.loggingjson.deployment;

import io.quarkiverse.loggingjson.LoggingJacksonJsonRecorder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
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

public class LoggingJacksonJsonProcessor {

    private static final String FEATURE = "logging-jackson-json";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerObjectMapperSupplier(Capabilities capabilities, BuildProducer<AdditionalBeanBuildItem> beans) {
        if (capabilities.isPresent(Capability.JACKSON)) {
            beans.produce(
                    AdditionalBeanBuildItem.unremovableOf("io.quarkiverse.loggingjson.jackson.JacksonObjectMapperSupplier"));
        }
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void setUpFormatters(
            Capabilities capabilities,
            LoggingJacksonJsonRecorder recorder,
            BuildProducer<LogConsoleFormatBuildItem> consoleFormatProducer,
            BuildProducer<LogFileFormatBuildItem> fileFormatProducer,
            BuildProducer<LogSocketFormatBuildItem> socketFormatProducer) {
        if (capabilities.isPresent(Capability.JACKSON)) {
            consoleFormatProducer.produce(new LogConsoleFormatBuildItem(recorder.initializeConsoleJsonLoggingFormatter()));
            fileFormatProducer.produce(new LogFileFormatBuildItem(recorder.initializeFileJsonLoggingFormatter()));
            socketFormatProducer.produce(new LogSocketFormatBuildItem(recorder.initializeSocketJsonLoggingFormatter()));
        }
    }

}
