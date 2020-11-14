package asia.kala.collection.immutable;

import asia.kala.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ImmutableCollectionOps<E, CC extends ImmutableCollection<?>, COLL extends ImmutableCollection<E>> {

    @NotNull COLL filter(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull COLL filterNotNull();

    @NotNull <U> CC map(@NotNull Function<? super E, ? extends U> mapper);

    @Contract(pure = true)
    <U> @NotNull CC flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);

    @NotNull Tuple2<? extends COLL, ? extends COLL> span(@NotNull Predicate<? super E> predicate);
}
