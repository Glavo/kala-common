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

    static @NotNull Mutable${Type}Value create(${PrimitiveType} initialValue) {
        return new ${Type}Var(initialValue);
    }

    static @NotNull Mutable${Type}Value createVolatile() {
        return new Volatile${Type}Var();
    }

    static @NotNull Mutable${Type}Value createVolatile(${PrimitiveType} initialValue) {
        return new Volatile${Type}Var(initialValue);
    }

<#if Type == "Int" || Type == "Long">
    static @NotNull Mutable${Type}Value createAtomic() {
        return new Atomic${Type}Var();
    }

    static @NotNull Mutable${Type}Value createAtomic(${PrimitiveType} initialValue) {
        return new Atomic${Type}Var(initialValue);
    }

</#if>
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

    // ...

    default ${PrimitiveType} getAndSet(${PrimitiveType} newValue) {
        ${PrimitiveType} oldValue = get();
        set(newValue);
        return oldValue;
    }

    default boolean compareAndSet(${PrimitiveType} expectedValue, ${PrimitiveType} newValue) {
        if (${PrimitiveEquals("get()", "expectedValue")}) {
            set(newValue);
            return true;
        } else {
            return false;
        }
    }

    default void update(@NotNull ${Type}UnaryOperator updateFunction) {
        set(updateFunction.applyAs${Type}(get()));
    }

    default ${PrimitiveType} getAndUpdate(@NotNull ${Type}UnaryOperator updateFunction) {
        ${PrimitiveType} v = get();
        set(updateFunction.applyAs${Type}(v));
        return v;
    }

    default ${PrimitiveType} updateAndGet(@NotNull ${Type}UnaryOperator updateFunction) {
        ${PrimitiveType} v = updateFunction.applyAs${Type}(get());
        set(v);
        return v;
    }

    default void accumulate(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        set(accumulatorFunction.applyAs${Type}(get(), t));
    }

    default ${PrimitiveType} getAndAccumulate(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        ${PrimitiveType} v = get();
        set(accumulatorFunction.applyAs${Type}(v, t));
        return v;
    }

    default ${PrimitiveType} accumulateAndGet(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        ${PrimitiveType} v = accumulatorFunction.applyAs${Type}(get(), t);
        set(v);
        return v;
    }
<#if Type == "Int" || Type == "Long">

    default void increment() {
        set(get() + 1);
    }

    default void decrement() {
        set(get() - 1);
    }

    default void add(${PrimitiveType} delta) {
        set(get() + delta);
    }

    default void sub(${PrimitiveType} delta) {
        set(get() - delta);
    }

    default ${PrimitiveType} getAndIncrement() {
        ${PrimitiveType} v = get();
        set(v + 1);
        return v;
    }

    default ${PrimitiveType} getAndDecrement() {
        ${PrimitiveType} v = get();
        set(v - 1);
        return v;
    }

    default ${PrimitiveType} getAndAdd(${PrimitiveType} delta) {
        ${PrimitiveType} v = get();
        set(get() + delta);
        return v;
    }

    default ${PrimitiveType} getAndSub(${PrimitiveType} delta) {
        ${PrimitiveType} v = get();
        set(get() - delta);
        return v;
    }

    default ${PrimitiveType} incrementAndGet() {
        ${PrimitiveType} v = get() + 1;
        set(v);
        return v;
    }

    default ${PrimitiveType} decrementAndGet() {
        ${PrimitiveType} v = get() - 1;
        set(v);
        return v;
    }

    default ${PrimitiveType} addAndGet(${PrimitiveType} delta) {
        ${PrimitiveType} v = get() + delta;
        set(v);
        return v;
    }

    default ${PrimitiveType} subAndGet(${PrimitiveType} delta) {
        ${PrimitiveType} v = get() - delta;
        set(v);
        return v;
    }
<#elseif Type == "Byte" || Type == "Short">

    default void increment() {
        set((${PrimitiveType}) (get() + 1));
    }

    default void decrement() {
        set((${PrimitiveType}) (get() - 1));
    }

    default void add(int delta) {
        set((${PrimitiveType}) (get() + delta));
    }

    default void sub(int delta) {
        set((${PrimitiveType}) (get() - delta));
    }

    default ${PrimitiveType} getAndIncrement() {
        ${PrimitiveType} v = get();
        set((${PrimitiveType}) (v + 1));
        return v;
    }

    default ${PrimitiveType} getAndDecrement() {
        ${PrimitiveType} v = get();
        set((${PrimitiveType}) (v - 1));
        return v;
    }

    default ${PrimitiveType} getAndAdd(int delta) {
        ${PrimitiveType} v = get();
        set((${PrimitiveType}) (get() + delta));
        return v;
    }

    default ${PrimitiveType} getAndSub(int delta) {
        ${PrimitiveType} v = get();
        set((${PrimitiveType}) (get() - delta));
        return v;
    }

    default ${PrimitiveType} incrementAndGet() {
        ${PrimitiveType} v = (${PrimitiveType}) (get() + 1);
        set(v);
        return v;
    }

    default ${PrimitiveType} decrementAndGet() {
        ${PrimitiveType} v = (${PrimitiveType}) (get() - 1);
        set(v);
        return v;
    }

    default ${PrimitiveType} addAndGet(int delta) {
        ${PrimitiveType} v = (${PrimitiveType}) (get() + delta);
        set(v);
        return v;
    }

    default ${PrimitiveType} subAndGet(int delta) {
        ${PrimitiveType} v = (${PrimitiveType}) (get() - delta);
        set(v);
        return v;
    }
</#if>
}
