package kala.internal;

import kala.annotations.StaticClass;
import kala.comparator.Comparators;

import java.util.Comparator;

@StaticClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComparableUtils {
    private ComparableUtils() {
    }

    public static int compare(Object o1, Object o2) {
        return ((Comparable<Object>) o1).compareTo(o2);
    }

    public static int compare(Object o1, Object o2, Comparator comparator) {
        if (comparator == null) {
            return compare(o1, o2);
        } else {
            return comparator.compare(o1, o2);
        }
    }

    public static boolean comparatorEquals(Comparator comparator1, Comparator comparator2) {
        if (comparator1 == null) {
            return comparator2 == null || comparator2.equals(Comparators.naturalOrder());
        }
        //noinspection ReplaceNullCheck
        if (comparator2 == null) {
            return comparator1.equals(Comparators.naturalOrder());
        }
        return comparator1.equals(comparator2);
    }
}
