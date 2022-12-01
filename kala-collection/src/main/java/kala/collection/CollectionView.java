package kala.collection;

import kala.annotations.DelegateBy;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.view.CollectionViews;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.*;

@SuppressWarnings("unchecked")
public interface CollectionView<@Covariant E> extends CollectionLike<E>, AnyCollectionView<E> {

    @SuppressWarnings("unchecked")
    static <E> @NotNull CollectionView<E> empty() {
        return ((CollectionView<E>) CollectionViews.Empty.INSTANCE);
    }

    static <E> @NotNull CollectionView<E> ofJava(@NotNull java.util.Collection<E> collection) {
        Objects.requireNonNull(collection);
        return collection.isEmpty() ? empty() : new CollectionViews.OfJava<>(collection);
    }

    @Override
    default @NotNull String className() {
        return "CollectionView";
    }

    @Override
    default @NotNull CollectionView<E> view() {
        return this;
    }

    default @NotNull CollectionView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new CollectionViews.Filter<>(this, predicate);
    }

    default @NotNull CollectionView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new CollectionViews.FilterNot<>(this, predicate);
    }

    default @NotNull CollectionView<@NotNull E> filterNotNull() {
        return new CollectionViews.FilterNotNull<>(this);
    }

    @Override
    default <U> @NotNull CollectionView<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((CollectionView<U>) filter(Predicates.instanceOf(clazz)));
    }

    default <U> @NotNull CollectionView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new CollectionViews.Mapped<>(this, mapper);
    }

    @Override
    default @NotNull <U> CollectionView<U> mapNotNull(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new CollectionViews.MapNotNull<>(this, mapper);
    }

    @Override
    default @NotNull <U> CollectionView<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Objects.requireNonNull(mapper);
        return new CollectionViews.MapMulti<>(this, mapper);
    }

    default <U> @NotNull CollectionView<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return new CollectionViews.FlatMapped<>(this, mapper);
    }

    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull CollectionView<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull CollectionView<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(mapper);
        return new CollectionViews.Zip<>(this, other, mapper);
    }

    default <U, V> @NotNull CollectionView<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        Objects.requireNonNull(other1);
        Objects.requireNonNull(other2);
        return new CollectionViews.Zip3<>(this, other1, other2);
    }

    default @NotNull CollectionView<E> distinct() {
        LinkedHashSet<E> set = new LinkedHashSet<>();
        for (E e : this) {
            set.add(e);
        }
        return ofJava(set);
    }
}
