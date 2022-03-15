package kala.function;

import java.io.Serializable;
import java.util.Objects;

final class Balances {
    static final Balance<?> DEFAULT = new Default<>();
    static final Balance<?> IDENTITY = new Identity<>();

    private static final class Default<T> implements Balance<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            if (obj == null) {
                return 0;
            }

            int originalHash = obj.hashCode();

            return originalHash ^ (originalHash >>> 16);
        }

        @Override
        public boolean test(T t1, T t2) {
            return Objects.equals(t1, t2);
        }

        private Object readResolve() {
            return DEFAULT;
        }
    }

    private static final class Identity<T> implements Balance<T>, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return System.identityHashCode(obj);
        }

        @Override
        public boolean test(T t1, T t2) {
            return t1 == t2;
        }

        private Object readResolve() {
            return IDENTITY;
        }
    }
}
