package asia.kala;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public interface PrimitiveTuples extends Serializable {
    final class IntIntTuple2 implements PrimitiveTuples {
        private static final long serialVersionUID = 2586221246308875196L;
        private static final int HASH_MAGIC = -1464965121;

        public final int _1;
        public final int _2;

        public IntIntTuple2(int i1, int i2) {
            _1 = i1;
            _2 = i2;
        }

        public final int component1() {
            return _1;
        }

        public final int component2() {
            return _2;
        }

        public final @NotNull Tuple2<@NotNull Integer, @NotNull Integer> toTuple2() {
            return Tuple.of(_1, _2);
        }

        @Override
        public final int hashCode() {
            return 31 * _1 + _2 + HASH_MAGIC;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IntIntTuple2)) {
                return false;
            }
            IntIntTuple2 that = (IntIntTuple2) o;
            return _1 == that._1 &&
                    _2 == that._2;
        }

        @Override
        public final String toString() {
            return "IntIntTuple2(" + _1 + ", " + _2 + ")";
        }
    }

    final class IntObjTuple2<T> implements PrimitiveTuples {
        private static final long serialVersionUID = 5346453005675203380L;
        private static final int HASH_MAGIC = -1381225368;

        public final int _1;
        public final T _2;

        public IntObjTuple2(int i, T t) {
            _1 = i;
            _2 = t;
        }

        public final int component1() {
            return _1;
        }

        public final T component2() {
            return _2;
        }


        public final @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
            return Tuple.of(_1, _2);
        }

        @Override
        public final int hashCode() {
            return 31 * _1 + Objects.hashCode(_2) + HASH_MAGIC;
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IntObjTuple2<?>)) {
                return false;
            }
            IntObjTuple2<?> that = (IntObjTuple2<?>) o;
            return _1 == that._1 &&
                    Objects.equals(_2, that._2);
        }

        @Override
        public final String toString() {
            return "IntObjTuple2(" + _1 + ", " + _2 + ")";
        }
    }
}
