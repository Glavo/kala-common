package org.glavo.kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface IntValue extends PrimitiveValue<Integer> {
    int get();

    @Override
    default @NotNull Integer getValue() {
        return get();
    }
}
