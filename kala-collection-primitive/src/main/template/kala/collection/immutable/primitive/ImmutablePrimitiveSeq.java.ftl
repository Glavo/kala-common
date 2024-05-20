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

import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface Immutable${Type}Seq extends ImmutablePrimitiveSeq<${WrapperType}>, ${Type}Seq, Immutable${Type}Collection {
    
    //region Static Factories

    static <E> @NotNull ${Type}CollectionFactory<?, Immutable${Type}Seq> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Array.factory());
    }

    static @NotNull Immutable${Type}Seq empty() {
        return Immutable${Type}Array.empty();
    }

    static @NotNull Immutable${Type}Seq of() {
        return Immutable${Type}Array.of();
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1) {
        return Immutable${Type}Array.of(value1);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Array.of(value1, value2);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Array.of(value1, value2, value3);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Array.of(value1, value2, value3, value4);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Array.of(value1, value2, value3, value4, value5);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType}... values) {
        return Immutable${Type}Array.of(values);
    }

    static @NotNull Immutable${Type}Seq from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Array.from(values);
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Array.from(values);
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Array.from(it);
    }

<#if IsSpecialized>
    /*
    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Stream stream) {
        return Immutable${Type}Array.from(stream);
    }
     */

</#if>
    static @NotNull Immutable${Type}Seq fill(int n, ${PrimitiveType} value) {
        return Immutable${Type}Array.fill(n, value);
    }

    static @NotNull Immutable${Type}Seq fill(int n, @NotNull ${Type}Supplier supplier) {
        return Immutable${Type}Array.fill(n, supplier);
    }

    /*
    static @NotNull Immutable${Type}Seq fill(int n, @NotNull IntTo${Type}Function init) {
        return Immutable${Type}Array.fill(n, init);
    }
     */

    //endregion

    @Override
    default @NotNull String className() {
        return "Immutable${Type}Seq";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Immutable${Type}Seq> iterableFactory() {
        return Immutable${Type}Seq.factory();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq slice(int beginIndex, int endIndex) {
        return AbstractImmutable${Type}Seq.slice(this, beginIndex, endIndex, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq drop(int n) {
        return AbstractImmutable${Type}Seq.drop(this, n, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropLast(int n) {
        return AbstractImmutable${Type}Seq.dropLast(this, n, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropWhile(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Seq.dropWhile(this, predicate, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq take(int n) {
        return AbstractImmutable${Type}Seq.take(this, n, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeLast(int n) {
        return AbstractImmutable${Type}Seq.takeLast(this, n, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeWhile(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Seq.takeWhile(this, predicate, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq updated(int index, ${PrimitiveType} newValue) {
        return AbstractImmutable${Type}Seq.updated(this, index, newValue, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq concat(@NotNull ${Type}SeqLike other) {
        return AbstractImmutable${Type}Seq.concat(this, other, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prepended(${PrimitiveType} value) {
        return AbstractImmutable${Type}Seq.prepended(this, value, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(${PrimitiveType} @NotNull [] values) {
        return AbstractImmutable${Type}Seq.prependedAll(this, values, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(@NotNull ${Type}Traversable values) {
        return AbstractImmutable${Type}Seq.prependedAll(this, values, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appended(${PrimitiveType} value) {
        return AbstractImmutable${Type}Seq.appended(this, value, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(@NotNull ${Type}Traversable values) {
        return AbstractImmutable${Type}Seq.appendedAll(this, values, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(${PrimitiveType} @NotNull [] values) {
        return AbstractImmutable${Type}Seq.appendedAll(this, values, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq sorted() {
        return AbstractImmutable${Type}Seq.sorted(this, iterableFactory());
    }


    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq reversed() {
        return AbstractImmutable${Type}Seq.reversed(this, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filter(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filter(this, predicate, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filterNot(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filterNot(this, predicate, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq map(@NotNull ${Type}UnaryOperator mapper) {
        return AbstractImmutable${Type}Collection.map(this, mapper, iterableFactory());
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        return ${Type}Seq.super.mapToObj(mapper);
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        return AbstractImmutable${Type}Collection.flatMap(this, mapper, iterableFactory());
    }

    @Override
    default @NotNull Immutable${Type}Seq toImmutableSeq() {
        return this;
    }
}
