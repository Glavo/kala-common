package org.glavo.kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface LongValue extends PrimitiveValue<Long> {
    long get();

    @Override
    default @NotNull Long getValue() {
        return get();
    }
}
