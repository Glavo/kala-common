package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface CharValue extends PrimitiveValue<Character> {
    char get();

    @Override
    default @NotNull Character getValue() {
        return get();
    }
}
