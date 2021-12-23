package kala.collection.internal;

import kala.collection.AbstractSeqIterator;
import kala.collection.SeqIterator;
import kala.collection.SeqLike;
import kala.collection.base.AbstractIterator;
import kala.collection.mutable.MutableList;
import kala.collection.mutable.MutableListIterator;
import kala.collection.mutable.MutableSeq;
import kala.collection.mutable.MutableSeqIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public final class SeqIterators {
    private SeqIterators() {
    }

    public static final SeqIterator<?> EMPTY = new EmptySeqIterator<>();
    public static final MutableSeqIterator<?> EMPTY_MUTABLE = new EmptyMutableSeqIterator<>();

    private static class EmptySeqIterator<E> extends AbstractIterator<E> implements SeqIterator<E> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return false;
        }

        public E previous() {
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }
    }

    private static class EmptyMutableSeqIterator<E> extends EmptySeqIterator<E> implements MutableSeqIterator<E> {
        @Override
        public void set(E e) {
            throw new IllegalStateException();
        }
    }

    public static class DefaultSeqIterator<E, S extends SeqLike<E>> extends AbstractSeqIterator<E> {
        protected final @NotNull S seq;

        protected int lastRet = -1;

        public DefaultSeqIterator(@NotNull S seq, int index) {
            super(index);
            this.seq = seq;
        }

        @Override
        public boolean hasNext() {
            return cursor != seq.size();
        }

        @Override
        public E next() {
            try {
                int i = cursor;
                E next = seq.get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            try {
                int i = cursor - 1;
                E previous = seq.get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }
    }

    public static class DefaultMutableSeqIterator<E, S extends MutableSeq<E>> extends DefaultSeqIterator<E, S>
            implements MutableSeqIterator<E> {
        public DefaultMutableSeqIterator(@NotNull S seq, int index) {
            super(seq, index);
        }


        @Override
        public void set(E e) {
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

    public static class DefaultMutableListIterator<E, S extends MutableList<E>> extends DefaultMutableSeqIterator<E, S>
            implements MutableListIterator<E> {
        public DefaultMutableListIterator(@NotNull S seq, int index) {
            super(seq, index);
        }


        @Override
        public void add(E e) {
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

    public static class FrozenSeqIterator<E> extends AbstractIterator<E> implements SeqIterator<E> {
        private final @NotNull SeqIterator<E> source;

        public FrozenSeqIterator(@NotNull SeqIterator<E> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public E next() {
            return source.next();
        }

        @Override
        public boolean hasPrevious() {
            return source.hasPrevious();
        }

        @Override
        public E previous() {
            return source.previous();
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
            return "FrozenSeqIterator[" + source + ']';
        }
    }
}
