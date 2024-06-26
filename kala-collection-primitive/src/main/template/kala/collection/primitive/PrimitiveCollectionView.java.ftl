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

import kala.collection.CollectionView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.primitive.internal.view.${Type}CollectionViews;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface ${Type}CollectionView extends ${Type}CollectionLike, PrimitiveCollectionView<${WrapperType}> {

    static @NotNull ${Type}CollectionView empty() {
        return ${Type}CollectionViews.EMPTY;
    }

    @Override
    default @NotNull String className() {
        return "${Type}CollectionView";
    }

    @Override
    default @NotNull ${Type}CollectionView view() {
        return this;
    }

    default @NotNull ${Type}CollectionView filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}CollectionViews.Filter(this, predicate);
    }

    default @NotNull ${Type}CollectionView filterNot(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}CollectionViews.FilterNot(this, predicate);
    }

    default @NotNull ${Type}CollectionView map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.Mapped(this, mapper);
    }

    default <U> @NotNull CollectionView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.MapToObj<>(this, mapper);
    }

    @Contract(pure = true)
    default @NotNull ${Type}CollectionView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.FlatMapped(this, mapper);
    }

    @Override
    default @NotNull <T> CollectionView<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.FlatMapToObj<>(this, mapper);
    }
}
