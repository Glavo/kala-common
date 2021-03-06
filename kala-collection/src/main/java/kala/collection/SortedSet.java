package kala.collection;

import kala.annotations.Covariant;
import kala.comparator.Comparators;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

public interface SortedSet<@Covariant E> extends Set<E> {

    <U> @NotNull CollectionFactory<U, ?, ? extends SortedSet<U>> iterableFactory(Comparator<? super U> comparator);

    default Comparator<? super E> comparator() {
        return Comparators.naturalOrder();
    }

    @Contract(pure = true)
    default E first() {
        return iterator().next();
    }

    @Contract(pure = true)
    default E last() {
        Iterator<E> iterator = iterator();
        E res = iterator.next();
        while (iterator.hasNext()) {
            res = iterator.next();
        }
        return res;
    }
}
