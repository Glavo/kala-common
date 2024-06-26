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
package kala.collection.immutable.primitive;

import kala.annotations.StaticClass;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.mutable.primitive.Mutable${Type}ArrayList;
import kala.collection.primitive.${Type}ArraySeq;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
<#if IsSpecialized || Type == "Boolean">
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
</#if>

public final class Immutable${Type}Array extends ${Type}ArraySeq implements Immutable${Type}Seq, Serializable {
    private static final long serialVersionUID = 1845940935381169058L;

    public static final Immutable${Type}Array EMPTY = new Immutable${Type}Array(${Type}Arrays.EMPTY);

    private static final Factory FACTORY = new Factory();

    Immutable${Type}Array(${PrimitiveType} @NotNull [] elements) {
        super(elements);
    }

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, ? extends Immutable${Type}Array> factory() {
        return FACTORY;
    }

    public static @NotNull Immutable${Type}Array empty() {
        return EMPTY;
    }

    public static @NotNull Immutable${Type}Array of() {
        return empty();
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType} value1) {
        return new Immutable${Type}Array(new ${PrimitiveType}[]{value1});
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return new Immutable${Type}Array(new ${PrimitiveType}[]{value1, value2});
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return new Immutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3});
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return new Immutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3, value4});
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return new Immutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3, value4, value5});
    }

    @Contract(pure = true)
    public static @NotNull Immutable${Type}Array of(${PrimitiveType}... values) {
        return from(values);
    }

    @Contract(pure = true)
    public static @NotNull Immutable${Type}Array from(${PrimitiveType} @NotNull [] values) {
        return values.length == 0
                ? empty()
                : new Immutable${Type}Array(values.clone());
    }

    public static @NotNull Immutable${Type}Array from(@NotNull ${Type}Traversable values) {
        if (values instanceof Immutable${Type}Array) {
            return (Immutable${Type}Array) values;
        }

        if (values.isEmpty()) { // implicit null check of values
            return empty();
        }

        ${PrimitiveType}[] arr = values.toArray();
        return arr.length == 0 ? empty() : new Immutable${Type}Array(arr);
    }


    public static @NotNull Immutable${Type}Array from(@NotNull ${Type}Iterator it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new Immutable${Type}Array(it.toArray());
    }

    public static @NotNull Immutable${Type}Array fill(int n, ${PrimitiveType} value) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        if (value != ${Values.Default}) {
            Arrays.fill(ans, value);
        }
        return new Immutable${Type}Array(ans);
    }

    public static @NotNull Immutable${Type}Array fill(int n, @NotNull ${Type}Supplier supplier) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.getAs${Type}();
        }
        return new Immutable${Type}Array(ans);
    }

    @StaticClass
    public final static class Unsafe {
        private Unsafe() {
        }

        @Contract("_ -> new")
        public static @NotNull Immutable${Type}Array wrap(${PrimitiveType} @NotNull [] array) {
            Objects.requireNonNull(array);
            return new Immutable${Type}Array(array);
        }
    }

    //endregion

    ${PrimitiveType} @NotNull [] getArray() {
        return elements;
    }

    @Override
    public @NotNull String className() {
        return "Immutable${Type}Array";
    }
<#if IsSpecialized>

    @Override
    public @NotNull Spliterator.Of${Type} spliterator() {
        return Spliterators.spliterator(elements, Spliterator.IMMUTABLE);
    }
</#if>

    private static final class Factory implements ${Type}CollectionFactory<Mutable${Type}ArrayList, Immutable${Type}Array> {

        @Override
        public Immutable${Type}Array empty() {
            return Immutable${Type}Array.empty();
        }

        @Override
        public Immutable${Type}Array from(${PrimitiveType} @NotNull [] values) {
            return Immutable${Type}Array.from(values);
        }

        @Override
        public Immutable${Type}Array from(@NotNull ${Type}Traversable values) {
            return Immutable${Type}Array.from(values);
        }

        @Override
        public Immutable${Type}Array from(@NotNull ${Type}Iterator it) {
            return Immutable${Type}Array.from(it);
        }

        @Override
        public Immutable${Type}Array fill(int n, ${PrimitiveType} value) {
            return Immutable${Type}Array.fill(n, value);
        }

        @Override
        public Immutable${Type}Array fill(int n, @NotNull ${Type}Supplier supplier) {
            return Immutable${Type}Array.fill(n, supplier);
        }

        /*
        @Override
        public Immutable${Type}Array fill(int n, @NotNull IntTo${Type}Function init) {
            return Immutable${Type}Array.fill(n, init);
        }
         */

        @Override
        public Mutable${Type}ArrayList newBuilder() {
            return new Mutable${Type}ArrayList();
        }

        @Override
        public void addToBuilder(@NotNull Mutable${Type}ArrayList buffer, ${PrimitiveType} value) {
            buffer.append(value);
        }

        @Override
        public Mutable${Type}ArrayList mergeBuilder(@NotNull Mutable${Type}ArrayList builder1, @NotNull Mutable${Type}ArrayList builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public void sizeHint(@NotNull Mutable${Type}ArrayList builder, int size) {
            builder.sizeHint(size);
        }

        @Override
        public Immutable${Type}Array build(@NotNull Mutable${Type}ArrayList buffer) {
            return new Immutable${Type}Array(buffer.toArray());
        }
    }
}
