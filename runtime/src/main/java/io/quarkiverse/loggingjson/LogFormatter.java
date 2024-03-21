package io.quarkiverse.loggingjson;

import io.quarkiverse.loggingjson.config.Config;

import java.util.List;

/**
 * We can support different flavors of JSON output by providing different implementations of this interface.
 */
public interface LogFormatter {

    /**
     * Returns the list of providers that will be used to generate the JSON output.
     *
     * @param config the configuration
     * @return the list of providers
     */
    List<JsonProvider> providers(Config config);
}
