package asia.kala.collection.internal;

import asia.kala.annotations.StaticClass;
import asia.kala.collection.AbstractCollection;
import asia.kala.collection.Collection;
import asia.kala.collection.IndexedSeq;
import asia.kala.collection.Seq;
import asia.kala.collection.mutable.Buffer;
import asia.kala.collection.mutable.MutableSeq;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

@StaticClass
public final class FromJavaConvert {
    public static class CollectionFromJava<E> extends AbstractCollection<E> implements Collection<E> {
        @NotNull
        protected final java.util.Collection<E> collection;

        public CollectionFromJava(@NotNull java.util.Collection<E> collection) {
            this.collection = collection;
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return collection.iterator();
        }

        @NotNull
        @Override
        public Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @NotNull
        @Override
        public Stream<E> stream() {
            return collection.stream();
        }

        @NotNull
        @Override
        public Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @NotNull
        @Override
        public java.util.Collection<E> asJava() {
            return collection;
        }

        @Override
        public int size() {
            return collection.size();
        }

        @Override
        public int knownSize() {
            return size();
        }
    }

    public static class SeqFromJava<E> implements Seq<E> {
        @NotNull
        protected final List<E> list;

        public SeqFromJava(@NotNull List<E> list) {
            this.list = list;
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public int indexOf(Object value) {
            return list.indexOf(value);
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public int lastIndexOf(Object value) {
            return list.lastIndexOf(value);
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public boolean contains(Object value) {
            return list.contains(value);
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return list.iterator();
        }

        @NotNull
        @Override
        public Iterator<E> reverseIterator() {
            return new Iterator<E>() {
                private final java.util.ListIterator<E> it = list.listIterator(list.size());

                @Override
                public final boolean hasNext() {
                    return it.hasPrevious();
                }

                @Override
                public final E next() {
                    return it.previous();
                }
            };
        }

        @NotNull
        @Override
        public final Spliterator<E> spliterator() {
            return list.spliterator();
        }

        @NotNull
        @Override
        public final Stream<E> stream() {
            return list.stream();
        }

        @NotNull
        @Override
        public final Stream<E> parallelStream() {
            return list.parallelStream();
        }

        @NotNull
        @Override
        public List<E> asJava() {
            return list;
        }
    }

    public static class IndexedSeqFromJava<E> extends SeqFromJava<E> implements IndexedSeq<E> {
        public IndexedSeqFromJava(@NotNull List<E> list) {
            super(list);
        }
    }

    public static class MutableSeqFromJava<E>
            extends SeqFromJava<E> implements MutableSeq<E> {

        public MutableSeqFromJava(@NotNull List<E> list) {
            super(list);
        }

        @Override
        public void set(int index, E newValue) {
            list.set(index, newValue);
        }

        @Override
        public void sort(Comparator<? super E> comparator) {
            list.sort(comparator);
        }
    }

    public static class MutableIndexedSeqFromJava<E>
            extends MutableSeqFromJava<E> implements IndexedSeq<E> {
        public MutableIndexedSeqFromJava(@NotNull List<E> list) {
            super(list);
        }
    }

    public static class BufferFromJava<E>
            extends MutableSeqFromJava<E> implements Buffer<E> {
        public BufferFromJava(@NotNull List<E> list) {
            super(list);
        }

        @Override
        public void append(E value) {
            list.add(value);
        }

        @Override
        public void prepend(E value) {
            list.add(0, value);
        }

        @Override
        public void insert(int index, E value) {
            list.add(index, value);
        }

        @Override
        public E remove(int index) {
            return list.remove(index);
        }

        @Override
        public void clear() {
            list.clear();
        }
    }

    public static class IndexedBufferFromJava<E>
            extends BufferFromJava<E> implements IndexedSeq<E> {
        public IndexedBufferFromJava(@NotNull List<E> list) {
            super(list);
        }
    }
}
