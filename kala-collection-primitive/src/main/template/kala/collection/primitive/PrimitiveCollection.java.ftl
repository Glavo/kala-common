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

import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.internal.view.${Type}CollectionViews;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.primitive.Immutable${Type}Collection;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}Collection extends PrimitiveCollection<${WrapperType}>, ${Type}CollectionLike {
    //region Static Factories

    static <E> @NotNull ${Type}CollectionFactory<?, ${Type}Collection> factory() {
        return ${Type}CollectionFactory.narrow(${Type}Seq.factory());
    }

    static @NotNull ${Type}Collection empty() {
        return ${Type}Seq.empty();
    }

    static @NotNull ${Type}Collection of() {
        return ${Type}Seq.of();
    }

    static @NotNull ${Type}Collection of(${PrimitiveType} value1) {
        return ${Type}Seq.of(value1);
    }

    static @NotNull ${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return ${Type}Seq.of(value1, value2);
    }

    static @NotNull ${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return ${Type}Seq.of(value1, value2, value3);
    }

    static @NotNull ${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return ${Type}Seq.of(value1, value2, value3, value4);
    }

    static @NotNull ${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return ${Type}Seq.of(value1, value2, value3, value4, value5);
    }

    static @NotNull ${Type}Collection of(${PrimitiveType}... values) {
        return ${Type}Seq.of(values);
    }

    static @NotNull ${Type}Collection from(${PrimitiveType} @NotNull [] values) {
        return ${Type}Seq.from(values);
    }

    static @NotNull ${Type}Collection from(@NotNull ${Type}Traversable values) {
        return ${Type}Seq.from(values);
    }

    static @NotNull ${Type}Collection from(@NotNull ${Type}Iterator it) {
        return ${Type}Seq.from(it);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "${Type}CollectionLike";
    }

    @Override
    default @NotNull ${Type}CollectionView view() {
        return isEmpty() ? ${Type}CollectionView.empty() : new ${Type}CollectionViews.Of(this);
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends ${Type}Collection> iterableFactory() {
        return ${Type}Collection.factory();
    }

    default @NotNull Immutable${Type}Collection filter(@NotNull ${Type}Predicate predicate) {
        return view().filter(predicate).toImmutableSeq();
    }

    default @NotNull Immutable${Type}Collection filterNot(@NotNull ${Type}Predicate predicate) {
        return view().filterNot(predicate).toImmutableSeq();
    }

    default @NotNull Immutable${Type}Collection map(@NotNull ${Type}UnaryOperator mapper) {
        return view().map(mapper).toImmutableSeq();
    }

    default <U> @NotNull ImmutableCollection<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        return view().<U>mapToObj(mapper).toImmutableSeq();
    }

    @Contract(pure = true)
    default @NotNull Immutable${Type}Collection flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        return view().flatMap(mapper).toImmutableSeq();
    }

    @Override
    default @NotNull <T> ImmutableCollection<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper) {
        return view().flatMapToObj(mapper).toImmutableSeq();
    }
}