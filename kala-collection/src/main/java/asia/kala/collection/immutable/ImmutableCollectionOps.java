package asia.kala.collection.immutable;

import asia.kala.traversable.TransformableOps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ImmutableCollectionOps<E, CC extends ImmutableCollection<?>, COLL extends ImmutableCollection<E>>
        extends TransformableOps<E, CC, COLL> {
    @NotNull
    @Contract(pure = true)
    <U> CC flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper);
}
