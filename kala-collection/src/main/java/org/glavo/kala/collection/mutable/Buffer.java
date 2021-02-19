package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.internal.AsJavaConvert;
import org.glavo.kala.collection.internal.FromJavaConvert;
import org.glavo.kala.collection.internal.CollectionHelper;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.Seq;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public interface Buffer<E> extends MutableSeq<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ? extends Buffer<E>> factory() {
        return ArrayBuffer.factory();
    }

    @Contract("-> new")
    static <E> @NotNull Buffer<E> create() {
        return new ArrayBuffer<>();
    }

    @Contract("-> new")
    static <E> @NotNull Buffer<E> of() {
        return ArrayBuffer.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull Buffer<E> of(E value1) {
        return ArrayBuffer.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull Buffer<E> of(E value1, E value2) {
        return ArrayBuffer.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull Buffer<E> of(E value1, E value2, E value3) {
        return ArrayBuffer.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull Buffer<E> of(E value1, E value2, E value3, E value4) {
        return ArrayBuffer.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull Buffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ArrayBuffer.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull Buffer<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull Buffer<E> from(E @NotNull [] values) {
        return ArrayBuffer.from(values);
    }

    static <E> @NotNull Buffer<E> from(@NotNull Iterable<? extends E> values) {
        return ArrayBuffer.from(values);
    }

    static <E> @NotNull Buffer<E> from(@NotNull Iterator<? extends E> it) {
        return ArrayBuffer.from(it);
    }

    @Contract("_ -> new")
    static <E> @NotNull Buffer<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof AsJavaConvert.BufferAsJava<?, ?>) {
            return ((AsJavaConvert.BufferAsJava<E, Buffer<E>>) list).collection;
        }
        return list instanceof RandomAccess
                ? new FromJavaConvert.IndexedBufferFromJava<>(list)
                : new FromJavaConvert.BufferFromJava<>(list);
    }

    //endregion

    //region Collection Operations

    @Override
    default String className() {
        return "Buffer";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Buffer<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull BufferEditor<E, ? extends Buffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @Override
    default @NotNull List<E> asJava() {
        return this instanceof IndexedSeq<?>
                ? new AsJavaConvert.IndexedBufferAsJava<>((Buffer<E> & IndexedSeq<E>) this)
                : new AsJavaConvert.BufferAsJava<>(this);
    }

    //endregion

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void appendAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        final int length = values.length;
        //noinspection StatementWithEmptyBody
        if (length == 0) {
        } else if (length == 1) {
            this.append(values[0]);
        } else {
            this.appendAll(ArraySeq.wrap(values));
        }
    }

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (E e : this.toImmutableSeq()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                this.append(e);
            }
        } else {
            for (E e : values) {
                this.append(e);
            }
        }
    }

    @Contract(mutates = "this")
    void prepend(E value);

    @Contract(mutates = "this")
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        this.prependAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            Iterator<?> iterator = ((Seq<?>) values).toImmutableArray().reverseIterator(); // avoid mutating under our own iterator
            while (iterator.hasNext()) {
                //noinspection ConstantConditions
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof Seq<?>) {
            Iterator<?> iterator = ((Seq<?>) values).reverseIterator();
            while (iterator.hasNext()) {
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof List<?> && values instanceof RandomAccess) {
            List<?> seq = (List<?>) values;
            int s = seq.size();
            for (int i = s - 1; i >= 0; i--) {
                prepend((E) seq.get(i));
            }
            return;
        }

        Object[] cv = CollectionHelper.asArray(values);

        for (int i = cv.length - 1; i >= 0; i--) {
            prepend((E) cv[i]);
        }
    }

    @Contract(mutates = "this")
    void insert(int index, @Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        insertAll(index, ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (E e : this.toImmutableSeq()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                insert(index++, e);
            }
        } else {
            for (E e : values) {
                insert(index++, e);
            }
        }
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    E removeAt(@Range(from = 0, to = Integer.MAX_VALUE) int index);

    @Contract(mutates = "this")
    default void removeAt(int index, int count) {
        for (int i = 0; i < count; i++) {
            removeAt(index);
        }
    }

    @Contract(mutates = "this")
    void clear();

    @Contract(mutates = "this")
    default void dropInPlace(int n) {
        if (n <= 0) {
            return;
        }
        removeAt(0, Integer.min(n, size()));
    }

    @Contract(mutates = "this")
    default void dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        int idx = indexWhere(predicate.negate());
        if (idx < 0) {
            clear();
        } else {
            dropInPlace(idx);
        }
    }

    @Contract(mutates = "this")
    default void takeInPlace(int n) {
        if (n <= 0) {
            clear();
            return;
        }

        final int size = this.size();
        if (n >= size) {
            return;
        }
        removeAt(n, size - n);
    }

    @Contract(mutates = "this")
    default void takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        int idx = indexWhere(predicate.negate());
        if (idx >= 0) {
            takeInPlace(idx);
        }
    }

}
