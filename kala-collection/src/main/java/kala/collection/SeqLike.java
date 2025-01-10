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

import kala.Conditions;
import kala.annotations.DelegateBy;
import kala.collection.base.Growable;
import kala.collection.base.OrderedTraversable;
import kala.collection.internal.SeqIterators;
import kala.collection.internal.view.SeqViews;
import kala.collection.mutable.MutableSeq;
import kala.control.Option;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedFunction;
import kala.index.Index;
import kala.index.Indexes;
import kala.tuple.Tuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface SeqLike<E> extends CollectionLike<E>, AnySeqLike<E>, OrderedTraversable<E> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> SeqLike<E> narrow(SeqLike<? extends E> view) {
        return (SeqLike<E>) view;
    }

    @Override
    default @NotNull String className() {
        return "SeqLike";
    }

    @DelegateBy("seqIterator(int)")
    default @NotNull SeqIterator<E> seqIterator() {
        return seqIterator(0);
    }

    default @NotNull SeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultSeqIterator<>(this, index);
    }

    @Override
    @NotNull SeqView<E> view();

    default @NotNull SeqView<E> sliceView(@Index int beginIndex, @Index int endIndex) {
        return view().slice(beginIndex, endIndex);
    }

    //region Positional Access Operations

    @Override
    default E elementAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index);
        }
        return get(index);
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true)
    default E get(@Index int index) {
        Iterator<E> it = iterator(index);
        if (!it.hasNext()) throw Indexes.outOfBounds(index);

        return it.next();
    }

    @Contract(pure = true)
    @DelegateBy("get(int)")
    default @Nullable E getOrNull(@Index int index) {
        return isDefinedAt(index) ? get(index) : null;
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, targetIsContainer = true)
    @DelegateBy("get(int)")
    default @NotNull Option<E> getOption(@Index int index) {
        return isDefinedAt(index) ? Option.some(get(index)) : Option.none();
    }

    //endregion

    @Contract(pure = true)
    @DelegateBy("binarySearch(int, int, E)")
    default int binarySearch(E value) {
        return binarySearch(0, ~0, value);
    }

    @Contract(pure = true)
    @DelegateBy("binarySearch(int, int, E, Comparator<E>)")
    default int binarySearch(E value, Comparator<? super E> comparator) {
        return binarySearch(0, ~0, value, comparator);
    }

    @Contract(pure = true)
    @DelegateBy("binarySearch(int, int, E, Comparator<E>)")
    default int binarySearch(@Index int beginIndex, @Index int endIndex, E value) {
        return binarySearch(beginIndex, endIndex, value, null);
    }

    @Contract(pure = true)
    default int binarySearch(@Index int beginIndex, @Index int endIndex, E value, Comparator<? super E> comparator) {
        if (comparator == null) {
            @SuppressWarnings("unchecked")
            Comparator<E> c = (Comparator<E>) Comparator.naturalOrder();
            comparator = c;
        }

        final int size = size();

        int low = Indexes.checkBeginIndex(beginIndex, size);
        int high = Indexes.checkEndIndex(low, endIndex, size) - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final E midVal = get(mid);
            final int cmp = comparator.compare(midVal, value);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    @Contract(pure = true)
    @NotNull SeqLike<E> slice(@Index int beginIndex, @Index int endIndex);

    @Contract(pure = true)
    @NotNull SeqLike<E> drop(int n);

    @Contract(pure = true)
    @NotNull SeqLike<E> dropLast(int n);

    @Contract(pure = true)
    @NotNull SeqLike<E> dropWhile(@NotNull Predicate<? super E> predicate);

    @Contract(pure = true)
    @NotNull SeqLike<E> take(int n);

    @NotNull SeqLike<E> takeLast(int n);

    @Contract(pure = true)
    @NotNull SeqLike<E> takeWhile(@NotNull Predicate<? super E> predicate);

    @Contract(pure = true)
    @NotNull SeqLike<E> updated(@Index int index, E newValue);

    @Contract(pure = true)
    @NotNull SeqLike<E> concat(@NotNull SeqLike<? extends E> other);

    @Contract(pure = true)
    @NotNull SeqLike<E> concat(@NotNull List<? extends E> other);

    @Contract(pure = true)
    @NotNull SeqLike<E> prepended(E value);

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @NotNull SeqLike<E> prependedAll(E... values);

    @Contract(pure = true)
    @NotNull SeqLike<E> prependedAll(@NotNull Iterable<? extends E> values);

    @Contract(pure = true)
    @NotNull SeqLike<E> appended(E value);

    @Contract(pure = true)
    @NotNull SeqLike<E> appendedAll(@NotNull Iterable<? extends E> values);

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @NotNull SeqLike<E> appendedAll(E... values);

    @Contract(pure = true)
    @NotNull SeqLike<E> inserted(@Index int index, E value);

    @Contract(pure = true)
    @NotNull SeqLike<E> removedAt(@Index int index);

    @Contract(pure = true)
    @NotNull SeqLike<E> sorted();

    @Contract(pure = true)
    @NotNull SeqLike<E> sorted(@Nullable Comparator<? super E> comparator);

    @Contract(pure = true)
    @NotNull SeqLike<E> reversed();

    @Override
    @Contract(pure = true)
    @NotNull SeqLike<E> filter(@NotNull Predicate<? super E> predicate);

    @Override
    @Contract(pure = true)
    @NotNull SeqLike<E> filterNot(@NotNull Predicate<? super E> predicate);

    @Override
    @Contract(pure = true)
    @NotNull SeqLike<@NotNull E> filterNotNull();

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqLike<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz);

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqLike<U> map(@NotNull Function<? super E, ? extends U> mapper);

    @Contract(pure = true)
    <U> @NotNull SeqLike<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper);

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqLike<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper);

    @Contract(pure = true)
    <U> @NotNull SeqLike<@NotNull U> mapIndexedNotNull(
            @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper);

    @Override
    @Contract(pure = true)
    @NotNull <U> SeqLike<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper);

    @Contract(pure = true)
    @NotNull <U> SeqLike<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper);

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqLike<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(
            @NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        int idx = 0;
        for (E e : this) {
            destination.plusAssign(mapper.apply(idx++, e));
        }
        return destination;
    }

    default <U> @NotNull SeqView<@NotNull Tuple2<E, U>> zipView(@NotNull SeqLike<? extends U> other) {
        return new SeqViews.Zip<>(this, other);
    }

    @Override
    default int copyTo(@NotNull MutableSeq<? super E> dest, int destPos, int limit) {
        return copyTo(0, dest, destPos, limit);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(int srcPos, @NotNull MutableSeq<? super E> dest) {
        return copyTo(srcPos, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(int srcPos, @NotNull MutableSeq<? super E> dest, int destPos) {
        return copyTo(srcPos, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyTo(int srcPos, @NotNull MutableSeq<? super E> dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + srcPos + ") < 0");
        }
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

        final int kn = this.knownSize();
        if (kn >= 0 && srcPos >= kn) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        Iterator<E> it;
        try {
            it = this.iterator(srcPos);
        } catch (IndexOutOfBoundsException ignored) {
            return 0;
        }

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest.set(idx++, it.next());
        }
        return idx - destPos;
    }
}
