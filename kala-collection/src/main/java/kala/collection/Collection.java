package kala.collection;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.immutable.*;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.view.CollectionViews;
import kala.function.CheckedFunction;
import kala.function.CheckedPredicate;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.function.*;

public interface Collection<@Covariant E> extends CollectionLike<E>, AnyCollection<E> {
    //region Static Factories

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> Collection<E> narrow(Collection<? extends E> collection) {
        return (Collection<E>) collection;
    }

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, Collection<E>> factory() {
        return CollectionFactory.narrow(ImmutableCollection.factory());
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Collection";
    }

    default <U> @NotNull CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull CollectionView<E> view() {
        return isEmpty() ? CollectionView.empty() : new CollectionViews.Of<>(this);
    }

    default java.util.@NotNull @UnmodifiableView Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filter(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNot(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filterNot(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNotUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableCollection<@NotNull E> filterNotNull() {
        return filterNotNull(ImmutableSeq.factory());
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return filterIsInstance(ImmutableSeq.factory(), clazz);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return map(ImmutableSeq.factory(), mapper);
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNull(ImmutableSeq.factory(), mapper);
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return mapMulti(ImmutableSeq.factory(), mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMap(ImmutableSeq.factory(), mapper);
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    default <R> Tuple2<? extends ImmutableCollection<E>, ? extends ImmutableCollection<E>> partition(@NotNull Predicate<? super E> predicate) {
        return partition(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull ImmutableCollection<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull ImmutableCollection<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        return view().<U, R>zip(other, mapper).toImmutableSeq();
    }

    @Contract(pure = true)
    default <U, V> @NotNull ImmutableCollection<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        return view().<U, V>zip3(other1, other2).toImmutableSeq();
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> distinct() {
        return distinct(ImmutableSeq.factory());
    }
}
