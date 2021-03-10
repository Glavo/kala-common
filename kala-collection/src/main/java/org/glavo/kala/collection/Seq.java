package org.glavo.kala.collection;

import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.internal.convert.FromJavaConvert;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.internal.view.SeqViews;
import org.jetbrains.annotations.*;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

@SuppressWarnings("unchecked")
public interface Seq<@Covariant E> extends Collection<E>, SeqLike<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> Seq<E> narrow(Seq<? extends E> seq) {
        return (Seq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, Seq<E>> factory() {
        return CollectionFactory.narrow(ImmutableSeq.factory());
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

    static <E> @NotNull Seq<E> wrapJava(java.util.@NotNull List<? extends E> source) {
        Objects.requireNonNull(source);
        if (source instanceof AsJavaConvert.SeqAsJava<?, ?>) {
            return ((AsJavaConvert.SeqAsJava<E, ?>) source).source;
        }
        return source instanceof RandomAccess
                ? new FromJavaConvert.IndexedSeqFromJava<>((List<E>) source)
                : new FromJavaConvert.SeqFromJava<>((List<E>) source);
    }

    //endregion

    static int hashCode(@NotNull Seq<?> seq) {
        return Iterators.hash(seq.iterator()) + SEQ_HASH_MAGIC;
    }

    static boolean equals(@NotNull Seq<?> seq1, @NotNull Seq<?> seq2) {
        if (!seq1.canEqual(seq2) || !seq2.canEqual(seq1)) {
            return false;
        }

        return seq1.sameElements(seq2);
    }

    //region Collection Operations

    @Override
    default @NotNull String className() {
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
        return this instanceof RandomAccess
                ? new AsJavaConvert.IndexedSeqAsJava<>(this)
                : new AsJavaConvert.SeqAsJava<>(this);
    }

    //endregion

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Seq<?>;
    }
}
