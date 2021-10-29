package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

final class ${Type}Comparators {
    enum NaturalOrderComparator implements ${Type}Comparator {
        INSTANCE;

        @Override
        public final int compare(${PrimitiveType} ${var}1, ${PrimitiveType} b2) {
            return ${WrapperType}.compare(${var}1, b2);
        }

        @Override
        public final @NotNull ${Type}Comparator nullsFirst() {
            return NaturalOrderNullComparator.NULLS_FIRST;
        }

        @Override
        public final @NotNull ${Type}Comparator nullsLast() {
            return NaturalOrderNullComparator.NULLS_LAST;
        }

        @Override
        public final @NotNull ${Type}Comparator reversed() {
            return ReverseOrderComparator.INSTANCE;
        }
    }

    enum ReverseOrderComparator implements ${Type}Comparator {
        INSTANCE;

        @Override
        public final int compare(${PrimitiveType} ${var}1, ${PrimitiveType} b2) {
            return ${WrapperType}.compare(b2, ${var}1);
        }

        @Override
        public final @NotNull ${Type}Comparator nullsFirst() {
            return ReverseOrderNullComparator.NULLS_FIRST;
        }

        @Override
        public final @NotNull ${Type}Comparator nullsLast() {
            return ReverseOrderNullComparator.NULLS_LAST;
        }

        @Override
        public final @NotNull ${Type}Comparator reversed() {
            return NaturalOrderComparator.INSTANCE;
        }
    }

    static final class NullComparator implements ${Type}Comparator, Serializable {
        private static final long serialVersionUID = 0L;

        private final boolean nullFirst;

        private final @NotNull ${Type}Comparator real;

        NullComparator(boolean nullFirst, @NotNull ${Type}Comparator real) {
            this.nullFirst = nullFirst;
            this.real = real;
        }

        @Override
        public int compare(${PrimitiveType} ${var}1, ${PrimitiveType} b2) {
            return real.compare(${var}1, b2);
        }

        @Override
        public int compare(@Nullable ${WrapperType} ${var}1, @Nullable ${WrapperType} b2) {
            if (${var}1 == null) {
                return (b2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return real.compare(${var}1, b2);
            }
        }

        @Override
        public @NotNull ${Type}Comparator nullsFirst() {
            return nullFirst ? this : new NullComparator(true, real);
        }

        @Override
        public @NotNull ${Type}Comparator nullsLast() {
            return nullFirst ? new NullComparator(false, real) : this;
        }

        @Override
        public @NotNull ${Type}Comparator reversed() {
            return new NullComparator(!nullFirst, real.reversed());
        }

    }

    static final class NaturalOrderNullComparator implements ${Type}Comparator, Serializable {
        private static final long serialVersionUID = 0L;

        static final NaturalOrderNullComparator NULLS_FIRST = new NaturalOrderNullComparator(true);
        static final NaturalOrderNullComparator NULLS_LAST = new NaturalOrderNullComparator(false);

        private final boolean nullFirst;

        NaturalOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public int compare(${PrimitiveType} ${var}1, ${PrimitiveType} b2) {
            return ${WrapperType}.compare(${var}1, b2);
        }

        @Override
        public int compare(${WrapperType} ${var}1, ${WrapperType} b2) {
            if (${var}1 == null) {
                return (b2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return ${var}1.compareTo(b2);
            }
        }

        @Override
        public @NotNull ${Type}Comparator nullsFirst() {
            return NULLS_FIRST;
        }

        @Override
        public @NotNull ${Type}Comparator nullsLast() {
            return NULLS_LAST;
        }

        @Override
        public @NotNull ${Type}Comparator reversed() {
            return nullFirst ? ReverseOrderNullComparator.NULLS_LAST : ReverseOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }

    static final class ReverseOrderNullComparator implements ${Type}Comparator, Serializable {
        private static final long serialVersionUID = 0L;

        static final ReverseOrderNullComparator NULLS_FIRST = new ReverseOrderNullComparator(true);
        static final ReverseOrderNullComparator NULLS_LAST = new ReverseOrderNullComparator(false);

        private final boolean nullFirst;

        ReverseOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public int compare(${PrimitiveType} ${var}1, ${PrimitiveType} ${var}2) {
            return ${WrapperType}.compare(${var}2, ${var}1);
        }

        @Override
        public int compare(${WrapperType} ${var}1, ${WrapperType} ${var}2) {
            if (${var}1 == null) {
                return (${var}2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (${var}2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return ${var}2.compareTo(${var}1);
            }
        }

        @Override
        public @NotNull ${Type}Comparator nullsFirst() {
            return NULLS_FIRST;
        }

        @Override
        public @NotNull ${Type}Comparator nullsLast() {
            return NULLS_LAST;
        }

        @Override
        public @NotNull ${Type}Comparator reversed() {
            return nullFirst ? NaturalOrderNullComparator.NULLS_LAST : NaturalOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }
}
