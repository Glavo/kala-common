package kala.collection;

import kala.Equatable;
import kala.annotations.Covariant;
import kala.collection.immutable.*;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.collection.base.Traversable;
import kala.collection.internal.view.Views;
import kala.function.CheckedFunction;
import kala.function.CheckedPredicate;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public interface Collection<@Covariant E> extends Traversable<E>, CollectionLike<E>, Equatable {

    int SEQ_HASH_MAGIC = -1140647423;

    int SET_HASH_MAGIC = 1045751549;

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

    //region Collection Operations

    default @NotNull String className() {
        return "Collection";
    }

    default <U> @NotNull CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    default @NotNull View<E> view() {
        return knownSize() == 0 ? View.empty() : new Views.Of<>(this);
    }

    default java.util.@NotNull @UnmodifiableView Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    //endregion

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return view().filter(predicate).toImmutableSeq();
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

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return view().filterNot(predicate).toImmutableSeq();
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
    @Contract(pure = true)
    default @NotNull ImmutableCollection<@NotNull E> filterNotNull() {
        return view().filterNotNull().toImmutableSeq();
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return view().<U>filterIsInstance(clazz).toImmutableSeq();
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return view().<U>map(mapper).toImmutableSeq();
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
        return view().<U>mapNotNull(mapper).toImmutableSeq();
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
        return view().mapMulti(mapper).toImmutableSeq();
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return view().flatMap(mapper).toImmutableSeq();
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

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return view().<U>zip(other).toImmutableSeq();
    }


    @Override
    default boolean canEqual(Object other) {
        return other instanceof Collection<?>;
    }

}
