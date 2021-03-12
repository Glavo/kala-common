package org.glavo.kala.collection;

import org.glavo.kala.collection.base.GenericArrays;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.glavo.kala.collection.mutable.Growable;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.CheckedIndexedConsumer;
import org.glavo.kala.function.IndexedBiFunction;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.function.Predicate;

public interface SeqLike<E> extends CollectionLike<E> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> SeqLike<E> narrow(SeqLike<? extends E> view) {
        return (SeqLike<E>) view;
    }

    @Override
    default @NotNull String className() {
        return "SeqLike";
    }

    @Override
    @NotNull SeqView<E> view();

    default @NotNull SeqView<E> sliceView(int beginIndex, int endIndex) {
        return view().slice(beginIndex, endIndex);
    }

    //region Positional Access Operations

    @Contract(pure = true)
    default boolean isDefinedAt(int index) {
        return index >= 0 && sizeGreaterThan(index);
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true)
    default E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getOption(index).getOrThrow(IndexOutOfBoundsException::new);
    }

    @Contract(pure = true)
    default @Nullable E getOrNull(int index) {
        return getOption(index).getOrNull();
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, targetIsContainer = true)
    default @NotNull Option<E> getOption(int index) {
        if (index < 0) {
            return Option.none();
        }

        int s = knownSize();
        if (s >= 0 && index >= s) {
            return Option.none();
        }

        int i = index;
        for (E e : this) {
            if (i-- == 0) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    //endregion

    //region Reversal Operations

    default @NotNull Iterator<E> reverseIterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Iterators.empty();
        }
        Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return it;
        }
        ArrayBuffer<E> buffer = ks > 0
                ? new ArrayBuffer<>(ks)
                : new ArrayBuffer<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }

        @SuppressWarnings("unchecked")
        Iterator<E> res = (Iterator<E>) GenericArrays.reverseIterator(buffer.toArray());
        return res;
    }

    //endregion

    //region Element Retrieval Operations

    default E first() {
        return iterator().next();
    }

    default E last() {
        return reverseIterator().next();
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    default int indexOf(Object value) {
        int idx = 0;
        if (value == null) {
            for (E e : this) {
                if (null == e) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (E e : this) {
                if (value.equals(e)) {
                    return idx;
                }
                ++idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexOf(Object value, int from) {
        int idx = 0;
        if (value == null) {
            for (E e : this) {
                if (idx >= from && null == e) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (E e : this) {
                if (idx >= from && value.equals(e)) {
                    return idx;
                }
                ++idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super E> predicate) {
        int idx = 0;
        for (E e : this) {
            if (predicate.test(e)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        int idx = 0;
        for (E e : this) {
            if (idx >= from && predicate.test(e)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(Object value) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();

        if (value == null) {
            while (it.hasNext()) {
                if (null == it.next()) {
                    return idx;
                }
                --idx;
            }
        } else {
            while (it.hasNext()) {
                if (value.equals(it.next())) {
                    return idx;
                }
                --idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(Object value, int end) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();

        if (value == null) {
            while (it.hasNext()) {
                if (idx <= end && null == it.next()) {
                    return idx;
                }
                --idx;
            }
        } else {
            while (it.hasNext()) {
                if (idx <= end && value.equals(it.next())) {
                    return idx;
                }
                --idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();
        while (it.hasNext()) {
            if (idx <= end && predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    //endregion

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(
            @NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        int idx = 0;
        for (E e : this) {
            destination.plusAssign(mapper.apply(idx++, e));
        }
        return destination;
    }

    default @NotNull SeqView<IntObjTuple2<E>> withIndex() {
        return view().withIndex();
    }

    //region Aggregate Operations

    default E foldIndexed(E zero, @NotNull IndexedBiFunction<? super E, ? super E, ? extends E> op) {
        return foldLeftIndexed(zero, op);
    }

    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
        return Iterators.foldLeftIndexed(this.iterator(), zero, op);
    }

    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
        return Iterators.foldRightIndexed(this.iterator(), zero, op);
    }

    //endregion

    //region Traversable Operations

    default void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        int idx = 0;
        for (E e : this) {
            action.accept(idx++, e); // implicit null check of action
        }
    }

    default <Ex extends Throwable> void forEachIndexedChecked(
            @NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
        forEachIndexed(action);
    }

    default void forEachIndexedUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
        forEachIndexed(action);
    }

    //endregion
}
