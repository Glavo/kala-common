package org.glavo.kala.collection;

import org.glavo.kala.collection.internal.view.IndexedSeqViews;
import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.function.IndexedBiFunction;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

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

    @SafeVarargs
    static <E> @NotNull IndexedSeq<E> of(E... values) {
        return IndexedSeq.<E>factory().from(values);
    }

    static <E> @NotNull IndexedSeq<E> from(E @NotNull [] values) {
        return IndexedSeq.<E>factory().from(values);
    }

    static <E> @NotNull IndexedSeq<E> from(@NotNull Iterable<? extends E> values) {
        return IndexedSeq.<E>factory().from(values);
    }

    //endregion

    @Override
    default @NotNull IndexedSeqView<E> view() {
        return size() == 0 ? IndexedSeqView.empty() : new IndexedSeqViews.Of<>(this);
    }

    E get(int index);

    int size();
}
