package kala.collection.base.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
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
<#if Type == 'Char'>

    static final class OfString extends Abstract${Type}Iterator {
        private final String str;
        private int idx = 0;

        OfString(String str) {
            this.str = str;
        }

        @Override
        public boolean hasNext() {
            return idx < str.length();
        }

        @Override
        public char nextChar() {
            if (idx < str.length())
                return str.charAt(idx);
            throw new NoSuchElementException();
        }
    }
</#if>

    static final class Concat extends Abstract${Type}Iterator {
        private ${Type}Iterator it1;
        private ${Type}Iterator it2;

        Concat(${Type}Iterator it1, ${Type}Iterator it2) {
            this.it1 = it1;
            this.it2 = it2;
        }

        @Override
        public boolean hasNext() {
            if (it1 != null) {
                if (it1.hasNext()) {
                    return true;
                } else {
                    it1 = null;
                }
            }
            if (it2 != null) {
                if (it2.hasNext()) {
                    return true;
                } else {
                    it2 = null;
                }
            }
            return false;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (!hasNext())
                throw new NoSuchElementException();

            if (it1 != null)
                return it1.next${Type}();

            if (it2 != null)
                return it2.next${Type}();

            throw new AssertionError(); // never
        }
    }

    static final class ConcatAll extends Abstract${Type}Iterator {
        private final @NotNull Iterator<? extends ${Type}Iterator> iterators;

        private ${Type}Iterator current = null;

        ConcatAll(@NotNull Iterator<? extends ${Type}Iterator> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            while ((current == null || !current.hasNext()) && iterators.hasNext()) {
                current = iterators.next();
            }
            return current != null && current.hasNext();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (!hasNext()) throw new NoSuchElementException(this + ".next()");

            return current.next${Type}();
        }
    }

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

        private ${PrimitiveType} nextValue;
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
        private final @NotNull ${Type}Iterator source;

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
        private final @NotNull ${Type}Iterator source;

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
        private final @NotNull ${Type}Iterator source;
        private final @NotNull ${Type}Predicate predicate;

        private ${PrimitiveType} nextValue;
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
