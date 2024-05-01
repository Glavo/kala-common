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

import kala.Conditions;
import kala.value.AnyValue;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.Atomic${WrapperType};
import java.util.function.*;

@SuppressWarnings("NullableProblems")
public final class Atomic${Type}Var extends Atomic${WrapperType} implements Mutable${Type}Value {
    public Atomic${Type}Var() {
    }

    public Atomic${Type}Var(${PrimitiveType} initialValue) {
        super(initialValue);
    }

    @Override
    public void update(@NotNull ${Type}UnaryOperator updateFunction) {
        updateAndGet(updateFunction);
    }

    @Override
    public void accumulate(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        accumulateAndGet(t, accumulatorFunction);
    }

<#if Type == "Int" || Type == "Long">
    @Override
    public void increment() {
        incrementAndGet();
    }

    @Override
    public void decrement() {
        decrementAndGet();
    }

    @Override
    public void add(${PrimitiveType} delta) {
        addAndGet(delta);
    }

    @Override
    public void sub(${PrimitiveType} delta) {
        subAndGet(delta);
    }

    @Override
    public ${PrimitiveType} subAndGet(${PrimitiveType} delta) {
        return addAndGet(-delta);
    }

    @Override
    public ${PrimitiveType} getAndSub(${PrimitiveType} delta) {
        return getAndAdd(-delta);
    }

</#if>
    @Override
    public int hashCode() {
        return ${WrapperType}.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyValue<?>))
            return false;
        AnyValue<?> other = (AnyValue<?>) obj;
        if (!this.canEqual(other) || !other.canEqual(this))
            return false;

        if (other instanceof ${Type}Value)
            return this.get() == ((${Type}Value) other).get();

        return Conditions.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "Atomic${Type}Var[" + get() + "]";
    }
}
