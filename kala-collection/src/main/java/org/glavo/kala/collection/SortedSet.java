package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.comparator.Comparators;
import org.glavo.kala.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

public interface SortedSet<@Covariant E> extends Set<E> {

    @NotNull <U> CollectionFactory<U, ?, ? extends SortedSet<U>> iterableFactory(Comparator<? super U> comparator);

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
