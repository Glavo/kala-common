package kala.collection;

import kala.collection.factory.MapFactory;
import kala.comparator.Comparators;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface SortedMap<K, V> extends Map<K, V> {
    @NotNull <NK, NV> MapFactory<NK, NV, ?, ? extends Map<NK, NV>> mapFactory(Comparator<? super NK> comparator);

    default Comparator<? super K> comparator() {
        return Comparators.naturalOrder();
    }

    K firstKey();

    K lastKey();
}
