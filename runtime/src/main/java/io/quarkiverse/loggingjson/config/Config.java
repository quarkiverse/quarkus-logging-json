package io.quarkiverse.loggingjson.config;

import java.util.Map;
import java.util.Optional;

import io.quarkiverse.loggingjson.providers.ArgumentsJsonProvider;
import io.quarkiverse.loggingjson.providers.StructuredArgument;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.log.json")
public interface Config {
    /**
     * Configuration properties for console formatter.
     */
    @WithName("console")
    ConfigConsole console();

    /**
     * Configuration properties for file formatter.
     */
    @WithName("file")
    ConfigFile file();

    /**
     * Configuration properties for socket formatter.
     */
    @WithName("socket")
    ConfigSocket socket();

    /**
     * Configuration properties to customize fields
     */
    FieldsConfig fields();

    /**
     * Enable "pretty printing" of the JSON record. Note that some JSON parsers will fail to read pretty printed output.
     */
    @WithDefault("false")
    boolean prettyPrint();

    /**
     * The special end-of-record delimiter to be used. By default, newline delimiter is used.
     */
    @WithDefault("\n")
    String recordDelimiter();

    /**
     * For adding fields to the json output directly from the config.
     */
    @ConfigDocMapKey("field-name")
    @ConfigDocSection
    Map<String, AdditionalFieldConfig> additionalField();

    /**
     * Support changing logging format.
     */
    @WithDefault("DEFAULT")
    LogFormat logFormat();

    @ConfigGroup
    interface FieldsConfig {
        /**
         * Used to customize {@link ArgumentsJsonProvider}
         */
        ArgumentsConfig arguments();

        /**
         * Options for timestamp.
         */
        TimestampField timestamp();

        /**
         * Options for hostname.
         */
        FieldConfig hostname();

        /**
         * Options for sequence.
         */
        FieldConfig sequence();

        /**
         * Options for loggerClassName.
         */
        FieldConfig loggerClassName();

        /**
         * Options for loggerName.
         */
        FieldConfig loggerName();

        /**
         * Options for level.
         */
        FieldConfig level();

        /**
         * Options for message.
         */

        FieldConfig message();

        /**
         * Options for threadName.
         */
        FieldConfig threadName();

        /**
         * Options for threadId.
         */
        FieldConfig threadId();

        /**
         * Options for mdc.
         */
        MDCConfig mdc();

        /**
         * Options for ndc.
         */
        FieldConfig ndc();

        /**
         * Options for processName.
         */
        FieldConfig processName();

        /**
         * Options for processId.
         */
        FieldConfig processId();

        /**
         * Options for stackTrace.
         */
        FieldConfig stackTrace();

        /**
         * Options for errorType.
         */
        FieldConfig errorType();

        /**
         * Options for errorMessage.
         */
        FieldConfig errorMessage();
    }

    @ConfigGroup
    interface FieldConfig {
        /**
         * Used to change the json key for the field.
         */
        Optional<String> fieldName();

        /**
         * Enable or disable the field.
         */
        Optional<Boolean> enabled();
    }

    @ConfigGroup
    interface MDCConfig {
        /**
         * Used to change the json key for the field.
         */
        Optional<String> fieldName();

        /**
         * Enable or disable the field.
         */
        Optional<Boolean> enabled();

        /**
         * Will write the values at the top level of the JSON log object.
         */
        @WithDefault("false")
        boolean flatFields();
    }

    @ConfigGroup
    interface TimestampField {
        /**
         * Used to change the json key for the field.
         */
        Optional<String> fieldName();

        /**
         * The date format to use. The special string "default" indicates that the default format should be used.
         */
        @WithDefault("default")
        String dateFormat();

        /**
         * The zone to use when formatting the timestamp.
         */
        @WithDefault("default")
        String zoneId();

        /**
         * Enable or disable the field.
         */
        Optional<Boolean> enabled();
    }

    @ConfigGroup
    interface ArgumentsConfig {

        /**
         * Used to wrap arguments in a json object, with this fieldName on root json.
         */
        Optional<String> fieldName();

        /**
         * Enable output of structured logging arguments
         * {@link StructuredArgument},
         * default is true.
         */
        @WithDefault("true")
        boolean includeStructuredArguments();

        /**
         * Enable output of non-structured logging arguments, default is false.
         */
        @WithDefault("false")
        boolean includeNonStructuredArguments();

        /**
         * What prefixes to use, when outputting non-structured arguments.
         * Default is `arg`, example key for the first argument will be `arg0`.
         */
        @WithDefault("arg")
        String nonStructuredArgumentsFieldPrefix();
    }

    @ConfigGroup
    interface AdditionalFieldConfig {
        /**
         * Additional field value.
         */
        String value();

        /**
         * Type of the field, default is STRING.
         * Supported types: STRING, INT, LONG, FLOAT, DOUBLE.
         */
        @WithDefault("STRING")
        AdditionalFieldType type();
    }

    enum AdditionalFieldType {
        STRING,
        INT,
        LONG,
        FLOAT,
        DOUBLE
    }

    enum LogFormat {
        DEFAULT,
        ECS
    }
}
