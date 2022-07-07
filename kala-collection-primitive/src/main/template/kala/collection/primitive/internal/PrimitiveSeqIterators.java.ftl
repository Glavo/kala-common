package kala.collection.primitive.internal;

import kala.collection.base.primitive.Abstract${Type}Iterator;
import kala.collection.mutable.MutableSeqIterator;
import kala.collection.mutable.primitive.Mutable${Type}List;
import kala.collection.mutable.primitive.Mutable${Type}ListIterator;
import kala.collection.mutable.primitive.Mutable${Type}Seq;
import kala.collection.mutable.primitive.Mutable${Type}SeqIterator;
import kala.collection.primitive.Abstract${Type}SeqIterator;
import kala.collection.primitive.${Type}SeqIterator;
import kala.collection.primitive.${Type}SeqLike;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public final class ${Type}SeqIterators {
    private ${Type}SeqIterators() {
    }

    public static final ${Type}SeqIterator EMPTY = new Empty${Type}SeqIterator();
    public static final Mutable${Type}SeqIterator EMPTY_MUTABLE = new EmptyMutable${Type}SeqIterator();

    private static class Empty${Type}SeqIterator extends Abstract${Type}Iterator implements ${Type}SeqIterator {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return false;
        }

        public ${PrimitiveType} previous${Type}() {
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }
    }

    private static class EmptyMutable${Type}SeqIterator extends Empty${Type}SeqIterator implements Mutable${Type}SeqIterator {
        @Override
        public void set(${PrimitiveType} e) {
            throw new IllegalStateException();
        }
    }

    public static class Default${Type}SeqIterator<S extends ${Type}SeqLike> extends Abstract${Type}SeqIterator {
        protected final @NotNull S seq;

        protected int lastRet = -1;

        public Default${Type}SeqIterator(@NotNull S seq, int index) {
            super(index);
            this.seq = seq;
        }

        @Override
        public boolean hasNext() {
            return cursor != seq.size();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            try {
                int i = cursor;
                ${PrimitiveType} next = seq.get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public ${PrimitiveType} previous${Type}() {
            try {
                int i = cursor - 1;
                ${PrimitiveType} previous = seq.get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }
    }

    public static class DefaultMutable${Type}SeqIterator<S extends Mutable${Type}Seq> extends Default${Type}SeqIterator<S>
            implements Mutable${Type}SeqIterator {
        public DefaultMutable${Type}SeqIterator(@NotNull S seq, int index) {
            super(seq, index);
        }

        @Override
        public void set(${PrimitiveType} e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                seq.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public static class DefaultMutable${Type}ListIterator<S extends Mutable${Type}List> extends DefaultMutable${Type}SeqIterator<S>
            implements Mutable${Type}ListIterator {
        public DefaultMutable${Type}ListIterator(@NotNull S seq, int index) {
            super(seq, index);
        }

        @Override
        public void add(${PrimitiveType} e) {
            try {
                int i = cursor;
                seq.insert(i, e);
                lastRet = -1;
                cursor = i + 1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                seq.removeAt(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public static final class Frozen${Type}SeqIterator implements ${Type}SeqIterator {
        private final @NotNull ${Type}SeqIterator source;

        public Frozen${Type}SeqIterator(@NotNull ${Type}SeqIterator source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            return source.next${Type}();
        }

        @Override
        public boolean hasPrevious() {
            return source.hasPrevious();
        }

        @Override
        public ${PrimitiveType} previous${Type}() {
            return source.previous${Type}();
        }

        @Override
        public int nextIndex() {
            return source.nextIndex();
        }

        @Override
        public int previousIndex() {
            return source.previousIndex();
        }

        @Override
        public String toString() {
            return "Frozen${Type}SeqIterator[" + source + ']';
        }
    }
}
