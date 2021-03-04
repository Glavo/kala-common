package org.glavo.kala.collection.internal;

import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.SeqLike;
import org.glavo.kala.function.IndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Predicate;

public interface FullSeqOps<E, CC extends SeqLike<?>, COLL extends SeqLike<E>> extends FullCollectionOps<E, CC, COLL> {

    @NotNull COLL slice(int beginIndex, int endIndex);

    @NotNull COLL drop(int n);

    @NotNull COLL dropLast(int n);

    @NotNull COLL dropWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL take(int n);

    @NotNull COLL takeLast(int n);

    @NotNull COLL takeWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL updated(int index, E newValue);

    @NotNull COLL concat(@NotNull SeqLike<? extends E> other);

    @NotNull COLL prepended(E element);

    @NotNull COLL prependedAll(@NotNull Iterable<? extends E> prefix);

    @NotNull COLL appended(E element);

    @NotNull COLL appendedAll(@NotNull Iterable<? extends E> prefix);

    @NotNull COLL sorted();

    @NotNull COLL sorted(@NotNull Comparator<? super E> comparator);

    @NotNull COLL reversed();

    <U> @NotNull CC mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper);
}
