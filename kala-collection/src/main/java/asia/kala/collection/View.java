package asia.kala.collection;

import asia.kala.traversable.Transformable;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface View<@Covariant E> extends Collection<E>, Transformable<E> {
    @NotNull
    @Override
    @Contract(value = "-> this", pure = true)
    default View<E> view() {
        return this;
    }

    @Override
    default String className() {
        return "View";
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof View<?>;
    }

    @NotNull
    @Override
    default <U> View<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.Mapped<>(this, mapper);
    }

    @NotNull
    @Override
    default View<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new Views.Filter<>(this, predicate);
    }

    @NotNull
    @Override
    default View<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new Views.Filter<>(this, predicate.negate());
    }

    @NotNull
    default <U> View<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.FlatMapped<>(this, mapper);
    }

    @NotNull
    @Override
    default Tuple2<? extends View<E>, ? extends View<E>> span(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException(); // TODO
    }
}
