package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface Mutable${Type}Value extends MutablePrimitiveValue<${WrapperType}>, ${Type}Value {

    void set(${PrimitiveType} value);

    @Override
    default void setValue(@NotNull ${WrapperType} value) {
        set(value);
    }
}
