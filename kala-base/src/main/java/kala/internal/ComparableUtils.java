package kala.internal;

import java.util.Comparator;

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
}
