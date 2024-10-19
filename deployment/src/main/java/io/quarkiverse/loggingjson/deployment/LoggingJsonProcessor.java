package io.quarkiverse.loggingjson.deployment;

import java.util.Collection;

import org.jboss.jandex.ClassInfo;

import io.quarkiverse.loggingjson.JsonFactoryType;
import io.quarkiverse.loggingjson.LoggingJsonRecorder;
import io.quarkiverse.loggingjson.config.Config;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;
import io.quarkus.deployment.builditem.LogFileFormatBuildItem;

class LoggingJsonProcessor {

    private static final String FEATURE = "logging-json";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogConsoleFormatBuildItem setUpConsoleFormatter(Capabilities capabilities, LoggingJsonRecorder recorder,
            Config config) {
        JsonFactoryType factoryType = determineJsonFactoryType(capabilities);

        return new LogConsoleFormatBuildItem(
                recorder.initializeConsoleJsonLogging(config, factoryType));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogFileFormatBuildItem setUpFileFormatter(Capabilities capabilities, LoggingJsonRecorder recorder,
            Config config) {
        JsonFactoryType factoryType = determineJsonFactoryType(capabilities);

        return new LogFileFormatBuildItem(
                recorder.initializeFileJsonLogging(config, factoryType));
    }

    private JsonFactoryType determineJsonFactoryType(Capabilities capabilities) {
        if (capabilities.isPresent(Capability.JACKSON)) {
            return JsonFactoryType.JACKSON;
        } else if (capabilities.isPresent(Capability.JSONB)) {
            return JsonFactoryType.JSONB;
        } else {
            throw new RuntimeException(
                    "Missing JSON implementation to use for logging-json. Supported: [quarkus-jackson, quarkus-jsonb]");
        }
    }

    @BuildStep
    void discoverJsonProviders(BuildProducer<AdditionalBeanBuildItem> beans,
            CombinedIndexBuildItem combinedIndexBuildItem) {
        Collection<ClassInfo> jsonProviders = combinedIndexBuildItem.getIndex()
                .getAllKnownImplementors(LoggingJsonDotNames.JSON_PROVIDER);
        for (ClassInfo provider : jsonProviders) {
            beans.produce(AdditionalBeanBuildItem.unremovableOf(provider.name().toString()));
        }
    }
}
