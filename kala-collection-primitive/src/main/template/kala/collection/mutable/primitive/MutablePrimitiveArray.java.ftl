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
package kala.collection.mutable.primitive;

import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.${Type}ArraySeq;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.*;

public final class Mutable${Type}Array extends ${Type}ArraySeq implements Mutable${Type}Seq, Serializable {
    private static final long serialVersionUID = 8060307722127719792L;

    public static final Mutable${Type}Array EMPTY = new Mutable${Type}Array(${Type}Arrays.EMPTY);

    private static final Factory FACTORY = new Factory();

    //region Constructors

    Mutable${Type}Array(${PrimitiveType} @NotNull [] array) {
        super(array);
    }

    //endregion

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, Mutable${Type}Array> factory() {
        return FACTORY;
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}Array create(int size) {
        return new Mutable${Type}Array(new ${PrimitiveType}[size]);
    }

    public static @NotNull Mutable${Type}Array empty() {
        return EMPTY;
    }

    public static @NotNull Mutable${Type}Array of() {
        return EMPTY;
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}Array of(${PrimitiveType} value1) {
        return new Mutable${Type}Array(new ${PrimitiveType}[]{value1});
    }

    @Contract("_, _ -> new")
    public static @NotNull Mutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return new Mutable${Type}Array(new ${PrimitiveType}[]{value1, value2});
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Mutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return new Mutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3});
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Mutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return new Mutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3, value4});
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull Mutable${Type}Array of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return new Mutable${Type}Array(new ${PrimitiveType}[]{value1, value2, value3, value4, value5});
    }

    public static @NotNull Mutable${Type}Array of(${PrimitiveType}... values) {
        return values.length != 0 ? new Mutable${Type}Array(values) : empty();
    }

    public static @NotNull Mutable${Type}Array from(${PrimitiveType} @NotNull [] values) {
        return values.length != 0 // implicit null check of values
                ? new Mutable${Type}Array(values.clone()) : empty();
    }

    public static @NotNull Mutable${Type}Array from(@NotNull ${Type}Traversable values) {
        return values.isNotEmpty()  // implicit null check of values
                ? new Mutable${Type}Array(values.toArray()) : empty();

    }

    public static @NotNull Mutable${Type}Array from(@NotNull ${Type}Iterator it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }

        return new Mutable${Type}Array(it.toArray());
    }

    public static @NotNull Mutable${Type}Array fill(int n, ${PrimitiveType} value) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        if (value != ${Values.Default}) {
            Arrays.fill(ans, value);
        }
        return new Mutable${Type}Array(ans);
    }

    public static @NotNull Mutable${Type}Array fill(int n, @NotNull ${Type}Supplier supplier) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.getAs${Type}();
        }
        return new Mutable${Type}Array(ans);
    }

    public static @NotNull Mutable${Type}Array wrap(${PrimitiveType} @NotNull [] array) {
        Objects.requireNonNull(array);
        return new Mutable${Type}Array(array);
    }

    //endregion

    @Override
    public final @NotNull String className() {
        return "MutableArray";
    }

    @Override
    public @NotNull ${Type}CollectionFactory<?, Mutable${Type}Array> iterableFactory() {
        return factory();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull Mutable${Type}Array clone() {
        return new Mutable${Type}Array(this.elements.clone());
    }

    public ${PrimitiveType} @NotNull [] getArray() {
        return elements;
    }

    @Override
    public void set(@Index int index, ${PrimitiveType} newValue) {
        elements[Indexes.checkIndex(index, elements.length)] = newValue;
    }

    @Override
    public void replaceAll(@NotNull ${Type}UnaryOperator operator) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = operator.applyAs${Type}(elements[i]);
        }
    }

    @Override
    public void sort() {
        ${Type}Arrays.sort(elements);
    }

    private static final class Factory implements ${Type}CollectionFactory<Mutable${Type}ArrayList, Mutable${Type}Array> {
        Factory() {
        }

        @Override
        public Mutable${Type}Array from(${PrimitiveType} @NotNull [] values) {
            return Mutable${Type}Array.from(values);
        }

        @Override
        public Mutable${Type}Array from(@NotNull ${Type}Traversable values) {
            return Mutable${Type}Array.from(values);
        }

        @Override
        public @NotNull Mutable${Type}Array from(@NotNull ${Type}Iterator it) {
            return Mutable${Type}Array.from(it);
        }

        @Override
        public Mutable${Type}Array fill(int n, ${PrimitiveType} value) {
            return Mutable${Type}Array.fill(n, value);
        }

        @Override
        public Mutable${Type}Array fill(int n, @NotNull ${Type}Supplier supplier) {
            return Mutable${Type}Array.fill(n, supplier);
        }

        @Override
        public Mutable${Type}ArrayList newBuilder() {
            return new Mutable${Type}ArrayList();
        }

        @Override
        public void addToBuilder(@NotNull Mutable${Type}ArrayList builder, ${PrimitiveType} value) {
            builder.append(value);
        }

        @Override
        public void sizeHint(@NotNull Mutable${Type}ArrayList builder, int size) {
            builder.sizeHint(size);
        }

        @Override
        public Mutable${Type}ArrayList mergeBuilder(@NotNull Mutable${Type}ArrayList builder1, @NotNull Mutable${Type}ArrayList builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public Mutable${Type}Array build(@NotNull Mutable${Type}ArrayList builder) {
            return new Mutable${Type}Array(builder.toArray());
        }
    }
}
