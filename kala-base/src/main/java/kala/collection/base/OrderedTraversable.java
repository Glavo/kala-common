package kala.collection.base;

import kala.annotations.DelegateBy;
import kala.control.Option;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A collection of elements that exist in some order.
 * <p>
 * This order can be insertion order, sort order, or any other meaningful order,
 * its iterators and iteration methods should follow this order.
 */
public interface OrderedTraversable<E> extends Traversable<E> {

    default @NotNull Iterator<E> reverseIterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Iterators.empty();
        }
        Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return Iterators.empty();
        }
        List<E> buffer = ks > 0
                ? new ArrayList<>(ks)
                : new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }

        @SuppressWarnings("unchecked")
        Iterator<E> res = (Iterator<E>) GenericArrays.reverseIterator(buffer.toArray());
        return res;
    }

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
    default E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final Iterator<E> it = this.reverseIterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(it.next(), e);
        }
        return e;
    }

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
}
