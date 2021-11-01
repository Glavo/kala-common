package kala.collection.base.primitive;

import kala.function.BooleanPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

final class BooleanIterators {
    static final BooleanIterator EMPTY = new AbstractBooleanIterator() {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean nextBoolean() {
            throw new NoSuchElementException();
        }

        @Override
        public String toString() {
            return "BooleanIterator[]";
        }
    };

    static final class OfTrue extends AbstractBooleanIterator {

        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public boolean nextBoolean() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = false;
            return true;
        }
    }

    static final class OfFalse extends AbstractBooleanIterator {

        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public boolean nextBoolean() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = false;
            return false;
        }
    }

    static final class Take extends AbstractBooleanIterator {

        private final @NotNull BooleanIterator source;
        private int n;

        Take(BooleanIterator source, int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public boolean hasNext() {
            return n > 0 && source.hasNext();
        }

        @Override
        public boolean nextBoolean() {
            if (hasNext()) {
                --n;
                return source.nextBoolean();
            }
            throw new NoSuchElementException(this + ".next()" );
        }
    }

    static final class TakeWhile extends AbstractBooleanIterator {

        private @NotNull BooleanIterator source;

        private BooleanPredicate predicate;

        private boolean nextValue = false;
        private boolean tag = false;

        TakeWhile(@NotNull BooleanIterator source, BooleanPredicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
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
        public boolean nextBoolean() {
            if (hasNext()) {
                tag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException(this + ".next()" );
            }
        }
    }

    static final class Updated extends AbstractBooleanIterator {

        private final @NotNull BooleanIterator source;

        private final int n;
        private final boolean newValue;

        private int idx = 0;

        Updated(@NotNull BooleanIterator source, int n, boolean newValue) {
            this.source = source;
            this.n = n;
            this.newValue = newValue;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public boolean nextBoolean() {
            if (idx++ == n) {
                source.nextBoolean();
                return newValue;
            } else {
                return source.nextBoolean();
            }
        }
    }

    static final class Prepended extends AbstractBooleanIterator {

        private final @NotNull BooleanIterator source;

        private final boolean value;

        private boolean flag = true;

        Prepended(@NotNull BooleanIterator source, boolean value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return flag || source.hasNext();
        }

        @Override
        public boolean nextBoolean() {
            if (flag) {
                flag = false;
                return value;
            }

            return source.nextBoolean();
        }
    }

    static final class Appended extends AbstractBooleanIterator {

        private final @NotNull BooleanIterator source;

        private final boolean value;
        private boolean flag = true;

        Appended(@NotNull BooleanIterator source, boolean value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext() || flag;
        }

        @Override
        public boolean nextBoolean() {
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

        private final @NotNull BooleanIterator source;

        private final @NotNull BooleanPredicate predicate;

        private boolean nextValue = false;
        private boolean flag = false;

        private final boolean isFlipped;

        Filter(@NotNull BooleanIterator source, @NotNull BooleanPredicate predicate, boolean isFlipped) {
            this.source = source;
            this.predicate = predicate;
            this.isFlipped = isFlipped;
        }

        @Override
        public boolean hasNext() {
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

        public boolean nextBoolean() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

}
