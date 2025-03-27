package io.quarkiverse.loggingjson.config;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.smallrye.config.ConfigSourceInterceptorContext;
import io.smallrye.config.RelocateConfigSourceInterceptor;

public class LoggingConfigRelocateConfigSourceInterceptor extends RelocateConfigSourceInterceptor {

    private static final Map<String, String> RELOCATIONS = relocations();

    public LoggingConfigRelocateConfigSourceInterceptor() {
        super(RELOCATIONS);
    }

    @Override
    public Iterator<String> iterateNames(final ConfigSourceInterceptorContext context) {
        final Set<String> names = new HashSet<>();
        final Iterator<String> namesIterator = context.iterateNames();
        while (namesIterator.hasNext()) {
            final String name = namesIterator.next();
            names.add(name);
            final String mappedName = RELOCATIONS.get(name);
            if (mappedName != null) {
                names.add(mappedName);
            }
        }
        return names.iterator();
    }

    private static Map<String, String> relocations() {
        return Map.of(
                "quarkus.log.console.json.enable", "quarkus.log.console.json.enabled",
                "quarkus.log.file.json.enable", "quarkus.log.file.json.enabled");
    }

}
