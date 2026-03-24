package io.quarkiverse.loggingjson.jackson;

import org.jboss.logging.Logger;

import io.quarkus.arc.Arc;

public final class JacksonJsonFactoryHolder {

    private static final Logger log = Logger.getLogger(JacksonJsonFactoryHolder.class);

    private JacksonJsonFactoryHolder() {
    }

    private static JacksonJsonFactory initializeFactory() {
        JacksonObjectMapperSupplier jacksonObjectMapperSupplier = Arc.container().instance(JacksonObjectMapperSupplier.class)
                .orElse(null);
        if (jacksonObjectMapperSupplier != null) {
            return new JacksonJsonFactory(jacksonObjectMapperSupplier.getObjectMapper());
        }
        log.warn("ObjectMapperProvider not found, falling back to default ObjectMapper.");
        return new JacksonJsonFactory();
    }

    public static JacksonJsonFactory getFactory() {
        return Holder.FACTORY;
    }

    private static class Holder {
        private static final JacksonJsonFactory FACTORY = initializeFactory();
    }

}
