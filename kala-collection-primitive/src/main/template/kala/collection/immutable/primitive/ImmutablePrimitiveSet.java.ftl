package kala.collection.immutable;

import kala.collection.primitive.*;
import kala.collection.factory.CollectionFactory;
import kala.collection.Set;
import kala.function.Predicates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface Immutable${Type}Set extends ImmutableCollection<E>, Set<E>, ImmutableAnySet<E> {

    @Contract(value = "_ -> param1", pure = true)
    static <E> Immutable${Type}Set narrow(ImmutableSet<? extends E> set) {
        return (Immutable${Type}Set) set;
    }

    //region Static Factories

    static <E> CollectionFactory<E, ?, ? extends Immutable${Type}Set> factory() {
        return ImmutableHashSet.factory();
    }

    static <E> @NotNull Immutable${Type}Set empty() {
        return ImmutableHashSet.empty();
    }

    static <E> @NotNull Immutable${Type}Set of() {
        return ImmutableHashSet.of();
    }

    static <E> @NotNull Immutable${Type}Set of(E value1) {
        return ImmutableHashSet.of(value1);
    }

    static <E> @NotNull Immutable${Type}Set of(E value1, E value2) {
        return ImmutableHashSet.of(value1, value2);
    }

    static <E> @NotNull Immutable${Type}Set of(E value1, E value2, E value3) {
        return ImmutableHashSet.of(value1, value2, value3);
    }

    static <E> @NotNull Immutable${Type}Set of(E value1, E value2, E value3, E value4) {
        return ImmutableHashSet.of(value1, value2, value3, value4);
    }

    static <E> @NotNull Immutable${Type}Set of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableHashSet.of(value1, value2, value3, value4, value5);
    }

    static <E> @NotNull Immutable${Type}Set of(E... values) {
        return ImmutableHashSet.of(values);
    }

    static <E> @NotNull Immutable${Type}Set from(E @NotNull [] values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull Immutable${Type}Set from(@NotNull Iterable<? extends E> values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull Immutable${Type}Set from(@NotNull Iterator<? extends E> it) {
        return ImmutableHashSet.from(it);
    }

    static <E> @NotNull Immutable${Type}Set from(@NotNull Stream<? extends E> stream) {
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

    default @NotNull Immutable${Type}Set added(E value) {
        if (contains(value)) {
            return this;
        }
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends Immutable${Type}Set> factory =
                    (CollectionFactory<E, ?, ? extends Immutable${Type}Set>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutable${Type}Set.added(this, value, factory);
        }
        return AbstractImmutable${Type}Set.added(this, value, iterableFactory());
    }

    default @NotNull Immutable${Type}Set addedAll(@NotNull Iterable<? extends E> values) {
        /*
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends Immutable${Type}Set> factory =
                    (CollectionFactory<E, ?, ? extends Immutable${Type}Set>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutable${Type}Set.addedAll(this, values, factory);
        }
        */
        return AbstractImmutable${Type}Set.addedAll(this, values, iterableFactory());
    }

    default @NotNull Immutable${Type}Set addedAll(E @NotNull [] values) {
        return addedAll(${Type}ArraySeq.wrap(values));
    }

    @Override
    default @NotNull Immutable${Type}Set filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, factory());
    }

    @Override
    default @NotNull Immutable${Type}Set filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, factory());
    }
}
