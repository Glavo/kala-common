package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

import java.util.Comparator;

public class MutableTreeSetTest implements MutableSortedSetTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, ? extends MutableSortedSet<E>> factory(Comparator<? super E> comparator) {
        return MutableTreeSet.factory(comparator);
    }

    @Override
    public <E> MutableSortedSet<E> of(Comparator<? super E> comparator, E... elements) {
        return MutableTreeSet.of(comparator, elements);
    }

    @Override
    public <E> MutableSortedSet<E> from(Comparator<? super E> comparator, E[] elements) {
        return MutableTreeSet.from(comparator, elements);
    }

    @Override
    public <E> MutableSortedSet<E> from(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        return MutableTreeSet.from(comparator, elements);
    }
}
