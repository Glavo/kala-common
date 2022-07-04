package kala.collection.immutable;

import kala.collection.ArraySeq;
import kala.collection.SortedSet;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import kala.collection.Set;
import kala.function.Predicates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface ImmutableSet<@Covariant E> extends ImmutableCollection<E>, Set<E>, ImmutableAnySet<E> {

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableSet<E> narrow(ImmutableSet<? extends E> set) {
        return (ImmutableSet<E>) set;
    }

    //region Static Factories

    static <E> CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory() {
        return ImmutableHashSet.factory();
    }


    static <E> @NotNull ImmutableSet<E> empty() {
        return ImmutableHashSet.empty();
    }

    static <E> @NotNull ImmutableSet<E> of() {
        return ImmutableHashSet.of();
    }

    static <E> @NotNull ImmutableSet<E> of(E value1) {
        return ImmutableHashSet.of(value1);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2) {
        return ImmutableHashSet.of(value1, value2);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3) {
        return ImmutableHashSet.of(value1, value2, value3);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableHashSet.of(value1, value2, value3, value4);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableHashSet.of(value1, value2, value3, value4, value5);
    }

    static <E> @NotNull ImmutableSet<E> of(E... values) {
        return ImmutableHashSet.of(values);
    }

    static <E> @NotNull ImmutableSet<E> from(E @NotNull [] values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableHashSet.from(it);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableHashSet.from(stream);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "ImmutableSet";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSet<U>> iterableFactory() {
        return factory();
    }

    default @NotNull ImmutableSet<E> added(E value) {
        if (contains(value)) {
            return this;
        }
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                    (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutableSet.added(this, value, factory);
        }
        return AbstractImmutableSet.added(this, value, iterableFactory());
    }

    default @NotNull ImmutableSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                    (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutableSet.addedAll(this, values, factory);
        }
        return AbstractImmutableSet.addedAll(this, values, iterableFactory());
    }

    default @NotNull ImmutableSet<E> addedAll(E @NotNull [] values) {
        return addedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull ImmutableSet<E> filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, factory());
    }

    @Override
    default @NotNull ImmutableSet<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, factory());
    }

    @Override
    default @NotNull ImmutableSet<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> @NotNull ImmutableSet<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (ImmutableSet<U>) filter(clazz::isInstance);
    }
}
