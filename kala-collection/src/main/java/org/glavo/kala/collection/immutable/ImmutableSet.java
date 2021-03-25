package org.glavo.kala.collection.immutable;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.FullCollectionLike;
import org.glavo.kala.collection.SortedSet;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.Set;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;

public interface ImmutableSet<@Covariant E> extends ImmutableCollection<E>, Set<E> {

    static <E> CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory() {
        throw new UnsupportedOperationException();// TODO
    }

    @Override
    default @NotNull String className() {
        return "ImmutableSet";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSet<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull Spliterator<E> spliterator() {
        final int knownSize = knownSize();
        if (knownSize == 0) {
            return Spliterators.emptySpliterator();
        } else if (knownSize > 0) {
            return Spliterators.spliterator(iterator(), knownSize, Spliterator.DISTINCT | Spliterator.IMMUTABLE);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.DISTINCT | Spliterator.IMMUTABLE);
        }
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
        return filter(Objects::nonNull);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> @NotNull ImmutableSet<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (ImmutableSet<U>) filter(clazz::isInstance);
    }
}
