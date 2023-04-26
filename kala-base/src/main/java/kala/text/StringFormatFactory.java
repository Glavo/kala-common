package kala.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class StringFormatFactory {
    private final Map<String, Function<?, String>> formatTypes;

    private StringFormatFactory(Map<String, Function<?, String>> formatTypes) {
        this.formatTypes = formatTypes;
    }

    public static StringFormatFactory.Builder builder() {
        return new Builder();
    }

    public Map<String, Function<?, String>> getFormatTypes() {
        return formatTypes;
    }

    public static final class Builder {
        private final Map<String, Function<?, String>> formatTypes = new HashMap<>();

        Builder() {
        }
    }
}
