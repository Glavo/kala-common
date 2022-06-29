package kala.function;

import kala.annotations.ReplaceWith;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Hasher<T> extends BiPredicate<T, T> {

    Hasher<?> DEFAULT = new Default<>();
    Hasher<?> OPTIMIZED = new Optimized<>();
    Hasher<?> IDENTITY = new Identity<>();

    static <T> Hasher<T> narrow(Hasher<? super T> hasher) {
        return (Hasher<T>) hasher;
    }

    static <T> Hasher<T> defaultHasher() {
        return (Hasher<T>) DEFAULT;
    }

    static <T> Hasher<T> optimizedHasher() {
        return (Hasher<T>) OPTIMIZED;
    }

    static <T> Hasher<T> identityHasher() {
        return (Hasher<T>) IDENTITY;
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

    final class Default<T> implements Hasher<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return Objects.hashCode(obj);
        }

        private Object readResolve() {
            return DEFAULT;
        }
    }

    final class Optimized<T> implements Hasher<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            if (obj == null) {
                return 0;
            }

            int originalHash = obj.hashCode();

            return originalHash ^ (originalHash >>> 16);
        }

        private Object readResolve() {
            return OPTIMIZED;
        }
    }

    final class Identity<T> implements Hasher<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return System.identityHashCode(obj);
        }

        @Override
        public boolean equals(T t1, T t2) {
            return t1 == t2;
        }

        private Object readResolve() {
            return IDENTITY;
        }
    }
}
