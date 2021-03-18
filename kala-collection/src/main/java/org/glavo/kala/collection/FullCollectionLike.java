package org.glavo.kala.collection;

import org.glavo.kala.annotations.UnstableName;
import org.glavo.kala.collection.immutable.AbstractImmutableCollection;
import org.glavo.kala.collection.immutable.ImmutableCollection;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

@UnstableName
public interface FullCollectionLike<E> extends CollectionLike<E> {
    @NotNull FullCollectionLike<E> filter(@NotNull Predicate<? super E> predicate);

    @NotNull FullCollectionLike<E> filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull FullCollectionLike<@NotNull E> filterNotNull();

    <U> @NotNull FullCollectionLike<U> map(@NotNull Function<? super E, ? extends U> mapper);

    <U> @NotNull FullCollectionLike<U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper);

    @Contract(pure = true)
    <U> @NotNull FullCollectionLike<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

    <U> @NotNull FullCollectionLike<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other);
}
