package asia.kala.comparator;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

final class BooleanComparators {
    enum NaturalOrderComparator implements BooleanComparator {
        INSTANCE;

        @Override
        public final int compare(boolean b1, boolean b2) {
            return Boolean.compare(b1, b2);
        }

        @NotNull
        @Override
        public final BooleanComparator nullsFirst() {
            return NaturalOrderNullComparator.NULLS_FIRST;
        }

        @NotNull
        @Override
        public final BooleanComparator nullsLast() {
            return NaturalOrderNullComparator.NULLS_LAST;
        }

        @NotNull
        @Override
        public final BooleanComparator reversed() {
            return ReverseOrderComparator.INSTANCE;
        }
    }

    enum ReverseOrderComparator implements BooleanComparator {
        INSTANCE;

        @Override
        public final int compare(boolean b1, boolean b2) {
            return Boolean.compare(b2, b1);
        }

        @NotNull
        @Override
        public final BooleanComparator nullsFirst() {
            return ReverseOrderNullComparator.NULLS_FIRST;
        }

        @NotNull
        @Override
        public final BooleanComparator nullsLast() {
            return ReverseOrderNullComparator.NULLS_LAST;
        }

        @NotNull
        @Override
        public final BooleanComparator reversed() {
            return NaturalOrderComparator.INSTANCE;
        }
    }

    static final class NullComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = -5396156551789118865L;

        private final boolean nullFirst;

        @NotNull
        private final BooleanComparator real;

        NullComparator(boolean nullFirst, @NotNull BooleanComparator real) {
            this.nullFirst = nullFirst;
            this.real = real;
        }

        @Override
        public final int compare(boolean b1, boolean b2) {
            return real.compare(b1, b2);
        }

        @Override
        public final int compare(Boolean b1, Boolean b2) {
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
        public final BooleanComparator nullsFirst() {
            return nullFirst ? this : new NullComparator(true, real);
        }

        @NotNull
        @Override
        public final BooleanComparator nullsLast() {
            return nullFirst ? new NullComparator(false, real) : this;
        }

        @NotNull
        @Override
        public final BooleanComparator reversed() {
            return new NullComparator(!nullFirst, real.reversed());
        }

    }

    static final class NaturalOrderNullComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = -846684682390742051L;

        static final NaturalOrderNullComparator NULLS_FIRST = new NaturalOrderNullComparator(true);
        static final NaturalOrderNullComparator NULLS_LAST = new NaturalOrderNullComparator(false);

        private final boolean nullFirst;

        NaturalOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public final int compare(boolean b1, boolean b2) {
            return Boolean.compare(b1, b2);
        }

        @Override
        public final int compare(Boolean b1, Boolean b2) {
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
        public final BooleanComparator nullsFirst() {
            return NULLS_FIRST;
        }

        @NotNull
        @Override
        public final BooleanComparator nullsLast() {
            return NULLS_LAST;
        }

        @NotNull
        @Override
        public final BooleanComparator reversed() {
            return nullFirst ? ReverseOrderNullComparator.NULLS_LAST : ReverseOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }

    static final class ReverseOrderNullComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = -646304779903669659L;

        static final ReverseOrderNullComparator NULLS_FIRST = new ReverseOrderNullComparator(true);
        static final ReverseOrderNullComparator NULLS_LAST = new ReverseOrderNullComparator(false);

        private final boolean nullFirst;

        ReverseOrderNullComparator(boolean nullFirst) {
            this.nullFirst = nullFirst;
        }

        @Override
        public final int compare(boolean b1, boolean b2) {
            return Boolean.compare(b2, b1);
        }

        @Override
        public final int compare(Boolean b1, Boolean b2) {
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
        public final BooleanComparator nullsFirst() {
            return NULLS_FIRST;
        }

        @NotNull
        @Override
        public final BooleanComparator nullsLast() {
            return NULLS_LAST;
        }

        @NotNull
        @Override
        public final BooleanComparator reversed() {
            return nullFirst ? NaturalOrderNullComparator.NULLS_LAST : NaturalOrderNullComparator.NULLS_FIRST;
        }

        private Object readResolve() {
            return nullFirst ? NULLS_FIRST : NULLS_LAST;
        }
    }
}
