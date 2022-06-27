package kala.function;

import java.util.function.BiPredicate;

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

    boolean test(T t1, T t2);
}
