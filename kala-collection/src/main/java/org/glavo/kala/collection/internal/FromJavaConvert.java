package org.glavo.kala.collection.internal;

import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.mutable.Buffer;
import org.glavo.kala.collection.mutable.MutableMap;
import org.glavo.kala.collection.mutable.MutableSeq;
import org.glavo.kala.control.Option;
import org.glavo.kala.iterator.MapIterator;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.Seq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@StaticClass
public final class FromJavaConvert {
    public static class CollectionFromJava<E> extends org.glavo.kala.collection.AbstractCollection<E> implements org.glavo.kala.collection.Collection<E> {

        protected final @NotNull java.util.Collection<E> collection;

        public CollectionFromJava(@NotNull java.util.Collection<E> collection) {
            this.collection = collection;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public @NotNull Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @Override
        public @NotNull java.util.Collection<E> asJava() {
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
        public E removeAt(int index) {
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

    public static class MapFromJava<K, V> extends org.glavo.kala.collection.AbstractMap<K, V> {
        protected final java.util.Map<K, V> source;

        public MapFromJava(java.util.Map<K, V> source) {
            this.source = source;
        }

        //region Collection Operations

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.ofIterator(source.entrySet().iterator());
        }

        //endregion

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.size();
        }

        //endregion

        @Override
        public V get(K key) {
            if (!source.containsKey(key)) {
                throw new NoSuchElementException();
            }
            return source.get(key);
        }

        @Override
        public @Nullable V getOrNull(K key) {
            return source.get(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            return source.containsKey(key)
                    ? Option.some(source.get(key))
                    : Option.none();
        }

        @Override
        public boolean containsKey(K key) {
            return source.containsKey(key);
        }

        @Override
        public boolean containsValue(V value) {
            return source.containsValue(value);
        }

    }

    public static class MutableMapFromJava<K, V> extends MapFromJava<K, V> implements MutableMap<K, V> {

        public MutableMapFromJava(java.util.Map<K, V> source) {
            super(source);
        }

        @Override
        public @NotNull Option<V> put(K key, V value) {
            if (source.containsKey(key)) {
                return Option.some(source.put(key, value));
            } else {
                source.put(key, value);
                return Option.none();
            }
        }

        @Override
        public void set(K key, V value) {
            source.put(key, value);
        }

        @Override
        public void putAll(@NotNull java.util.Map<? extends K, ? extends V> m) {
            source.putAll(m);
        }

        @Override
        public @NotNull Option<V> remove(K key) {
            if (source.containsKey(key)) {
                return Option.some(source.remove(key));
            }
            return Option.none();
        }

    }
}
