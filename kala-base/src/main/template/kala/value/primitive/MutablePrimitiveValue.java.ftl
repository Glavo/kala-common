/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.value.primitive;

import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.annotations.UnstableName;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public non-sealed interface Mutable${Type}Value extends MutablePrimitiveValue<${WrapperType}>, ${Type}Value, ${Type}Consumer {

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

    @DelegateBy("getAndUpdate(${Type}UnaryOperator)")
    default void update(@NotNull ${Type}UnaryOperator updateFunction) {
        getAndUpdate(updateFunction);
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

    @DelegateBy("getAndAccumulate(${PrimitiveType}, ${Type}BinaryOperator)")
    default void accumulate(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        getAndAccumulate(t, accumulatorFunction);
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
        increment();
        return v;
    }

    default ${PrimitiveType} getAndDecrement() {
        ${PrimitiveType} v = get();
        decrement();
        return v;
    }

    default ${PrimitiveType} getAndAdd(${PrimitiveType} delta) {
        ${PrimitiveType} v = get();
        add(delta);
        return v;
    }

    default ${PrimitiveType} getAndSub(${PrimitiveType} delta) {
        ${PrimitiveType} v = get();
        sub(delta);
        return v;
    }

    default ${PrimitiveType} incrementAndGet() {
        increment();
        return get();
    }

    default ${PrimitiveType} decrementAndGet() {
        decrement();
        return get();
    }

    default ${PrimitiveType} addAndGet(${PrimitiveType} delta) {
        add(delta);
        return get();
    }

    default ${PrimitiveType} subAndGet(${PrimitiveType} delta) {
        sub(delta);
        return get();
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
        increment();
        return v;
    }

    default ${PrimitiveType} getAndDecrement() {
        ${PrimitiveType} v = get();
        decrement();
        return v;
    }

    default ${PrimitiveType} getAndAdd(int delta) {
        ${PrimitiveType} v = get();
        add(delta);
        return v;
    }

    default ${PrimitiveType} getAndSub(int delta) {
        ${PrimitiveType} v = get();
        sub(delta);
        return v;
    }

    default ${PrimitiveType} incrementAndGet() {
        increment();
        return get();
    }

    default ${PrimitiveType} decrementAndGet() {
        decrement();
        return get();
    }

    default ${PrimitiveType} addAndGet(int delta) {
        add(delta);
        return get();
    }

    default ${PrimitiveType} subAndGet(int delta) {
        sub(delta);
        return get();
    }
</#if>
}
