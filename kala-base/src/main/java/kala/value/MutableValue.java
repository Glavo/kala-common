package kala.value;

import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.annotations.UnstableName;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface MutableValue<T> extends MutableAnyValue<T>, Value<T>, Consumer<T> {

    static <T> @NotNull MutableValue<T> create() {
        return new Var<>();
    }

    static <T> @NotNull MutableValue<T> create(T initialValue) {
        return new Var<>(initialValue);
    }

    static <T> @NotNull MutableValue<T> createTransient() {
        return new TransientVar<>();
    }

    static <T> @NotNull MutableValue<T> createTransient(T initialValue) {
        return new TransientVar<>(initialValue);
    }

    static <T> @NotNull MutableValue<T> createVolatile() {
        return new VolatileVar<>();
    }

    static <T> @NotNull MutableValue<T> createVolatile(T initialValue) {
        return new VolatileVar<>(initialValue);
    }

    static <T> @NotNull MutableValue<T> createAtomic() {
        return new AtomicVar<>();
    }

    static <T> @NotNull MutableValue<T> createAtomic(T initialValue) {
        return new AtomicVar<>(initialValue);
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

    // ...

    default T getAndSet(T newValue) {
        T oldValue = get();
        set(newValue);
        return oldValue;
    }

    default boolean compareAndSet(T expectedValue, T newValue) {
        if (Objects.equals(get(), expectedValue)) {
            set(newValue);
            return true;
        } else {
            return false;
        }
    }

    @DelegateBy("getAndUpdate(UnaryOperator<T>)")
    default void update(@NotNull UnaryOperator<T> updateFunction) {
        getAndUpdate(updateFunction);
    }

    default T getAndUpdate(@NotNull UnaryOperator<T> updateFunction) {
        T v = get();
        set(updateFunction.apply(v));
        return v;
    }

    default T updateAndGet(@NotNull UnaryOperator<T> updateFunction) {
        T v = updateFunction.apply(get());
        set(v);
        return v;
    }

    @DelegateBy("getAndAccumulate(BinaryOperator<T>)")
    default void accumulate(T t, @NotNull BinaryOperator<T> accumulatorFunction) {
        getAndAccumulate(t, accumulatorFunction);
    }

    default T getAndAccumulate(T t, @NotNull BinaryOperator<T> accumulatorFunction) {
        T v = get();
        set(accumulatorFunction.apply(v, t));
        return v;
    }

    default T accumulateAndGet(T t, @NotNull BinaryOperator<T> accumulatorFunction) {
        T v = accumulatorFunction.apply(get(), t);
        set(v);
        return v;
    }
}
