package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableBooleanValue extends MutablePrimitiveValue<Boolean>, BooleanValue {

    void set(boolean value);

    @Override
    default void setValue(@NotNull Boolean value) {
        set(value);
    }
}
