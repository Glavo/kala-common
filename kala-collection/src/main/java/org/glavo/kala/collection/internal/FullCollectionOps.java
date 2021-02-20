package org.glavo.kala.collection.internal;

import org.glavo.kala.collection.CollectionLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface FullCollectionOps<E, CC extends CollectionLike<?>, COLL extends CollectionLike<E>> {

    @NotNull COLL filter(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNotNull();

    @NotNull <U> CC map(@NotNull Function<? super E, ? extends U> mapper);

    @Contract(pure = true)
    <U> @NotNull CC flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

}
