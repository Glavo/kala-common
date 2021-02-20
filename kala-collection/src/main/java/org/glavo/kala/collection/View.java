package org.glavo.kala.collection;

import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface View<@Covariant E> extends CollectionLike<E> {

    //region Collection Operations

    default String className() {
        return "View";
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

    default <U> @NotNull View<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        Objects.requireNonNull(other);
        return new Views.Zip<>(this, other);
    }
}
