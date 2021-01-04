package org.glavo.kala;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public interface PrimitiveTuples extends Serializable {
    final class IntTuple2 implements PrimitiveTuples {
        private static final long serialVersionUID = 2586221246308875196L;
        private static final int HASH_MAGIC = -1464965121;

        public final int _1;
        public final int _2;

        public IntTuple2(int i1, int i2) {
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
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IntTuple2)) {
                return false;
            }
            IntTuple2 that = (IntTuple2) o;
            return _1 == that._1 &&
                    _2 == that._2;
        }

        @Override
        public final int hashCode() {
            return 31 * _1 + _2 + HASH_MAGIC;
        }

        @Override
        public final String toString() {
            return "IntTuple2(" + _1 + ", " + _2 + ")";
        }
    }

    final class BooleanTuple2 implements PrimitiveTuples {
        private static final long serialVersionUID = 3436771981901568081L;
        private static final int HASH_MAGIC = -1290448591;

        public final boolean _1;
        public final boolean _2;

        public BooleanTuple2(boolean i1, boolean i2) {
            _1 = i1;
            _2 = i2;
        }

        public final boolean component1() {
            return _1;
        }

        public final boolean component2() {
            return _2;
        }

        public final @NotNull Tuple2<@NotNull Boolean, @NotNull Boolean> toTuple2() {
            return Tuple.of(_1, _2);
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BooleanTuple2)) {
                return false;
            }
            BooleanTuple2 that = (BooleanTuple2) o;
            return _1 == that._1 &&
                    _2 == that._2;
        }

        @Override
        public final int hashCode() {
            return 31 * Boolean.hashCode(_1) + Boolean.hashCode(_2) + HASH_MAGIC;
        }

        @Override
        public final String toString() {
            return "BooleanTuple2(" + _1 + ", " + _2 + ")";
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
        public final int hashCode() {
            return 31 * _1 + Objects.hashCode(_2) + HASH_MAGIC;
        }

        @Override
        public final String toString() {
            return "IntObjTuple2(" + _1 + ", " + _2 + ")";
        }
    }

    final class IntTuple3 implements PrimitiveTuples {
        private static final long serialVersionUID = 6568634810148911056L;
        private static final int HASH_MAGIC = -1472170394;

        public final int _1;
        public final int _2;
        public final int _3;

        public IntTuple3(int i1, int i2, int i3) {
            _1 = i1;
            _2 = i2;
            _3 = i3;
        }

        public final int component1() {
            return _1;
        }

        public final int component2() {
            return _2;
        }

        public final int component3() {
            return _3;
        }

        public final @NotNull Tuple3<@NotNull Integer, @NotNull Integer, @NotNull Integer> toTuple3() {
            return Tuple.of(_1, _2, _3);
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IntTuple3)) {
                return false;
            }
            IntTuple3 other = (IntTuple3) o;
            return _1 == other._1 &&
                    _2 == other._2 &&
                    _3 == other._3;
        }

        @Override
        public final int hashCode() {
            int hash = 0;
            hash = 31 * hash + _1;
            hash = 31 * hash + _2;
            hash = 31 * hash + _3;
            return hash + HASH_MAGIC;
        }

        @Override
        public final String toString() {
            return "IntTuple3(" + _1 + ", " + _2 + ", " + _3 + ")";
        }
    }
}
