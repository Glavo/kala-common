package org.glavo.kala.collection.mutable;

import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.internal.convert.FromJavaConvert;
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

public interface Buffer<E> extends MutableSeq<E>, BufferLike<E>{

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, Buffer<E>> factory() {
        return CollectionFactory.narrow(ArrayBuffer.factory());
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
            return ((AsJavaConvert.BufferAsJava<E, Buffer<E>>) list).source;
        }
        return list instanceof RandomAccess
                ? new FromJavaConvert.IndexedBufferFromJava<>(list)
                : new FromJavaConvert.BufferFromJava<>(list);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
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
        return this instanceof RandomAccess
                ? new AsJavaConvert.IndexedBufferAsJava<>( this)
                : new AsJavaConvert.BufferAsJava<>(this);
    }

    @Override
    default @NotNull Buffer<E> asSynchronized() {
        return this instanceof IndexedSeq<?>
                ? new Synchronized.SynchronizedIndexedBuffer<>((Buffer<E> & IndexedSeq<E>) this)
                : new Synchronized.SynchronizedBuffer<>(this);
    }

    @Override
    default @NotNull Buffer<E> asSynchronized(@NotNull Object mutex) {
        Objects.requireNonNull(mutex);
        return this instanceof IndexedSeq<?>
                ? new Synchronized.SynchronizedIndexedBuffer<>((Buffer<E> & IndexedSeq<E>) this, mutex)
                : new Synchronized.SynchronizedBuffer<>(this, mutex);
    }

    //endregion

}
