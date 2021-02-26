package org.glavo.kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableLongValue extends MutablePrimitiveValue<Long>, LongValue {

    void set(long value);

    @Override
    default void setValue(@NotNull Long value) {
        set(value);
    }
}
