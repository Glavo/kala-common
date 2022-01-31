package kala.collection;

import kala.collection.base.Iterators;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.internal.convert.AsJavaConvert;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.view.SeqViews;
import kala.concurrent.ConcurrentScope;
import kala.function.CheckedConsumer;
import kala.io.StdOut;
import org.jetbrains.annotations.*;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntFunction;
import java.util.function.Supplier;

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

    static <E> @NotNull Seq<E> empty() {
        return ImmutableSeq.empty();
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

    static <E> @NotNull Seq<E> fill(int n, E value) {
        return ImmutableSeq.fill(n, value);
    }

    static <E> @NotNull Seq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        return ImmutableSeq.fill(n, supplier);
    }

    static <E> @NotNull Seq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return ImmutableSeq.fill(n, init);
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
        if (seq1 == seq2) {
            return true;
        }
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
        return knownSize() == 0 ? SeqView.empty() : new SeqViews.Of<>(this);
    }

    @Override
    default @NotNull @UnmodifiableView List<E> asJava() {
        return this.supportsFastRandomAccess()
                ? new AsJavaConvert.IndexedSeqAsJava<>(this)
                : new AsJavaConvert.SeqAsJava<>(this);
    }

    //endregion

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Seq<?>;
    }
}
