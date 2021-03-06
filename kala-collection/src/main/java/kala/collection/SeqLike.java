package kala.collection;

import kala.collection.base.GenericArrays;
import kala.collection.base.Growable;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.tuple.Tuple2;
import kala.tuple.primitive.IntObjTuple2;
import kala.collection.internal.view.SeqViews;
import kala.collection.mutable.ArrayBuffer;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
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

    default @NotNull Iterator<E> iterator(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
        }
        final int ks = knownSize();
        if (ks >= 0) {
            if (beginIndex > ks) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > size(" + ks + ")");
            }
            if (beginIndex == ks) {
                return Iterators.empty();
            }
        }

        final Iterator<E> it = iterator();
        int n = beginIndex;
        while (n-- > 0) {
            if (!it.hasNext()) {
                throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
            }
            it.next();
        }
        return it;
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

    @Override
    default E elementAt(int index) {
        return get(index);
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

    @Override
    default @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
        return firstOption(predicate);
    }

    default @NotNull Option<E> findFirst(@NotNull Predicate<? super E> predicate) {
        return firstOption(predicate);
    }

    default @NotNull Option<E> findLast(@NotNull Predicate<? super E> predicate) {
        return lastOption(predicate);
    }

    default E first() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().next();
    }

    default E first(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return Iterators.first(iterator(), predicate);
    }

    default @Nullable E firstOrNull() {
        if (knownSize() == 0) {
            return null;
        }
        Iterator<E> it = this.iterator();
        return it.hasNext() ? it.next() : null;
    }

    default @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            return null;
        }
        return Iterators.firstOrNull(iterator(), predicate);
    }

    default @NotNull Option<E> firstOption() {
        if (knownSize() == 0) {
            return Option.none();
        }
        Iterator<E> it = this.iterator();
        return it.hasNext() ? Option.some(it.next()) : Option.none();
    }

    default @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            return Option.none();
        }
        return Iterators.firstOption(iterator(), predicate);
    }

    default E last() {
        return reverseIterator().next();
    }

    default E last(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return Iterators.first(reverseIterator(), predicate);
    }

    default @Nullable E lastOrNull() {
        if (knownSize() == 0) {
            return null;
        }
        Iterator<E> it = reverseIterator();
        return it.hasNext() ? it.next() : null;
    }

    default @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            return null;
        }
        return Iterators.firstOrNull(reverseIterator(), predicate);
    }

    default @NotNull Option<E> lastOption() {
        if (knownSize() == 0) {
            return Option.none();
        }
        return Iterators.firstOption(reverseIterator());
    }

    default @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        if (knownSize() == 0) {
            return Option.none();
        }
        return Iterators.firstOption(reverseIterator(), predicate);
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

    default <U> @NotNull SeqView<@NotNull Tuple2<E, U>> zipView(@NotNull SeqLike<? extends U> other) {
        return new SeqViews.Zip<>(this, other);
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

    @Override
    default @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (this.knownSize() == 0) {
            return Option.none();
        }
        final Iterator<E> it = this.reverseIterator();
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(it.next(), e);
        }
        return Option.some(e);
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
