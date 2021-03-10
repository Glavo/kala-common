package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.internal.convert.FromJavaConvert;
import org.glavo.kala.comparator.Comparators;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.Seq;
import org.glavo.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public interface MutableSeq<E> extends MutableCollection<E>, Seq<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableSeq<E>> factory() {
        return CollectionFactory.narrow(MutableArray.factory());
    }

    static <E> @NotNull MutableSeq<E> of() {
        return MutableArray.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1) {
        return MutableArray.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2) {
        return MutableArray.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3) {
        return MutableArray.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return MutableArray.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArray.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableSeq<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableSeq<E> from(E @NotNull [] values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterator<? extends E> it) {
        return MutableArray.from(it);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> wrapJava(java.util.@NotNull List<E> list) {
        Objects.requireNonNull(list);
        return list instanceof RandomAccess
                ? new FromJavaConvert.MutableIndexedSeqFromJava<>(list)
                : new FromJavaConvert.MutableSeqFromJava<>(list);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "MutableSeq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull MutableSeqEditor<E, ? extends MutableSeq<E>> edit() {
        return new MutableSeqEditor<>(this);
    }

    @Override
    default @NotNull java.util.List<E> asJava() {
        if (this instanceof IndexedSeq<?>) {
            return new AsJavaConvert.MutableIndexedSeqAsJava<>((MutableSeq<E> & IndexedSeq<E>) this);
        }
        return new AsJavaConvert.MutableSeqAsJava<>(this);
    }

    @Override
    default @NotNull MutableSeq<E> asSynchronized() {
        return this instanceof IndexedSeq<?>
                ? new Synchronized.SynchronizedIndexedSeq<>((MutableSeq<E> & IndexedSeq<E>) this)
                : new Synchronized.SynchronizedSeq<>(this);
    }

    @Override
    default @NotNull MutableSeq<E> asSynchronized(@NotNull Object mutex) {
        Objects.requireNonNull(mutex);
        return this instanceof IndexedSeq<?>
                ? new Synchronized.SynchronizedIndexedSeq<>((MutableSeq<E> & IndexedSeq<E>) this, mutex)
                : new Synchronized.SynchronizedSeq<>(this, mutex);
    }

    //endregion

    @Contract(mutates = "this")
    void set(int index, E newValue);

    @Contract(mutates = "this")
    default void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void mapInPlaceIndexed(@NotNull IndexedFunction<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void sort() {
        sort(Comparators.naturalOrder());
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void sort(Comparator<? super E> comparator) {
        Object[] values = toArray();
        Arrays.sort(values, (Comparator<? super Object>) comparator);

        for (int i = 0; i < values.length; i++) {
            this.set(i, (E) values[i]);
        }
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size / 2; i++) {
            E tem = get(i);
            set(i, get(size - i - 1));
            set(size - i - 1, tem);
        }
    }

}
