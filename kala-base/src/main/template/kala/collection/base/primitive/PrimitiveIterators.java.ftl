package kala.collection.base.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
<#if IsSpecialized>
import java.util.function.*;
<#else>
import kala.function.*;
</#if>

final class ${Type}Iterators {

    static final ${Type}Iterator EMPTY = new Abstract${Type}Iterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            throw new NoSuchElementException();
        }

        @Override
        public String toString() {
            return "${Type}Iterator[]";
        }
    };

    static final class Take extends Abstract${Type}Iterator {
        private final ${Type}Iterator source;
        private int n;

        Take(${Type}Iterator source, int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public boolean hasNext() {
            return n > 0 && source.hasNext();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (hasNext()) {
                --n;
                return source.next${Type}();
            }
            throw new NoSuchElementException();
        }
    }

    static final class TakeWhile extends Abstract${Type}Iterator {
        private @NotNull ${Type}Iterator source;

        private ${Type}Predicate predicate;

        private ${PrimitiveType} nextValue = 0;
        private boolean tag = false;

        TakeWhile(@NotNull ${Type}Iterator source, ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (tag) {
                return true;
            }

            if (source.hasNext()) {
                ${PrimitiveType} v = nextValue = source.next${Type}();
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
        public ${PrimitiveType} next${Type}() {
            if (hasNext()) {
                tag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    static final class Updated extends Abstract${Type}Iterator {
        private final @NotNull ${Type}Iterator source;

        private final int n;
        private final ${PrimitiveType} newValue;

        private int idx = 0;

        Updated(@NotNull ${Type}Iterator source, int n, ${PrimitiveType} newValue) {
            this.source = source;
            this.n = n;
            this.newValue = newValue;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (idx++ == n) {
                source.next${Type}();
                return newValue;
            } else {
                return source.next${Type}();
            }
        }
    }

    static final class Prepended extends Abstract${Type}Iterator {
        @NotNull
        private final ${Type}Iterator source;

        private final ${PrimitiveType} value;

        private boolean flag = true;

        Prepended(@NotNull ${Type}Iterator source, ${PrimitiveType} value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return flag || source.hasNext();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (flag) {
                flag = false;
                return value;
            }

            return source.next${Type}();
        }
    }

    static final class Appended extends Abstract${Type}Iterator {
        @NotNull
        private final ${Type}Iterator source;

        private final ${PrimitiveType} value;
        private boolean flag = true;

        Appended(@NotNull ${Type}Iterator source, ${PrimitiveType} value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext() || flag;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (source.hasNext()) {
                return source.next${Type}();
            }
            if (flag) {
                flag = false;
                return value;
            }
            throw new NoSuchElementException();
        }
    }

    static final class Filter extends Abstract${Type}Iterator {
        @NotNull
        private final ${Type}Iterator source;
        @NotNull
        private final ${Type}Predicate predicate;

        private ${PrimitiveType} nextValue = 0;
        private boolean flag = false;

        private final boolean isFlipped;

        Filter(@NotNull ${Type}Iterator source, @NotNull ${Type}Predicate predicate, boolean isFlipped) {
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
            ${PrimitiveType} v = source.next${Type}();
            while (predicate.test(v) == isFlipped) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.next${Type}();
            }

            this.nextValue = v;
            flag = true;
            return true;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
