package kala.collection.immutable;

import kala.collection.FullCollectionLikeOps;
import kala.function.CheckedFunction;
import kala.function.CheckedPredicate;
import org.jetbrains.annotations.NotNull;

public interface ImmutableCollectionOps<E, CC extends ImmutableCollection<?>, COLL extends ImmutableCollection<E>>
        extends FullCollectionLikeOps<E, CC, COLL> {

    <Ex extends Throwable> @NotNull COLL filterChecked(@NotNull CheckedPredicate<? super E, ? extends Ex> predicate);

    @NotNull COLL filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate);

    <Ex extends Throwable> @NotNull COLL filterNotChecked(@NotNull CheckedPredicate<? super E, ? extends Ex> predicate);

    @NotNull COLL filterNotUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate);

    <U, Ex extends Throwable> @NotNull CC mapChecked(@NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper);

    <U, Ex extends Throwable> @NotNull CC mapNotNullChecked(@NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper);

    <U, Ex extends Throwable> @NotNull CC flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC flatMapUnchecked(@NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper);
}
