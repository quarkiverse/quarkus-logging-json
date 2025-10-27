package io.quarkiverse.loggingjson.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import io.quarkus.deployment.builditem.LogSocketFormatBuildItem;
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
    @Record(ExecutionTime.RUNTIME_INIT)
    LogConsoleFormatBuildItem setUpConsoleFormatter(Capabilities capabilities, LoggingJsonRecorder recorder) {
        return new LogConsoleFormatBuildItem(recorder.initializeConsoleJsonLogging(jsonFactory(capabilities)));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogFileFormatBuildItem setUpFileFormatter(Capabilities capabilities, LoggingJsonRecorder recorder) {
        return new LogFileFormatBuildItem(recorder.initializeFileJsonLogging(jsonFactory(capabilities)));
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogSocketFormatBuildItem setUpSocketFormatter(Capabilities capabilities, LoggingJsonRecorder recorder) {
        return new LogSocketFormatBuildItem(recorder.initializeSocketJsonLogging(jsonFactory(capabilities)));
    }

    @BuildStep
    void autoRegisterModules(BuildProducer<ClassPathJacksonModuleBuildItem> classPathJacksonModules) {
        List<String> moduleNames = new ArrayList<>(configJackson.baseModules());
        configJackson.additionalModules().ifPresent(moduleNames::addAll);
        for (String moduleClassName : moduleNames) {
            if (QuarkusClassLoader.isClassPresentAtRuntime(moduleClassName)) {
                classPathJacksonModules.produce(new ClassPathJacksonModuleBuildItem(moduleClassName));
            }
        }
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
