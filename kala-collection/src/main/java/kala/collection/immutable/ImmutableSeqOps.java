package kala.collection.immutable;

import kala.collection.FullSeqLikeOps;
import kala.function.CheckedIndexedFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ImmutableSeqOps<E, CC extends ImmutableSeq<?>, COLL extends ImmutableSeq<E>>
        extends ImmutableCollectionOps<E, CC, COLL>, FullSeqLikeOps<E, CC, COLL> {
    <U, Ex extends Throwable> @NotNull CC mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex;

    <U> @NotNull CC mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper);

    <U, Ex extends Throwable> @NotNull CC mapIndexedNotNullChecked(
            @NotNull CheckedIndexedFunction<? super E, @Nullable ? extends U, ? extends Ex> mapper);

    <U> @NotNull CC mapIndexedNotNullUnchecked(
            @NotNull CheckedIndexedFunction<? super E, @Nullable ? extends U, ?> mapper);
}
