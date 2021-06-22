package kala.collection.immutable;

import kala.collection.FullCollectionLikeOps;
import kala.function.CheckedFunction;
import org.jetbrains.annotations.NotNull;

public interface ImmutableCollectionOps<E, CC extends ImmutableCollection<?>, COLL extends ImmutableCollection<E>>
        extends FullCollectionLikeOps<E, CC, COLL> {
    <U, Ex extends Throwable> @NotNull CC mapChecked(@NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper);

    <U, Ex extends Throwable> @NotNull CC mapNotNullChecked(@NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper);
}
