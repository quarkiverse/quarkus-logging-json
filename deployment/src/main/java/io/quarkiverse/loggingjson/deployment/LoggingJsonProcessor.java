package io.quarkiverse.loggingjson.deployment;

import java.util.Collection;

import jakarta.inject.Inject;

import org.jboss.jandex.ClassInfo;

import io.quarkiverse.loggingjson.JsonFactory;
import io.quarkiverse.loggingjson.LoggingJsonRecorder;
import io.quarkiverse.loggingjson.deployment.config.ConfigJackson;
import io.quarkiverse.loggingjson.jackson.JacksonJsonFactory;
import io.quarkiverse.loggingjson.jsonb.JsonbJsonFactory;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
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
import io.quarkus.jackson.spi.ClassPathJacksonModuleBuildItem;

class LoggingJsonProcessor {

    private static final String FEATURE = "logging-json";

    @Inject
    ConfigJackson configJackson;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void autoRegisterModules(BuildProducer<ClassPathJacksonModuleBuildItem> classPathJacksonModules) {
        for (String moduleClassName : configJackson.modules()) {
            if (QuarkusClassLoader.isClassPresentAtRuntime(moduleClassName)) {
                classPathJacksonModules.produce(new ClassPathJacksonModuleBuildItem(moduleClassName));
            }
        }
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogConsoleFormatBuildItem setUpConsoleFormatter(Capabilities capabilities, LoggingJsonRecorder recorder) {
        return new LogConsoleFormatBuildItem(recorder.initializeConsoleJsonLogging(jsonFactory(capabilities)));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogFileFormatBuildItem setUpFileFormatter(Capabilities capabilities, LoggingJsonRecorder recorder) {
        return new LogFileFormatBuildItem(recorder.initializeFileJsonLogging(jsonFactory(capabilities)));
    }

    private JsonFactory jsonFactory(Capabilities capabilities) {
        if (capabilities.isPresent(Capability.JACKSON)) {
            return new JacksonJsonFactory();
        } else if (capabilities.isPresent(Capability.JSONB)) {
            return new JsonbJsonFactory();
        } else {
            throw new RuntimeException(
                    "Missing json implementation to use for logging-json. Supported: [quarkus-jackson, quarkus-jsonb]");
        }
    }

    @BuildStep
    void discoverJsonProviders(BuildProducer<AdditionalBeanBuildItem> beans,
            CombinedIndexBuildItem combinedIndexBuildItem) {
        Collection<ClassInfo> jsonProviders = combinedIndexBuildItem.getIndex()
                .getAllKnownImplementations(LoggingJsonDotNames.JSON_PROVIDER);
        for (ClassInfo provider : jsonProviders) {
            beans.produce(AdditionalBeanBuildItem.unremovableOf(provider.name().toString()));
        }
    }
}
