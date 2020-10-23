package asia.kala.traversable;

import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Transformable<@Covariant E> extends Mappable<E> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> Transformable<E> narrow(Transformable<? extends E> transformable) {
        return (Transformable<E>) transformable;
    }

    @NotNull
    @Override
    <U> Transformable<U> map(@NotNull Function<? super E, ? extends U> mapper);

    @NotNull
    Transformable<E> filter(@NotNull Predicate<? super E> predicate);

    @NotNull
    default Transformable<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @NotNull
    default Transformable<@NotNull E> filterNotNull() {
        return this.filter(Objects::nonNull);
    }

    @NotNull
    Tuple2<? extends Transformable<E>, ? extends Transformable<E>> span(@NotNull Predicate<? super E> predicate);
}
