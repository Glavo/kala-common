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
package kala.collection;

import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.collection.base.Traversable;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.*;
import kala.collection.mutable.MutableSeq;
import kala.function.CheckedConsumer;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.*;

public interface CollectionLike<E> extends Traversable<E>, AnyCollectionLike<E> {
    @Override
    default @NotNull String className() {
        return "CollectionLike";
    }

    @Override
    @NotNull CollectionView<E> view();

    @NotNull CollectionLike<E> filter(@NotNull Predicate<? super E> predicate);

    @NotNull CollectionLike<E> filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull CollectionLike<@NotNull E> filterNotNull();

    <U> @NotNull CollectionLike<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz);

    <U> @NotNull CollectionLike<U> map(@NotNull Function<? super E, ? extends U> mapper);

    <U> @NotNull CollectionLike<U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper);

    <U> @NotNull CollectionLike<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper);

    <U> @NotNull CollectionLike<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

    <U> @NotNull CollectionLike<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other);

    <U, R> @NotNull CollectionLike<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper);

    <U, V> @NotNull CollectionLike<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2);

    @NotNull
    CollectionLike<E> distinct();

    //region Copy Operations

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(@NotNull MutableSeq<? super E> dest) {
        return copyTo(dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(@NotNull MutableSeq<? super E> dest, int destPos) {
        return copyTo(dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(@NotNull MutableSeq<? super E> dest, int destPos, int limit) {
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        if (limit <= 0) {
            return 0;
        }

        final int dl = dest.size(); // implicit null check of dest
        if (destPos > dl) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        Iterator<E> it = this.iterator();

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest.set(idx++, it.next());
        }
        return idx - destPos;
    }

    //endregion

    @ApiStatus.NonExtendable
    default @NotNull ImmutableCollection<E> collect() {
        return toCollection();
    }

    default @NotNull ImmutableCollection<E> toCollection() {
        return ImmutableCollection.from(this);
    }

    default @NotNull ImmutableSeq<E> toSeq() {
        return ImmutableSeq.from(this);
    }

    default @NotNull ImmutableArray<E> toArraySeq() {
        @SuppressWarnings("unchecked")
        ImmutableArray<E> res = (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(toArray());
        return res;
    }

    default @NotNull ImmutableArray<E> toImmutableArray() { // TODO: Do we need to keep this method?
        return toArraySeq();
    }

    default @NotNull ImmutableSet<E> toSet() {
        return ImmutableSet.from(this);
    }

    default <K, V> @NotNull ImmutableMap<K, V> associate(@NotNull Function<? super E, ? extends java.util.Map.Entry<? extends K, ? extends V>> transform) {
        final int ks = knownSize();
        if (ks == 0) {
            return ImmutableMap.empty();
        }
        final Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return ImmutableMap.empty();
        }

        final var builder = ImmutableMap.<K, V>newMapBuilder();
        if (ks > 0) {
            builder.sizeHint(ks);
        }
        while (it.hasNext()) {
            final var v = transform.apply(it.next());
            builder.plusAssign(v.getKey(), v.getValue());
        }
        return builder.build();
    }

    @DelegateBy("associate(Function<E, Map.Entry<K, V>>)")
    default <K> @NotNull ImmutableMap<K, E> associateBy(@NotNull Function<? super E, ? extends K> keySelector) {
        return associate(value -> Tuple.of(keySelector.apply(value), value));
    }

    @DelegateBy("associate(Function<E, Map.Entry<K, V>>)")
    default <K, V> @NotNull ImmutableMap<K, V> associateBy(
            @NotNull Function<? super E, ? extends K> keySelector, @NotNull Function<? super E, ? extends V> valueTransform) {
        return associate(value -> Tuple.of(keySelector.apply(value), valueTransform.apply(value)));
    }

    //region Deprecated

    @Deprecated(forRemoval = true)
    @ReplaceWith("toSeq()")
    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return toSeq();
    }

    @Deprecated(forRemoval = true)
    @ReplaceWith("toSet()")
    default @NotNull ImmutableSet<E> toImmutableSet() {
        return toSet();
    }

    @Deprecated(forRemoval = true)
    default <K, V> @NotNull ImmutableMap<K, V> toImmutableMap(CollectionLike<E /* ? extends java.util.Map.Entry<? extends K, ? extends V> */>this) {
        final int ks = knownSize();
        if (ks == 0) {
            return ImmutableMap.empty();
        }
        final Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return ImmutableMap.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, ImmutableMap<K, V>> factory =
                (MapFactory) ImmutableMap.factory();
        final Object builder = factory.newBuilder();
        if (ks > 0) {
            factory.sizeHint(builder, ks);
        }
        while (it.hasNext()) {
            @SuppressWarnings("unchecked") final java.util.Map.Entry<K, V> v = (java.util.Map.Entry<K, V>) it.next();
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }
        return factory.build(builder);
    }

    //endregion

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull CollectionLike<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull CollectionLike<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull CollectionLike<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }
}
