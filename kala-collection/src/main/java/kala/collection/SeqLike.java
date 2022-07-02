package kala.collection;

import kala.Conditions;
import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.collection.base.GenericArrays;
import kala.collection.base.Growable;
import kala.collection.base.Iterators;
import kala.collection.immutable.AbstractImmutableCollection;
import kala.collection.immutable.AbstractImmutableSeq;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.SeqIterators;
import kala.collection.mutable.MutableArrayList;
import kala.comparator.Comparators;
import kala.control.Option;
import kala.function.*;
import kala.tuple.Tuple2;
import kala.tuple.primitive.IntObjTuple2;
import kala.collection.internal.view.SeqViews;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.*;

public interface SeqLike<E> extends CollectionLike<E>, AnySeqLike<E> {
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
        final int knownSize = knownSize();
        if (knownSize >= 0) {
            if (beginIndex > knownSize) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > size(" + knownSize + ")");
            }
            if (beginIndex == knownSize) {
                return Iterators.empty();
            }
        }

        final Iterator<E> it = iterator();
        for (int i = 0; i < beginIndex; i++) {
            if (!it.hasNext()) {
                throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
            }
            it.next();
        }
        return it;
    }

    default @NotNull SeqIterator<E> seqIterator() {
        return seqIterator(0);
    }

    default @NotNull SeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultSeqIterator<>(this, index);
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
        Iterator<E> it = iterator(index);
        if (!it.hasNext()) throw new IndexOutOfBoundsException();

        return it.next();
    }

    @Contract(pure = true)
    @DelegateBy("get(int)")
    default @Nullable E getOrNull(int index) {
        return isDefinedAt(index) ? get(index) : null;
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, targetIsContainer = true)
    @DelegateBy("get(int)")
    default @NotNull Option<E>  getOption(int index) {
        return isDefinedAt(index) ? Option.some(get(index)) : Option.none();
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
        MutableArrayList<E> buffer = ks > 0
                ? new MutableArrayList<>(ks)
                : new MutableArrayList<>();
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
        return findFirst(predicate);
    }

    default @NotNull Option<E> findFirst(@NotNull Predicate<? super E> predicate) {
        return Iterators.firstOption(iterator(), predicate);
    }

    default @NotNull Option<E> findLast(@NotNull Predicate<? super E> predicate) {
        return Iterators.firstOption(reverseIterator(), predicate);
    }

    default E first() {
        return this.iterator().next();
    }

    @DelegateBy("first()")
    default @Nullable E firstOrNull() {
        return isNotEmpty() ? first() : null;
    }

    @DelegateBy("first()")
    default @NotNull Option<E> firstOption() {
        return isNotEmpty() ? Option.some(first()) : Option.none();
    }

    default E last() {
        return reverseIterator().next();
    }

    @DelegateBy("last()")
    default @Nullable E lastOrNull() {
        return isNotEmpty() ? last() : null;
    }

    @DelegateBy("last()")
    default @NotNull Option<E> lastOption() {
        return isNotEmpty() ? Option.some(last()) : Option.none();
    }

    @DelegateBy("findFirst(Predicate<E>)")
    default E first(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate).get();
    }

    @DelegateBy("findFirst(Predicate<E>)")
    default @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate).getOrNull();
    }

    @DelegateBy("findFirst(Predicate<E>)")
    default @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate);
    }

    @DelegateBy("findLast(Predicate<E>)")
    default E last(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate).get();
    }

    @DelegateBy("findLast(Predicate<E>)")
    default @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate).getOrNull();
    }

    @DelegateBy("findLast(Predicate<E>)")
    default @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate);
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

    @Contract(pure = true)
    default @NotNull SeqLike<E> slice(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> dropLast(int n) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> take(int n) {
        throw new UnsupportedOperationException();
    }

    default @NotNull SeqLike<E> takeLast(int n) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> updated(int index, E newValue) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> concat(@NotNull SeqLike<? extends E> other) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> concat(@NotNull List<? extends E> other) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> prepended(E value) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> prependedAll(E @NotNull [] values) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> prependedAll(@NotNull Iterable<? extends E> values) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> appended(E value) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> appendedAll(@NotNull Iterable<? extends E> values) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> appendedAll(E @NotNull [] values) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> sorted() {
        return sorted(Comparators.naturalOrder());
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> sorted(Comparator<? super E> comparator) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull SeqLike<E> reversed() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default @NotNull SeqLike<E> filter(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default @NotNull SeqLike<E> filterNot(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default @NotNull SeqLike<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull SeqLike<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull SeqLike<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default <U> @NotNull SeqLike<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull SeqLike<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default <U> @NotNull SeqLike<@NotNull U> mapIndexedNotNull(
            @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default @NotNull <U> SeqLike<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        throw new UnsupportedOperationException();
    }

    @Contract(pure = true)
    default @NotNull <U> SeqLike<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull SeqLike<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        throw new UnsupportedOperationException();
    }

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
