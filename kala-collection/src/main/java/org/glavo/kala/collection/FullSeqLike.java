package org.glavo.kala.collection;

import org.glavo.kala.annotations.UnstableName;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

@UnstableName
public interface FullSeqLike<E> extends FullCollectionLike<E>, SeqLike<E> {
    @NotNull FullSeqLike<E> slice(int beginIndex, int endIndex);

    @NotNull FullSeqLike<E> drop(int n);

    @NotNull FullSeqLike<E> dropLast(int n);

    @NotNull FullSeqLike<E> dropWhile(@NotNull Predicate<? super E> predicate);

    @NotNull FullSeqLike<E> take(int n);

    @NotNull FullSeqLike<E> takeLast(int n);

    @NotNull FullSeqLike<E> takeWhile(@NotNull Predicate<? super E> predicate);

    @NotNull FullSeqLike<E> updated(int index, E newValue);

    @NotNull FullSeqLike<E> concat(@NotNull SeqLike<? extends E> other);

    @NotNull FullSeqLike<E> concat(java.util.@NotNull List<? extends E> other);

    @NotNull FullSeqLike<E> prepended(E element);

    @NotNull FullSeqLike<E> prependedAll(E @NotNull[] values);

    @NotNull FullSeqLike<E> prependedAll(@NotNull Iterable<? extends E> values);

    @NotNull FullSeqLike<E> appended(E element);

    @NotNull FullSeqLike<E> appendedAll(E @NotNull[] values);

    @NotNull FullSeqLike<E> appendedAll(@NotNull Iterable<? extends E> values);

    @NotNull FullSeqLike<E> sorted();

    @NotNull FullSeqLike<E> sorted(Comparator<? super E> comparator);

    @NotNull FullSeqLike<E> reversed();

    <U> @NotNull FullSeqLike<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper);

    <U> @NotNull FullSeqLike<U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper);
}
