package org.glavo.kala.collection;

import org.glavo.kala.comparator.Comparators;

import java.util.Comparator;

public interface SortedMap<K, V> {
    default Comparator<? super K> comparator() {
        return Comparators.naturalOrder();
    }

    K firstKey();

    K lastKey();
}
