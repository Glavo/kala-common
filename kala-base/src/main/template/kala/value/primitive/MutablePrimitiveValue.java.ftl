package kala.value.primitive;

import kala.annotations.ReplaceWith;
import kala.annotations.UnstableName;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface Mutable${Type}Value extends MutablePrimitiveValue<${WrapperType}>, ${Type}Value, ${Type}Consumer {

    static @NotNull Mutable${Type}Value create() {
        return new ${Type}Var();
    }

    static @NotNull Mutable${Type}Value create(${PrimitiveType} initValue) {
        return new ${Type}Var(initValue);
    }

    @UnstableName
    static @NotNull Mutable${Type}Value by(@NotNull ${Type}Supplier getter, @NotNull ${Type}Consumer setter) {
        Objects.requireNonNull(getter);
        Objects.requireNonNull(setter);
        return new DelegateMutable${Type}Value(getter, setter);
    }

    void set(${PrimitiveType} value);

    @Override
    default void setValue(@NotNull ${WrapperType} value) {
        set(value);
    }

    @Deprecated
    @ReplaceWith("set(${PrimitiveType})")
    default void accept(${PrimitiveType} value) {
        set(value);
    }

    default @NotNull Mutable${Type}Value withGetter(@NotNull ${Type}Supplier newGetter) {
        Objects.requireNonNull(newGetter);
        return new DelegateMutable${Type}Value(newGetter, this);
    }

    default @NotNull Mutable${Type}Value withSetter(@NotNull ${Type}Consumer newSetter) {
        Objects.requireNonNull(newSetter);
        return new DelegateMutable${Type}Value(this, newSetter);
    }

    @Override
    @Deprecated
    @ReplaceWith("withSetter(${Type}Consumer)")
    default @NotNull Mutable${Type}Value asMutable(@NotNull ${Type}Consumer setter) {
        return withSetter(setter);
    }
}
