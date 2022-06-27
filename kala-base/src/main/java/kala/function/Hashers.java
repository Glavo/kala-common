package kala.function;

import java.io.Serializable;
import java.util.Objects;

final class Hashers {
    static final Hasher<?> DEFAULT = new Default<>();
    static final Hasher<?> OPTIMIZED = new Optimized<>();
    static final Hasher<?> IDENTITY = new Identity<>();


    private static final class Default<T> implements Hasher<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return Objects.hashCode(obj);
        }

        private Object readResolve() {
            return DEFAULT;
        }
    }

    private static final class Optimized<T> implements Hasher<T>, Serializable {
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

    private static final class Identity<T> implements Hasher<T>, Serializable {
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
