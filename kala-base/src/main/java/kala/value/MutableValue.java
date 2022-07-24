package kala.value;

import kala.annotations.ReplaceWith;
import kala.annotations.UnstableName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface MutableValue<T> extends MutableAnyValue<T>, Value<T>, Consumer<T> {

    static <T> @NotNull MutableValue<T> create() {
        return new Var<>();
    }

    static <T> @NotNull MutableValue<T> create(T initValue) {
        return new Var<>(initValue);
    }

    @UnstableName
    static <T> @NotNull MutableValue<T> by(@NotNull Supplier<? extends T> getter, @NotNull Consumer<? super T> setter) {
        Objects.requireNonNull(getter);
        Objects.requireNonNull(setter);
        return new DelegateMutableValue<>(getter, setter);
    }

    void set(T value);

    @Override
    default void setValue(T value) {
        set(value);
    }

    @Override
    @Deprecated
    @ReplaceWith("set(T)")
    default void accept(T t) {
        set(t);
    }

    default @NotNull MutableValue<T> withGetter(@NotNull Supplier<? extends T> newGetter) {
        Objects.requireNonNull(newGetter);
        return new DelegateMutableValue<>(newGetter, this);
    }

    default @NotNull MutableValue<T> withSetter(@NotNull Consumer<? super T> newSetter) {
        Objects.requireNonNull(newSetter);
        return new DelegateMutableValue<>(this, newSetter);
    }

    @Override
    @Deprecated
    @ReplaceWith("withSetter(Consumer<T>)")
    default @NotNull MutableValue<T> asMutable(@NotNull Consumer<? super T> setter) {
        return withSetter(setter);
    }
}
