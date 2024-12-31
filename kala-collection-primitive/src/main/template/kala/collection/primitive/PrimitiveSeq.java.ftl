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
package kala.collection.primitive;

import kala.collection.AnySeq;
import kala.collection.SeqIterator;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.internal.view.${Type}SeqViews;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.immutable.primitive.Immutable${Type}Seq;
import kala.function.*;
import kala.index.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}Seq extends PrimitiveSeq<${WrapperType}>, ${Type}Collection, ${Type}SeqLike {
    //region Static Factories

    static <E> @NotNull ${Type}CollectionFactory<?, ${Type}Seq> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Seq.factory());
    }

    static @NotNull ${Type}Seq empty() {
        return Immutable${Type}Seq.empty();
    }

    static @NotNull ${Type}Seq of() {
        return Immutable${Type}Seq.of();
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1) {
        return Immutable${Type}Seq.of(value1);
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Seq.of(value1, value2);
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Seq.of(value1, value2, value3);
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Seq.of(value1, value2, value3, value4);
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Seq.of(value1, value2, value3, value4, value5);
    }

    static @NotNull ${Type}Seq of(${PrimitiveType}... values) {
        return Immutable${Type}Seq.of(values);
    }

    static @NotNull ${Type}Seq from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Seq.from(values);
    }

    static @NotNull ${Type}Seq from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Seq.from(values);
    }

    static @NotNull ${Type}Seq from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Seq.from(it);
    }

    static @NotNull ${Type}Seq fill(int n, ${PrimitiveType} value) {
        return Immutable${Type}Seq.fill(n, value);
    }

    static @NotNull ${Type}Seq fill(int n, @NotNull ${Type}Supplier supplier) {
        return Immutable${Type}Seq.fill(n, supplier);
    }
    /*
    static @NotNull ${Type}Seq fill(int n, @NotNull IntTo${Type}Function init) {
        return Immutable${Type}Seq.fill(n, init);
    }
    */

    //endregion

    static int hashCode(@NotNull ${Type}Seq seq) {
        return seq.iterator().hash() + SEQ_HASH_MAGIC;
    }

    static boolean equals(@NotNull ${Type}Seq seq1, @NotNull AnySeq<?> seq2) {
        if (seq1 == seq2) return true;
        if (!seq1.canEqual(seq2) || !seq2.canEqual(seq1)) return false;

        if (seq2 instanceof ${Type}Seq) {
            return seq1.sameElements(((${Type}Seq)seq2));
        } else {
            return seq1.sameElements(seq2.asGeneric());
        }
    }

    @Override
    default @NotNull String className() {
        return "${Type}Seq";
    }

    @Override
    default @NotNull ${Type}SeqView view() {
        return isEmpty() ? ${Type}SeqView.empty() : new ${Type}SeqViews.Of(this);
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends ${Type}Seq> iterableFactory() {
        return ${Type}Seq.factory();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq slice(@Index int beginIndex, @Index int endIndex) {
        return view().slice(beginIndex, endIndex).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq drop(int n) {
        return view().drop(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropLast(int n) {
        return view().dropLast(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropWhile(@NotNull ${Type}Predicate predicate) {
        return view().dropWhile(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq take(int n) {
        return view().take(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeLast(int n) {
        return view().takeLast(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeWhile(@NotNull ${Type}Predicate predicate) {
        return view().takeWhile(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq updated(@Index int index, ${PrimitiveType} newValue) {
        return view().updated(index, newValue).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq concat(@NotNull ${Type}SeqLike other) {
        return view().concat(other).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prepended(${PrimitiveType} value) {
        return view().prepended(value).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(${PrimitiveType} @NotNull [] values) {
        return view().prependedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(@NotNull ${Type}Traversable values) {
        return view().prependedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appended(${PrimitiveType} value) {
        return view().appended(value).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(@NotNull ${Type}Traversable values) {
        return view().appendedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(${PrimitiveType} @NotNull [] values) {
        return view().appendedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq sorted() {
        return view().sorted().toImmutableSeq();
    }


    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq reversed() {
        return view().reversed().toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filter(@NotNull ${Type}Predicate predicate) {
        return view().filter(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filterNot(@NotNull ${Type}Predicate predicate) {
        return view().filterNot(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq map(@NotNull ${Type}UnaryOperator mapper) {
        return view().map(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        return view().<U>mapToObj(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        return view().flatMap(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <T> @NotNull ImmutableSeq<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper) {
        return view().flatMapToObj(mapper).toImmutableSeq();
    }
}
