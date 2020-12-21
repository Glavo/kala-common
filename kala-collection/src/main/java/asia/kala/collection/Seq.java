package asia.kala.collection;

import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.control.Option;
import asia.kala.annotations.Covariant;
import asia.kala.collection.immutable.*;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.CheckedIndexedConsumer;
import asia.kala.function.IndexedConsumer;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.*;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public interface Seq<@Covariant E> extends Collection<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> Seq<E> narrow(Seq<? extends E> seq) {
        return (Seq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ? extends Seq<E>> factory() {
        return ImmutableSeq.factory();
    }

    static <E> @NotNull Seq<E> of() {
        return ImmutableSeq.of();
    }

    static <E> @NotNull Seq<E> of(E value1) {
        return ImmutableSeq.of(value1);
    }

    static <E> @NotNull Seq<E> of(E value1, E values2) {
        return ImmutableSeq.of(value1, values2);
    }

    static <E> @NotNull Seq<E> of(E value1, E values2, E value3) {
        return ImmutableSeq.of(value1, values2, value3);
    }

    static <E> @NotNull Seq<E> of(E value1, E values2, E value3, E value4) {
        return ImmutableSeq.of(value1, values2, value3, value4);
    }

    static <E> @NotNull Seq<E> of(E value1, E values2, E value3, E value4, E value5) {
        return ImmutableSeq.of(value1, values2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull Seq<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(E @NotNull [] values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSeq.from(it);
    }

    //endregion

    static void checkElementIndex(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size);
        }
    }

    static void checkPositionIndex(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + size);
        }
    }


    //region Collection Operations

    @Override
    default String className() {
        return "Seq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Seq<U>> iterableFactory() {
        return Seq.factory();
    }

    @Override
    default @NotNull SeqView<E> view() {
        return new SeqViews.Of<>(this);
    }

    @Override
    default @NotNull @UnmodifiableView List<E> asJava() {
        return this instanceof IndexedSeq<?>
                ? new AsJavaConvert.IndexedSeqAsJava<>((IndexedSeq<E>) this)
                : new AsJavaConvert.SeqAsJava<>(this);
    }

    //endregion

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
        ImmutableList<E> l = ImmutableList.nil();
        for (E e : this) {
            l = l.cons(e);
        }
        return l.iterator();
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

    //region Copy Operations

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, target = "array", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] array) {
        return copyToArray(array, 0);
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, target = "array", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] array, int start) {
        int arrayLength = array.length; // implicit null check of array
        Iterator<E> it = iterator();

        int i = start;
        while (i < arrayLength && it.hasNext()) {
            array[i++] = it.next();
        }
        return i - start;
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, target = "array", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] array, int start, int length) {
        Iterator<E> it = iterator();
        int i = start;
        int end = start + Math.min(length, array.length - start); // implicit null check of array
        while (i < end && it.hasNext()) {
            array[i++] = it.next();
        }
        return i - start;
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

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Seq<?>;
    }
}
