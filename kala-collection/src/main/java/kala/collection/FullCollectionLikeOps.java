package kala.collection;

import kala.annotations.UnstableName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

@UnstableName
public interface FullCollectionLikeOps<E, CC extends CollectionLike<?>, COLL extends CollectionLike<E>>
        extends CollectionLike<E> {

    @NotNull COLL filter(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNotNull();

    <U> @NotNull CC filterIsInstance(@NotNull Class<? extends U> clazz);

    <U> @NotNull CC map(@NotNull Function<? super E, ? extends U> mapper);

    <U> @NotNull CC mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper);

    @Contract(pure = true)
    <U> @NotNull CC flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

}
