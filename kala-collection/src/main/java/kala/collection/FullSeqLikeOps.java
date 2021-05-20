package kala.collection;

import kala.annotations.UnstableName;
import kala.function.IndexedFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

@UnstableName
public interface FullSeqLikeOps<E, CC extends SeqLike<?>, COLL extends SeqLike<E>>
        extends FullCollectionLikeOps<E, CC, COLL>, SeqLike<E> {

    @NotNull COLL slice(int beginIndex, int endIndex);

    @NotNull COLL drop(int n);

    @NotNull COLL dropLast(int n);

    @NotNull COLL dropWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL take(int n);

    @NotNull COLL takeLast(int n);

    @NotNull COLL takeWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL updated(int index, E newValue);

    @NotNull COLL concat(@NotNull SeqLike<? extends E> other);

    @NotNull COLL concat(java.util.@NotNull List<? extends E> other);

    @NotNull COLL prepended(E element);

    @NotNull COLL prependedAll(@NotNull Iterable<? extends E> values);

    @NotNull COLL appended(E element);

    @NotNull COLL appendedAll(@NotNull Iterable<? extends E> values);

    @NotNull COLL sorted();

    @NotNull COLL sorted(Comparator<? super E> comparator);

    @NotNull COLL reversed();

    <U> @NotNull CC mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper);

    <U> @NotNull CC mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper);
}
