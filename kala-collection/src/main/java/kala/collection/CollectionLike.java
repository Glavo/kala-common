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

import kala.collection.base.Traversable;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.*;
import kala.collection.mutable.MutableSeq;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.intellij.lang.annotations.Flow;
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
    @NotNull
    CollectionView<E> view();

    @NotNull
    CollectionLike<E> filter(@NotNull Predicate<? super E> predicate);

    @NotNull
    CollectionLike<E> filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull
    CollectionLike<@NotNull E> filterNotNull();

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

    default @NotNull Seq<E> toSeq() {
        return toImmutableSeq();
    }

    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return toImmutableVector();
    }

    default @NotNull ImmutableArray<E> toImmutableArray() {
        return ImmutableArray.Unsafe.wrap(toArray());
    }

    default @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
        return ImmutableLinkedSeq.from(this);
    }

    default @NotNull ImmutableVector<E> toImmutableVector() {
        return ImmutableVector.from(this);
    }

    default @NotNull Set<E> toSet() {
        return toImmutableSet();
    }

    default @NotNull ImmutableSet<E> toImmutableSet() {
        return ImmutableSet.from(this);
    }

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

    default <K, V> @NotNull Map<K, V> associate(@NotNull Function<? super E, ? extends java.util.Map.Entry<? extends K, ? extends V>> transform) {
        final int ks = knownSize();
        if (ks == 0) {
            return Map.empty();
        }
        final Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, Map<K, V>> factory =
                (MapFactory) Map.factory();
        final Object builder = factory.newBuilder();
        if (ks > 0) {
            factory.sizeHint(builder, ks);
        }
        while (it.hasNext()) {
            final java.util.Map.Entry<? extends K, ? extends V> v = transform.apply(it.next());
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }
        return factory.build(builder);
    }

    default <K> @NotNull Map<K, E> associateBy(@NotNull Function<? super E, ? extends K> keySelector) {
        final int ks = knownSize();
        if (ks == 0) {
            return Map.empty();
        }
        final Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, E, Object, Map<K, E>> factory =
                (MapFactory) Map.factory();
        final Object builder = factory.newBuilder();
        if (ks > 0) {
            factory.sizeHint(builder, ks);
        }
        while (it.hasNext()) {
            final E e = it.next();
            factory.addToBuilder(builder, keySelector.apply(e), e);
        }
        return factory.build(builder);
    }

    default <K, V> @NotNull Map<K, V> associateBy(
            @NotNull Function<? super E, ? extends K> keySelector, @NotNull Function<? super E, ? extends V> valueTransform) {
        final int ks = knownSize();
        if (ks == 0) {
            return Map.empty();
        }
        final Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, Map<K, V>> factory =
                (MapFactory) Map.factory();
        final Object builder = factory.newBuilder();
        if (ks > 0) {
            factory.sizeHint(builder, ks);
        }
        while (it.hasNext()) {
            final E e = it.next();
            factory.addToBuilder(builder, keySelector.apply(e), valueTransform.apply(e));
        }
        return factory.build(builder);
    }

}
