package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.*;
import org.glavo.kala.collection.mutable.MutableArray;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.tuple.Tuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface CollectionLike<E> extends Traversable<E> {

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

    default @NotNull ImmutableVector<E> toImmutableVector() {
        return ImmutableVector.from(this);
    }

    default <K, V> @NotNull Map<K, V> associate(@NotNull Function<? super E, ? extends Tuple2<? extends K, ? extends V>> transform) {
        throw new UnsupportedOperationException(); // TODO
    }

}
