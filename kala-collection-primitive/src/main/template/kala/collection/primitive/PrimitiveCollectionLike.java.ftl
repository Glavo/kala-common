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

import kala.collection.CollectionLike;
import kala.function.*;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.immutable.primitive.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}CollectionLike extends PrimitiveCollectionLike<${WrapperType}>, ${Type}Traversable {
    @Override
    default @NotNull String className() {
        return "${Type}CollectionLike";
    }

    @Override
    @NotNull ${Type}CollectionView view();

    @NotNull ${Type}CollectionLike filter(@NotNull ${Type}Predicate predicate);

    @NotNull ${Type}CollectionLike filterNot(@NotNull ${Type}Predicate predicate);

    @NotNull ${Type}CollectionLike map(@NotNull ${Type}UnaryOperator mapper);

    <U> @NotNull CollectionLike<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper);

    @Contract(pure = true)
    @NotNull ${Type}CollectionLike flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper);

    @Contract(pure = true)
    <T> @NotNull CollectionLike<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper);

    default @NotNull Immutable${Type}Seq toSeq() {
        return Immutable${Type}Seq.from(this);
    }

    @Deprecated(forRemoval = true)
    default @NotNull Immutable${Type}Seq toImmutableSeq() {
        return toSeq();
    }

    default @NotNull Immutable${Type}Array toImmutableArray() {
        return Immutable${Type}Array.from(this);
    }
}
