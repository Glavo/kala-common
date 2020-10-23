package asia.kala.traversable;

import asia.kala.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface TransformableOps<E, CC extends Transformable<?>, COLL extends Transformable<E>>
        extends MappableOps<E, CC, COLL> {

    @NotNull <U> CC map(@NotNull Function<? super E, ? extends U> mapper);

    @NotNull
    COLL filter(@NotNull Predicate<? super E> predicate);

    @NotNull
    COLL filterNot(@NotNull Predicate<? super E> predicate);

    @NotNull
    COLL filterNotNull();

    @NotNull
    Tuple2<? extends COLL, ? extends COLL> span(@NotNull Predicate<? super E> predicate);
}
