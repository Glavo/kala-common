package kala.collection;

import kala.collection.internal.view.IndexedSeqViews;
import kala.annotations.Covariant;
import kala.collection.immutable.ImmutableVector;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface IndexedSeq<@Covariant E> extends Seq<E>, IndexedSeqLike<E>, RandomAccess {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> IndexedSeq<E> narrow(IndexedSeq<? extends E> seq) {
        return (IndexedSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, IndexedSeq<E>> factory() {
        return CollectionFactory.narrow(ImmutableVector.factory());
    }

    static <E> @NotNull Collector<E, ?, IndexedSeq<E>> collector() {
        return factory();
    }

    static <E> @NotNull IndexedSeq<E> empty() {
        return ImmutableVector.empty();
    }

    static <E> @NotNull IndexedSeq<E> of() {
        return ImmutableVector.of();
    }

    static <E> @NotNull IndexedSeq<E> of(E value1) {
        return ImmutableVector.of(value1);
    }

    static <E> @NotNull IndexedSeq<E> of(E value1, E value2) {
        return ImmutableVector.of(value1, value2);
    }

    static <E> @NotNull IndexedSeq<E> of(E value1, E value2, E value3) {
        return ImmutableVector.of(value1, value2, value3);
    }

    static <E> @NotNull IndexedSeq<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableVector.of(value1, value2, value3, value4);
    }

    static <E> @NotNull IndexedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableVector.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull IndexedSeq<E> of(E... values) {
        return ImmutableVector.of(values);
    }

    static <E> @NotNull IndexedSeq<E> from(E @NotNull [] values) {
        return ImmutableVector.from(values);
    }

    static <E> @NotNull IndexedSeq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableVector.from(values);
    }

    static <E> @NotNull IndexedSeq<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableVector.from(it);
    }


    static <E> @NotNull IndexedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableVector.from(stream);
    }

    //endregion

    @Override
    default @NotNull IndexedSeqView<E> view() {
        return size() == 0 ? IndexedSeqView.empty() : new IndexedSeqViews.Of<>(this);
    }

    E get(int index);

    int size();
}
