package org.glavo.kala.collection;

import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.immutable.*;
import org.glavo.kala.collection.base.Growable;
import org.glavo.kala.collection.mutable.MutableArray;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.tuple.Tuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public interface CollectionLike<E> extends Traversable<E> {

    default @NotNull String className() {
        return "CollectionLike";
    }

    @NotNull View<E> view();

    //region Copy Operations

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(@NotNull MutableArray<? super E> dest) {
        return copyToArray(0, dest.getArray(), 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(@NotNull MutableArray<? super E> dest, int destPos) {
        return copyToArray(0, dest.getArray(), destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(@NotNull MutableArray<? super E> dest, int destPos, int limit) {
        return copyToArray(0, dest.getArray(), destPos, limit);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest) {
        return copyToArray(srcPos, dest.getArray(), 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest, int destPos) {
        return copyToArray(srcPos, dest.getArray(), destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest, int destPos, int limit) {
        return copyToArray(srcPos, dest.getArray(), destPos, limit);
    }

    //endregion

    default @NotNull Seq<E> toSeq() {
        return toImmutableSeq();
    }

    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return toImmutableVector();
    }

    @SuppressWarnings("unchecked")
    default @NotNull ImmutableArray<E> toImmutableArray() {
        return (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(toArray());
    }

    default @NotNull ImmutableList<E> toImmutableList() {
        return ImmutableList.from(this);
    }

    default @NotNull ImmutableSizedList<E> toImmutableSizedList() {
        return ImmutableSizedList.from(this);
    }

    default @NotNull ImmutableVector<E> toImmutableVector() {
        return ImmutableVector.from(this);
    }

    default <K, V> @NotNull ImmutableMap<K, V> toImmutableMap() {
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
