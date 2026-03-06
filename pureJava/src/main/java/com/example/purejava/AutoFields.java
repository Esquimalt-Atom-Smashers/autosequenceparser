package com.example.purejava;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for MetaField parsers.
 * Allows complex data types to be parsed from strings in config files.
 */
public class AutoFields {
    private static final Map<Class<?>, MetaField<?>> registry = new HashMap<>();

    public static <T> void register(Class<T> type, MetaField<T> parser) {
        registry.put(type, parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, String input) {
        MetaField<T> parser = (MetaField<T>) registry.get(type);
        if (parser == null) {
            throw new IllegalArgumentException("No MetaField registered for type: " + type.getName());
        }
        return parser.parse(input);
    }
}
