package asia.kala.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

final class IntIterators {
    static final IntIterator EMPTY = new AbstractIntIterator() {
        @Override
        public final boolean hasNext() {
            return false;
        }

        @Override
        public final int nextInt() {
            throw new NoSuchElementException();
        }

        @Override
        public String toString() {
            return "IntIterator[]";
        }
    };

    static final class Take extends AbstractIntIterator {
        private final IntIterator source;
        private int n;

        Take(IntIterator source, int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final boolean hasNext() {
            return n > 0 && source.hasNext();
        }

        @Override
        public final int nextInt() {
            if (hasNext()) {
                --n;
                return source.nextInt();
            }
            throw new NoSuchElementException();
        }
    }

    static final class TakeWhile extends AbstractIntIterator {
        @NotNull
        private IntIterator source;

        private IntPredicate predicate;

        private int nextValue = 0;
        private boolean tag = false;

        TakeWhile(@NotNull IntIterator source, IntPredicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final boolean hasNext() {
            if (tag) {
                return true;
            }

            if (source.hasNext()) {
                int v = nextValue = source.nextInt();
                if (predicate.test(v)) {
                    tag = true;
                    return true;
                } else {
                    source = EMPTY;
                    predicate = null;
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public final int nextInt() {
            if (hasNext()) {
                tag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    static final class Updated extends AbstractIntIterator {
        @NotNull
        private final IntIterator source;

        private final int n;
        private final int newValue;

        private int idx = 0;

        Updated(@NotNull IntIterator source, int n, int newValue) {
            this.source = source;
            this.n = n;
            this.newValue = newValue;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public final int nextInt() {
            if (idx++ == n) {
                source.nextInt();
                return newValue;
            } else {
                return source.nextInt();
            }
        }
    }

    static final class Prepended extends AbstractIntIterator {
        @NotNull
        private final IntIterator source;

        private final int value;

        private boolean flag = true;

        Prepended(@NotNull IntIterator source, int value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final boolean hasNext() {
            return flag || source.hasNext();
        }

        @Override
        public final int nextInt() {
            if (flag) {
                flag = false;
                return value;
            }

            return source.nextInt();
        }
    }

    static final class Appended extends AbstractIntIterator {
        @NotNull
        private final IntIterator source;

        private final int value;
        private boolean flag = true;

        Appended(@NotNull IntIterator source, int value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext() || flag;
        }

        @Override
        public final int nextInt() {
            if (source.hasNext()) {
                return source.nextInt();
            }
            if (flag) {
                flag = false;
                return value;
            }
            throw new NoSuchElementException();
        }
    }

    static final class Filter extends AbstractIntIterator {
        @NotNull
        private final IntIterator source;
        @NotNull
        private final IntPredicate predicate;

        private int nextValue = 0;
        private boolean flag = false;

        private final boolean isFlipped;

        Filter(@NotNull IntIterator source, @NotNull IntPredicate predicate, boolean isFlipped) {
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
            int v = source.nextInt();
            while (predicate.test(v) == isFlipped) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.nextInt();
            }

            this.nextValue = v;
            flag = true;
            return true;
        }

        @Override
        public final int nextInt() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
