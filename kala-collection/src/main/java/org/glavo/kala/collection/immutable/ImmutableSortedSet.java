package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.SortedSet;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public interface ImmutableSortedSet<E> extends ImmutableSet<E>, SortedSet<E> {


    @Override
    <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> iterableFactory();

    @Override
    <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> iterableFactory(Comparator<? super U> comparator);

    default <U> @NotNull ImmutableSortedSet<U> map(
            Comparator<? super U> newComparator,
            @NotNull Function<? super E, ? extends U> mapper
    ) {
        CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> factory =
                this.iterableFactory(newComparator);
        return AbstractImmutableCollection.map(this, mapper, factory);
    }

    default <U> @NotNull ImmutableCollection<U> flatMap(
            Comparator<? super U> newComparator,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, this.iterableFactory(newComparator));
    }

}
