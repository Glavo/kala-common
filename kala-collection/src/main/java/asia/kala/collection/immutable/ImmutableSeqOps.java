package asia.kala.collection.immutable;

import asia.kala.collection.Seq;
import asia.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Predicate;

public interface ImmutableSeqOps<E, CC extends ImmutableSeq<?>, COLL extends ImmutableSeq<E>>
        extends ImmutableCollectionOps<E, CC, COLL> {
    @NotNull COLL updated(int index, E newValue);

    @NotNull COLL drop(int n);

    @NotNull COLL dropLast(int n);

    @NotNull COLL dropWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL take(int n);

    @NotNull COLL takeLast(int n);

    @NotNull COLL takeWhile(@NotNull Predicate<? super E> predicate);

    @NotNull COLL concat(@NotNull Seq<? extends E> other);

    @NotNull COLL prepended(E element);

    @NotNull COLL prependedAll(@NotNull Iterable<? extends E> prefix);

    @NotNull COLL appended(E element);

    @NotNull COLL appendedAll(@NotNull Iterable<? extends E> prefix);

    @NotNull COLL sorted();

    @NotNull COLL sorted(@NotNull Comparator<? super E> comparator);

    @NotNull COLL reversed();

    @NotNull <U> CC mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper);
}
