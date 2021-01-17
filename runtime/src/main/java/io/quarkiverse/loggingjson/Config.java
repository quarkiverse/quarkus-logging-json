package io.quarkiverse.loggingjson;

import java.util.Map;
import java.util.Optional;

import io.quarkiverse.loggingjson.providers.ArgumentsJsonProvider;
import io.quarkiverse.loggingjson.providers.StructuredArgument;
import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "log.console.json")
public class Config {
    /**
     * Configuration properties to customize fields
     */
    @ConfigItem
    public FieldsConfig fields;
    /**
     * Determine whether to enable the JSON console formatting extension, which disables "normal" console formatting.
     */
    @ConfigItem(name = ConfigItem.PARENT, defaultValue = "true")
    boolean enable;
    /**
     * Enable "pretty printing" of the JSON record. Note that some JSON parsers will fail to read pretty printed output.
     */
    @ConfigItem
    boolean prettyPrint;
    /**
     * The special end-of-record delimiter to be used. By default, newline delimiter is used.
     */
    @ConfigItem(defaultValue = "\n")
    String recordDelimiter;
    /**
     * For adding fields to the json output directly from the config.
     */
    @ConfigItem
    @ConfigDocMapKey("field-name")
    @ConfigDocSection
    public Map<String, AdditionalFieldConfig> additionalField;

    @ConfigGroup
    public static class FieldsConfig {
        /**
         * Used to customize {@link ArgumentsJsonProvider}
         */
        @ConfigItem
        public ArgumentsConfig arguments;
        /**
         * Options for timestamp.
         */
        @ConfigItem
        public TimestampField timestamp;
        /**
         * Options for hostname.
         */
        @ConfigItem
        public FieldConfig hostname;
        /**
         * Options for sequence.
         */
        @ConfigItem
        public FieldConfig sequence;
        /**
         * Options for loggerClassName.
         */
        @ConfigItem
        public FieldConfig loggerClassName;
        /**
         * Options for loggerName.
         */
        @ConfigItem
        public FieldConfig loggerName;
        /**
         * Options for level.
         */
        @ConfigItem
        public FieldConfig level;
        /**
         * Options for message.
         */
        @ConfigItem
        public FieldConfig message;
        /**
         * Options for threadName.
         */
        @ConfigItem
        public FieldConfig threadName;
        /**
         * Options for threadId.
         */
        @ConfigItem
        public FieldConfig threadId;
        /**
         * Options for mdc.
         */
        @ConfigItem
        public MDCConfig mdc;
        /**
         * Options for ndc.
         */
        @ConfigItem
        public FieldConfig ndc;
        /**
         * Options for processName.
         */
        @ConfigItem
        public FieldConfig processName;
        /**
         * Options for processId.
         */
        @ConfigItem
        public FieldConfig processId;
        /**
         * Options for stackTrace.
         */
        @ConfigItem
        public FieldConfig stackTrace;
    }

    @ConfigGroup
    public static class FieldConfig {
        /**
         * Used to change the json key for the field.
         */
        @ConfigItem
        public Optional<String> fieldName;
        /**
         * Enable or disable the field.
         */
        @ConfigItem
        public Optional<Boolean> enabled;
    }

    @ConfigGroup
    public static class MDCConfig {
        /**
         * Used to change the json key for the field.
         */
        @ConfigItem
        public Optional<String> fieldName;
        /**
         * Enable or disable the field.
         */
        @ConfigItem
        public Optional<Boolean> enabled;
        /**
         * Will write the values at the top level of the JSON log object.
         */
        @ConfigItem(defaultValue = "false")
        public boolean flatFields;
    }

    @ConfigGroup
    public static class TimestampField {
        /**
         * Used to change the json key for the field.
         */
        @ConfigItem
        public Optional<String> fieldName;
        /**
         * The date format to use. The special string "default" indicates that the default format should be used.
         */
        @ConfigItem(defaultValue = "default")
        public String dateFormat;
        /**
         * The zone to use when formatting the timestamp.
         */
        @ConfigItem(defaultValue = "default")
        public String zoneId;
    }

    @ConfigGroup
    public static class ArgumentsConfig {

        /**
         * Used to wrap arguments in an json object, with this fieldName on root json.
         */
        @ConfigItem
        public Optional<String> fieldName = Optional.empty();
        /**
         * Enable output of structured logging arguments
         * {@link StructuredArgument},
         * default is true.
         */
        @ConfigItem(defaultValue = "true")
        public boolean includeStructuredArguments;
        /**
         * Enable output of non structured logging arguments, default is false.
         */
        @ConfigItem(defaultValue = "false")
        public boolean includeNonStructuredArguments;
        /**
         * What prefix to use, when outputting non structured arguments. Default is `arg`, example key for first argument will
         * be `arg0`.
         */
        @ConfigItem(defaultValue = "arg")
        public String nonStructuredArgumentsFieldPrefix;
    }

    @ConfigGroup
    public static class AdditionalFieldConfig {
        /**
         * Additional field value.
         */
        @ConfigItem
        public String value;
        /**
         * Type of the field, default is STRING.
         * Supported types: STRING, INT, LONG, FLOAT, DOUBLE.
         */
        @ConfigItem(defaultValue = "STRING")
        public AdditionalFieldType type;
    }

    public enum AdditionalFieldType {
        STRING,
        INT,
        LONG,
        FLOAT,
        DOUBLE
    }
}
