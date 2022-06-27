package kala.function;

import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Hasher<T> extends BiPredicate<T, T> {
    static <T> Hasher<T> defaultHasher() {
        return (Hasher<T>) Hashers.DEFAULT;
    }
    static <T> Hasher<T> optimizedHasher() {
        return (Hasher<T>) Hashers.OPTIMIZED;
    }

    static <T> Hasher<T> identityHasher() {
        return (Hasher<T>) Hashers.IDENTITY;
    }


    int hash(T obj);

    default boolean equals(T t1, T t2) {
        return Objects.equals(t1, t2);
    }

    default boolean test(T t1, T t2) {
        return equals(t1, t2);
    }
}
