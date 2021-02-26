package org.glavo.kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableCharValue extends MutablePrimitiveValue<Character>, CharValue {

    void set(char value);

    @Override
    default void setValue(@NotNull Character value) {
        set(value);
    }
}
