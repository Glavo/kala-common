package asia.kala.collection;

import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface View<@Covariant E> extends Collection<E> {

    //region Collection Operations

    @Override
    default String className() {
        return "View";
    }

    @Override
    @Contract(value = "-> this", pure = true)
    default @NotNull View<E> view() {
        return this;
    }

    //endregion

    default @NotNull View<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new Views.Filter<>(this, predicate);
    }


    default @NotNull View<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new Views.Filter<>(this, predicate.negate());
    }


    default <U> @NotNull View<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.Mapped<>(this, mapper);
    }

    default <U> @NotNull View<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.FlatMapped<>(this, mapper);
    }


    default @NotNull Tuple2<? extends View<E>, ? extends View<E>> span(@NotNull Predicate<? super E> predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof View<?>;
    }
}
