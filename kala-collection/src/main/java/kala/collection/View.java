package kala.collection;

import kala.collection.internal.view.Views;
import kala.function.Predicates;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public interface View<@Covariant E> extends CollectionLike<E>, FullCollectionLike<E> {

    @SuppressWarnings("unchecked")
    static <E> @NotNull View<E> empty() {
        return ((View<E>) Views.Empty.INSTANCE);
    }

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "View";
    }

    @Override
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
        return new Views.FilterNot<>(this, predicate);
    }

    default @NotNull View<@NotNull E> filterNotNull() {
        return new Views.FilterNotNull<>(this);
    }

    @Override
    default <U> @NotNull View<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((View<U>) filter(Predicates.instanceOf(clazz)));
    }

    default <U> @NotNull View<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.Mapped<>(this, mapper);
    }

    @Override
    default @NotNull <U> View<U> mapNotNull(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.MapNotNull<>(this, mapper);
    }

    @Override
    default @NotNull <U> View<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Objects.requireNonNull(mapper);
        return new Views.MapMulti<>(this, mapper);
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
