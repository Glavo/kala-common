package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.*;
import org.glavo.kala.collection.mutable.Growable;
import org.glavo.kala.collection.mutable.MutableArray;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.tuple.Tuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public interface CollectionLike<E> extends Traversable<E> {

    default @NotNull String className() {
        return "CollectionLike";
    }

    @NotNull View<E> view();

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (E e : this) {
            if (predicate.test(e)) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (E e : this) {
            if (!predicate.test(e)) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Contract(value = "_ -> param1", mutates = "param1")
    default <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
        for (E e : this) {
            if (e != null) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
        for (E e : this) {
            destination.addValue(mapper.apply(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<@NotNull ? super U>> @NotNull G mapNotNullTo(
            @NotNull G destination,
            @NotNull Function<? super E, @Nullable ? extends U> mapper) {
        for (E e : this) {
            U u = mapper.apply(e);
            if (u != null) {
                destination.addValue(u);
            }
        }
        return destination;
    }

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
