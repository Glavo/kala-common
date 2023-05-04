package kala.function;

import kala.annotations.ReplaceWith;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Hasher<T> extends BiPredicate<T, T> {

    static <T> Hasher<T> narrow(Hasher<? super T> hasher) {
        return (Hasher<T>) hasher;
    }

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

    @Deprecated
    @ReplaceWith("equals(T, T)")
    default boolean test(T t1, T t2) {
        return equals(t1, t2);
    }
}
