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

import kala.Conditions;
import kala.annotations.DelegateBy;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.${Type}Seq;
import kala.collection.primitive.internal.${Type}SeqIterators;
import kala.comparator.primitive.${Type}Comparator;
import kala.function.*;
import kala.index.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.random.RandomGenerator;

public interface Mutable${Type}Seq extends MutablePrimitiveSeq<${WrapperType}>, ${Type}Seq, Mutable${Type}Collection {

    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}Seq> factory() {
        return ${Type}CollectionFactory.narrow(Mutable${Type}Array.factory());
    }

    static @NotNull Mutable${Type}Seq of() {
        return Mutable${Type}Array.of();
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1) {
        return Mutable${Type}Array.of(value1);
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Mutable${Type}Array.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Mutable${Type}Array.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Mutable${Type}Array.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Mutable${Type}Array.of(value1, value2, value3, value4, value5);
    }

    static @NotNull Mutable${Type}Seq of(${PrimitiveType}...values) {
        return Mutable${Type}Array.of(values);
    }

    static @NotNull Mutable${Type}Seq from(${PrimitiveType} @NotNull []values) {
        return Mutable${Type}Array.from(values);
    }

    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Traversable values) {
        return Mutable${Type}Array.from(values);
    }

    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Iterator it) {
        return Mutable${Type}Array.from(it);
    }
<#if IsSpecialized>

    /*
    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Stream stream) {
        return Mutable${Type}Array.from(stream);
    }
     */
</#if>

    //endregion

    @Override
    default @NotNull String className() {
        return "Mutable${Type}Seq";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}Seq> iterableFactory() {
        return Mutable${Type}Seq.factory();
    }

    @Override
    default @NotNull Mutable${Type}SeqIterator seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull Mutable${Type}SeqIterator seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new ${Type}SeqIterators.DefaultMutable${Type}SeqIterator<>(this, index);
    }

    @Contract(mutates = "this")
    void set(@Index int index, ${PrimitiveType} newValue);

    default void swap(int index1, int index2) {
        final ${PrimitiveType} old1 = this.get(index1);
        final ${PrimitiveType} old2 = this.get(index2);

        this.set(index1, old2);
        this.set(index2, old1);
    }

    @Contract(mutates = "this")
    default void replaceAll(@NotNull ${Type}UnaryOperator operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.applyAs${Type}(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void replaceAllIndexed(@NotNull Indexed${Type}UnaryOperator operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.applyAs${Type}(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void sort() {
<#if Type == "Boolean">
        final int size = this.size();
        int trueCount = this.count(it -> it);
        int falseCount = size - trueCount;

        if (trueCount == 0 || falseCount == 0) return;

        for (int i = 0; i < falseCount; i++) {
            this.set(i, false);
        }

        for (int i = falseCount; i < size; i++) {
            this.set(i, true);
        }
<#else>
        ${PrimitiveType}[] values = toArray();
        Arrays.sort(values);

        for (int i = 0; i < values.length; i++) {
            this.set(i, values[i]);
        }
</#if>
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size / 2; i++) {
            swap(i, size - i - 1);
        }
    }

    @DelegateBy("shuffle(RandomGenerator)")
    default void shuffle() {
        shuffle(ThreadLocalRandom.current());
    }

    default void shuffle(@NotNull RandomGenerator random) {
        int ks = this.knownSize();
        if (ks == 0 || ks == 1) {
            return;
        }
        if (supportsFastRandomAccess() || (ks > 0 && ks <= 5)) { // TODO: SHUFFLE_THRESHOLD
            assert ks > 0;
            for (int i = ks; i > 1; i--) {
                swap(i - 1, random.nextInt(i));
            }
        } else {
            final ${PrimitiveType}[] arr = this.toArray();
            ${Type}Arrays.shuffle(arr, random);
            this.replaceAllIndexed((i, v) -> arr[i]);
        }
    }
}
