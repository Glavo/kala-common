package asia.kala.iterator;

import asia.kala.function.BooleanPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

final class BooleanIterators {
    static final BooleanIterator EMPTY = new AbstractBooleanIterator() {
        @Override
        public final boolean hasNext() {
            return false;
        }

        @Override
        public final boolean nextBoolean() {
            throw new NoSuchElementException();
        }

        @Override
        public final String toString() {
            return "BooleanIterator[]";
        }
    };

    static final class OfArray extends AbstractBooleanIterator {
        private boolean[] values;
        private int idx;

        OfArray(boolean[] values) {
            this(values, 0);
        }

        OfArray(boolean[] values, int idx) {
            this.values = values;
            this.idx = idx;
        }

        @Override
        public final boolean hasNext() {
            final boolean[] values = this.values;
            return values != null && idx < values.length;
        }

        @Override
        public final boolean nextBoolean() {
            final boolean[] values = this.values;
            final int idx = this.idx;

            if (values != null && idx < values.length) {
                boolean value = values[idx];
                if (idx >= values.length - 1) {
                    this.values = null;
                } else {
                    ++this.idx;
                }
                return value;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public final String toString() {
            int idx = this.idx;
            final boolean[] values = this.values;
            final int length = values.length;

            if (idx < length) {
                StringBuilder builder = new StringBuilder(7 * (length - idx) + 15);
                builder.append("BooleanIterator[");
                builder.append(values[idx++]);
                while (idx < length) {
                    builder.append(", ").append(values[idx++]);
                }
                builder.append(']');
                return builder.toString();
            } else {
                return "BooleanIterator[]";
            }
        }
    }

    static final class Take extends AbstractBooleanIterator {
        private final BooleanIterator source;
        private int n;

        Take(BooleanIterator source, int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final boolean hasNext() {
            return n > 0 && source.hasNext();
        }

        @Override
        public final boolean nextBoolean() {
            if (hasNext()) {
                --n;
                return source.nextBoolean();
            }
            throw new NoSuchElementException(this + ".next()");
        }
    }

    static final class TakeWhile extends AbstractBooleanIterator {
        @NotNull
        private BooleanIterator source;

        private BooleanPredicate predicate;

        private boolean nextValue = false;
        private boolean tag = false;

        TakeWhile(@NotNull BooleanIterator source, BooleanPredicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final boolean hasNext() {
            if (tag) {
                return true;
            }

            if (source.hasNext()) {
                boolean v = nextValue = source.nextBoolean();
                if (predicate.test(v)) {
                    tag = true;
                    return true;
                } else {
                    source = BooleanIterator.empty();
                    predicate = null;
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public final boolean nextBoolean() {
            if (hasNext()) {
                tag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException(this + ".next()");
            }
        }
    }

    static final class Updated extends AbstractBooleanIterator {
        @NotNull
        private final BooleanIterator source;

        private final int n;
        private final boolean newValue;

        private int idx = 0;

        Updated(@NotNull BooleanIterator source, int n, boolean newValue) {
            this.source = source;
            this.n = n;
            this.newValue = newValue;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public final boolean nextBoolean() {
            if (idx++ == n) {
                source.nextBoolean();
                return newValue;
            } else {
                return source.nextBoolean();
            }
        }
    }

    static final class Prepended extends AbstractBooleanIterator {
        @NotNull
        private final BooleanIterator source;

        private final boolean value;

        private boolean flag = true;

        Prepended(@NotNull BooleanIterator source, boolean value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final boolean hasNext() {
            return flag || source.hasNext();
        }

        @Override
        public final boolean nextBoolean() {
            if (flag) {
                flag = false;
                return value;
            }

            return source.nextBoolean();
        }
    }

    static final class Appended extends AbstractBooleanIterator {
        @NotNull
        private final BooleanIterator source;

        private final boolean value;
        private boolean flag = true;

        Appended(@NotNull BooleanIterator source, boolean value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext() || flag;
        }

        @Override
        public final boolean nextBoolean() {
            if (source.hasNext()) {
                return source.nextBoolean();
            }
            if (flag) {
                flag = false;
                return value;
            }
            throw new NoSuchElementException();
        }
    }

    static final class Filter extends AbstractBooleanIterator {
        @NotNull
        private final BooleanIterator source;
        @NotNull
        private final BooleanPredicate predicate;

        private boolean nextValue = false;
        private boolean flag = false;

        private final boolean isFlipped;

        Filter(@NotNull BooleanIterator source, @NotNull BooleanPredicate predicate, boolean isFlipped) {
            this.source = source;
            this.predicate = predicate;
            this.isFlipped = isFlipped;
        }

        @Override
        public final boolean hasNext() {
            if (flag) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }
            boolean v = source.nextBoolean();
            while (predicate.test(v) == isFlipped) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.nextBoolean();
            }

            this.nextValue = v;
            flag = true;
            return true;
        }

        @Override

        public final boolean nextBoolean() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

}
