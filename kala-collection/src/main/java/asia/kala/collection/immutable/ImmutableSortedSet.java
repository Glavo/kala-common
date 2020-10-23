package asia.kala.collection.immutable;

import asia.kala.collection.SortedSet;
import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public interface ImmutableSortedSet<E> extends ImmutableSet<E>, SortedSet<E> {

    default <U> ImmutableSortedSet<U> map(
            Comparator<? super U> newComparator,
            @NotNull Function<? super E, ? extends U> mapper
    ) {
        CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> factory =
                this.iterableFactory(newComparator);
        return AbstractImmutableCollection.map(this, mapper, factory);
    }

    @NotNull
    default <U> ImmutableCollection<U> flatMap(
            Comparator<? super U> newComparator,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, this.iterableFactory(newComparator));
    }

    @NotNull

    @Override
    <U> CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> iterableFactory();

    @NotNull
    @Override
    <U> CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> iterableFactory(Comparator<? super U> comparator);
}
