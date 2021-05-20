package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableShortValue extends MutablePrimitiveValue<Short>, ShortValue {

    void set(short value);

    @Override
    default void setValue(@NotNull Short value) {
        set(value);
    }
}
