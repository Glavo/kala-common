package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableIntValue extends MutablePrimitiveValue<Integer>, IntValue {

    void set(int value);

    @Override
    default void setValue(@NotNull Integer value) {
        set(value);
    }
}
