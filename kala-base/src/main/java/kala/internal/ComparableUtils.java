package kala.internal;

public class ComparableUtils {
    private ComparableUtils() {
    }

    @SuppressWarnings("unchecked")
    public static int compare(Object o1, Object o2) {
        return ((Comparable<Object>) o1).compareTo(o2);
    }
}
