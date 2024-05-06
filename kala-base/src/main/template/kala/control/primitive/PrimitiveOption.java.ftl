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
package kala.control.primitive;

import kala.collection.base.primitive.*;

import kala.annotations.ReplaceWith;
import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
<#if IsSpecialized>
import java.util.Optional${Type};
import java.util.function.*;
<#else>
import java.util.function.Supplier;

import kala.function.*;
</#if>

public final class ${Type}Option implements PrimitiveOption<${WrapperType}>, ${Type}Traversable {
    @Serial
    private static final long serialVersionUID = -8990024629462620023L;

    public static final ${Type}Option None = new ${Type}Option();

    private final ${PrimitiveType} value;

    private ${Type}Option() {
        this.value = 0;
    }

    private ${Type}Option(${PrimitiveType} value) {
        this.value = value;
    }

    public static @NotNull ${Type}Option some(${PrimitiveType} value) {
        return new ${Type}Option(value);
    }

    public static @NotNull ${Type}Option none() {
        return None;
    }

    @Deprecated
    @ReplaceWith("some(${PrimitiveType})")
    public static @NotNull ${Type}Option of(${PrimitiveType} value) {
        return some(value);
    }

    @Deprecated
    @ReplaceWith("ofNullable(${WrapperType})")
    public static @NotNull ${Type}Option of(@Nullable ${WrapperType} value) {
        return ofNullable(value);
    }

    public static @NotNull ${Type}Option ofNullable(@Nullable ${WrapperType} value) {
        return value == null ? None : some(value);
    }

<#if IsSpecialized>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static @NotNull ${Type}Option fromJava(@NotNull Optional${Type} optional) {
        return optional.isPresent() ? some(optional.getAs${Type}()) : none();
    }

</#if>
    public boolean isDefined() {
        return this != None;
    }

    public boolean isEmpty() {
        return this == None;
    }

    public ${PrimitiveType} get() {
        if (isEmpty()) {
            throw new NoSuchElementException("${Type}Option.None");
        }
        return value;
    }

    public @Nullable ${WrapperType} getOrNull() {
        return isDefined() ? value : null;
    }

    public ${PrimitiveType} getOrDefault(${PrimitiveType} defaultValue) {
        return isDefined() ? value : defaultValue;
    }

    public ${PrimitiveType} getOrElse(@NotNull ${Type}Supplier supplier) {
        return isDefined() ? get() : supplier.getAs${Type}();
    }

    public <Ex extends Throwable> ${PrimitiveType} getOrThrowException(@NotNull Ex exception) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(exception);
            throw exception;
        }
        return value;
    }

    public <Ex extends Throwable> ${PrimitiveType} getOrThrow(@NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(supplier);
            throw supplier.get();
        }
        return value;
    }

    public @NotNull ${Type}Option orElse(${Type}Option other) {
        return this.isDefined() ? this : other;
    }

    public @NotNull ${Type}Option map(${Type}UnaryOperator mapper) {
        return isDefined() ? ${Type}Option.some(mapper.applyAs${Type}(value)) : None;
    }

    public <T> @NotNull Option<T> mapToObj(@NotNull ${Type}Function<? extends T> mapper) {
        return isDefined() ? Option.some(mapper.apply(value)) : Option.none();
    }

    public @NotNull ${Type}Option filter(@NotNull ${Type}Predicate predicate) {
        return isDefined() && predicate.test(value) ? this : None;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof ${Type}Option other) {
            return this != None && o != None
                    && ${PrimitiveEquals("value", "other.value")};
        }

        if (o instanceof Option<?> other) {
            if (this.isEmpty()) return other.isEmpty();
            if (other.isEmpty()) return false;

            Object v = other.get();
            return v instanceof ${WrapperType}
                    && ${PrimitiveEquals("value", "(${WrapperType}) v")};
        }

        return false;
    }
    @Override
    public int hashCode() {
        return this.isDefined() ? HASH_MAGIC + ${WrapperType}.hashCode(value) : NONE_HASH;
    }

    @Override
    public String toString() {
        return this == None ? "${Type}Option.None" : "${Type}Option[" + value + "]";
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return isDefined() ? ${Type}Iterator.of(value) : ${Type}Iterator.empty();
    }

    @Override
    public void forEach(@NotNull ${Type}Consumer action) {
        if (isDefined()) {
            action.accept(value);
        }
    }

    private Object writeReplace() {
        return new Data(this == None ? null : value);
    }

    private static final class Data implements Serializable {
        @Serial
        private static final long serialVersionUID = -2044232156734869349L;
        private final ${WrapperType} value;

        Data(${WrapperType} value) {
            this.value = value;
        }

        @Serial
        Object readResolve() {
            return value == null ? None : some(value);
        }
    }
}
