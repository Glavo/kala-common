package org.glavo.kala.collection.internal;

import org.glavo.kala.Tuple2;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.mutable.*;
import org.glavo.kala.control.Option;
import org.glavo.kala.collection.base.MapIterator;
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
        protected final List<E> source;

        public SeqFromJava(@NotNull List<E> source) {
            this.source = source;
        }

        @Override
        public E get(int index) {
            return source.get(index);
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public int indexOf(Object value) {
            return source.indexOf(value);
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public int lastIndexOf(Object value) {
            return source.lastIndexOf(value);
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public boolean contains(Object value) {
            return source.contains(value);
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return source.iterator();
        }

        @NotNull
        @Override
        public Iterator<E> reverseIterator() {
            return new Iterator<E>() {
                private final java.util.ListIterator<E> it = source.listIterator(source.size());

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
            return source.spliterator();
        }

        @NotNull
        @Override
        public final Stream<E> stream() {
            return source.stream();
        }

        @NotNull
        @Override
        public final Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @NotNull
        @Override
        public List<E> asJava() {
            return source;
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
            source.set(index, newValue);
        }

        @Override
        public void sort(Comparator<? super E> comparator) {
            source.sort(comparator);
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
            source.add(value);
        }

        @Override
        public void prepend(E value) {
            source.add(0, value);
        }

        @Override
        public void insert(int index, E value) {
            source.add(index, value);
        }

        @Override
        public E removeAt(int index) {
            return source.remove(index);
        }

        @Override
        public void clear() {
            source.clear();
        }
    }

    public static class IndexedBufferFromJava<E>
            extends BufferFromJava<E> implements IndexedSeq<E> {
        public IndexedBufferFromJava(@NotNull List<E> list) {
            super(list);
        }
    }

    public static class SetFromJava<E> extends org.glavo.kala.collection.AbstractSet<E>
            implements org.glavo.kala.collection.Set<E> {

        protected final @NotNull java.util.Set<E> source;

        public SetFromJava(@NotNull java.util.Set<E> source) {
            this.source = source;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        @SuppressWarnings("SuspiciousMethodCalls")
        public final boolean contains(Object value) {
            return source.contains(value);
        }
    }

    public static class MutableSetFromJava<E> extends SetFromJava<E> implements MutableSet<E> {

        public MutableSetFromJava(@NotNull Set<E> source) {
            super(source);
        }

        @Override
        public final boolean add(E value) {
            return source.add(value);
        }

        @Override
        public final boolean remove(Object value) {
            //noinspection SuspiciousMethodCalls
            return source.remove(value);
        }

        @Override
        public final void clear() {
            source.clear();
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

        @Override
        public final void clear() {
            source.clear();
        }

        @Override
        public final @NotNull MutableSet<Tuple2<K, V>> asMutableSet() {
            return new AbstractMutableSet<Tuple2<K, V>>() {
                @Override
                public final boolean add(@NotNull Tuple2<K, V> value) {
                    return MutableMapFromJava.this.put(value._1, value._2).isDefined();
                }

                @Override
                @SuppressWarnings("unchecked")
                public final boolean remove(Object value) {
                    if (!(value instanceof Tuple2)) {
                        return false;
                    }
                    Tuple2<K, V> tuple = (Tuple2<K, V>) value;
                    if (MutableMapFromJava.this.getOption(tuple._1).contains(tuple._2)) {
                        source.remove(tuple._1);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public final void clear() {
                    source.clear();
                }

                @Override
                public final @NotNull Iterator<Tuple2<K, V>> iterator() {
                    return MutableMapFromJava.this.iterator();
                }
            };
        }
    }
}
