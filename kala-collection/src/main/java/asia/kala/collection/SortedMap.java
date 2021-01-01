package asia.kala.collection;

import asia.kala.comparator.Comparators;

import java.util.Comparator;

public interface SortedMap<K, V> {
    default Comparator<? super K> comparator() {
        return Comparators.naturalOrder();
    }

    K firstKey();

    K lastKey();
}
