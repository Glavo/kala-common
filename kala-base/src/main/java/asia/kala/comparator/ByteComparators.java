package asia.kala.comparator;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

final class ByteComparators {
    enum NaturalOrderComparator implements ByteComparator {
        INSTANCE;

        @Override
        public final int compare(byte b1, byte b2) {
            return Byte.compare(b1, b2);
        }

        @NotNull
        @Override
        public final ByteComparator nullsFirst() {
            return NaturalOrderNullComparator.NULLS_FIRST;
        }

        @NotNull
        @Override
        public final ByteComparator nullsLast() {
            return NaturalOrderNullComparator.NULLS_LAST;
        }

        @NotNull
        @Override
        public final ByteComparator reversed() {
            return ReverseOrderComparator.INSTANCE;
        }
    }

    enum ReverseOrderComparator implements ByteComparator {
        INSTANCE;

        @Override
        public final int compare(byte b1, byte b2) {
            return Byte.compare(b2, b1);
        }

        @NotNull
        @Override
        public final ByteComparator nullsFirst() {
            return ReverseOrderNullComparator.NULLS_FIRST;
        }

        @NotNull
        @Override
        public final ByteComparator nullsLast() {
            return ReverseOrderNullComparator.NULLS_LAST;
        }

        @NotNull
        @Override
        public final ByteComparator reversed() {
            return NaturalOrderComparator.INSTANCE;
        }
    }

    static final class NullComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1719147564587683336L;

        private final boolean nullFirst;

        @NotNull
        private final ByteComparator real;

        NullComparator(boolean nullFirst, @NotNull ByteComparator real) {
            this.nullFirst = nullFirst;
            this.real = real;
        }

        @Override
        public final int compare(byte b1, byte b2) {
            return real.compare(b1, b2);
        }

        @Override
        public final int compare(Byte b1, Byte b2) {
            if (b1 == null) {
                return (b2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return real.compare(b1, b2);
            }
        }

        @NotNull
        @Override
        public final ByteComparator nullsFirst() {
            return nullFirst ? this : new NullComparator(true, real);
        }

        @NotNull
        @Override
        public final ByteComparator nullsLast() {
            return nullFirst ? new NullComparator(false, real) : this;
        }

        @NotNull
        @Override
        public final ByteComparator reversed() {
            return new NullComparator(!nullFirst, real.reversed());
        }

    }

    static final class NaturalOrderNullComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 702754137803207007L;

        static final NaturalOrderNullComparator NULLS_FIRST = new NaturalOrderNullComparator(true);
        static final NaturalOrderNullComparator NULLS_LAST = new NaturalOrderNullComparator(false);

        private final boolean nullFirst;

        NaturalOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public final int compare(byte b1, byte b2) {
            return Byte.compare(b1, b2);
        }

        @Override
        public final int compare(Byte b1, Byte b2) {
            if (b1 == null) {
                return (b2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return b1.compareTo(b2);
            }
        }

        @NotNull
        @Override
        public final ByteComparator nullsFirst() {
            return NULLS_FIRST;
        }

        @NotNull
        @Override
        public final ByteComparator nullsLast() {
            return NULLS_LAST;
        }

        @NotNull
        @Override
        public final ByteComparator reversed() {
            return nullFirst ? ReverseOrderNullComparator.NULLS_LAST : ReverseOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }

    static final class ReverseOrderNullComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = -646304779903669659L;

        static final ReverseOrderNullComparator NULLS_FIRST = new ReverseOrderNullComparator(true);
        static final ReverseOrderNullComparator NULLS_LAST = new ReverseOrderNullComparator(false);

        private final boolean nullFirst;

        ReverseOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public final int compare(byte b1, byte b2) {
            return Byte.compare(b2, b1);
        }

        @Override
        public final int compare(Byte b1, Byte b2) {
            if (b1 == null) {
                return (b2 == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b2 == null) {
                return nullFirst ? 1 : -1;
            } else {
                return b2.compareTo(b1);
            }
        }

        @NotNull
        @Override
        public final ByteComparator nullsFirst() {
            return NULLS_FIRST;
        }

        @NotNull
        @Override
        public final ByteComparator nullsLast() {
            return NULLS_LAST;
        }

        @NotNull
        @Override
        public final ByteComparator reversed() {
            return nullFirst ? NaturalOrderNullComparator.NULLS_LAST : NaturalOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }
}
