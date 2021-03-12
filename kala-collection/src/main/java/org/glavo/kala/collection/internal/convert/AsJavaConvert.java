package org.glavo.kala.collection.internal.convert;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.mutable.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
@StaticClass
public final class AsJavaConvert {
    public static class CollectionAsJava<@Covariant E, C extends org.glavo.kala.collection.Collection<E>>
            extends java.util.AbstractCollection<E> {
        protected final @NotNull C source;

        public CollectionAsJava(@NotNull C source) {
            this.source = source;
        }

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public final boolean contains(Object o) {
            return source.contains(o);
        }

        @Override
        public final boolean containsAll(@NotNull java.util.Collection<?> c) {
            return source.containsAll(c);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return source.toArray();
        }

        public final <T> T @NotNull [] toArray(@NotNull IntFunction<T[]> generator) {
            return source.toArray(generator);
        }

        @Override
        @SuppressWarnings("unchecked")
        public final <T> T @NotNull [] toArray(T @NotNull [] a) {
            Objects.requireNonNull(a);

            T[] arr = toArray(i -> (T[]) Array.newInstance(a.getClass().getComponentType(), i));
            if (a.length < arr.length) {
                return arr;
            }
            System.arraycopy(arr, 0, a, 0, arr.length);
            return a;
        }

        @Override
        public void forEach(@NotNull Consumer<? super E> action) {
            source.forEach(action);
        }

    }

    public static class SeqAsJava<E, C extends Seq<E>> extends AbstractList<E> implements List<E> {
        public final C source;

        public SeqAsJava(@NotNull C source) {
            this.source = source;
        }

        @Override
        public boolean addAll(int index, @NotNull java.util.Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E get(int index) {
            return source.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return source.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return source.lastIndexOf(o);
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return source.contains(o);
        }

        @Override
        public boolean containsAll(@NotNull java.util.Collection<?> c) {
            return source.containsAll((c));
        }

        public <T> T[] toArray(IntFunction<T[]> generator) {
            return source.toArray(generator);
        }

        @Override
        public Object @NotNull [] toArray() {
            return source.toArray();
        }

        @Override
        @SuppressWarnings("unchecked")
        public final <T> T @NotNull [] toArray(@NotNull T[] a) {
            Objects.requireNonNull(a);

            T[] arr = toArray(i -> (T[]) Array.newInstance(a.getClass().getComponentType(), i));
            if (a.length < arr.length) {
                return arr;
            }
            System.arraycopy(arr, 0, a, 0, arr.length);
            return a;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return source.stream();
        }

        @Override
        public Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            source.forEach(action);
        }
    }

    public static class IndexedSeqAsJava<E, C extends Seq<E>> extends SeqAsJava<E, C> implements RandomAccess {
        public IndexedSeqAsJava(@NotNull C collection) {
            super(collection);
        }
    }

    @SuppressWarnings("NullableProblems")
    public static class SetAsJava<E, C extends org.glavo.kala.collection.Set<E>> extends CollectionAsJava<E, C> implements java.util.Set<E> {
        public SetAsJava(@NotNull C collection) {
            super(collection);
        }
    }

    public static class SortedSetAsJava<E, C extends org.glavo.kala.collection.Set<E>> extends CollectionAsJava<E, C> {
        public SortedSetAsJava(@NotNull C collection) {
            super(collection);
        }
    }

    public static class MutableCollectionAsJava<E, C extends MutableCollection<E>>
            extends java.util.AbstractCollection<E> {
        protected final C source;

        public MutableCollectionAsJava(C source) {
            this.source = source;
        }

        @Override
        public int size() {
            return source.size();
        }

        public <T> T[] toArray(IntFunction<T[]> generator) {
            return source.toArray(generator);
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return source.stream();
        }

        @Override
        public Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            source.forEach(action);
        }
    }

    public static class MutableSeqAsJava<E, C extends MutableSeq<E>>
            extends SeqAsJava<E, C> {
        public MutableSeqAsJava(@NotNull C source) {
            super(source);
        }

        @Override
        public E set(int index, E element) {
            E ans = source.get(index);
            source.set(index, element);
            return ans;
        }

        @Override
        public void sort(Comparator<? super E> c) {
            source.sort(c);
        }

    }

    public static class MutableIndexedSeqAsJava<E, C extends MutableSeq<E>>
            extends MutableSeqAsJava<E, C> implements RandomAccess {
        public MutableIndexedSeqAsJava(@NotNull C source) {
            super(source);
        }
    }

    public static class BufferAsJava<E, C extends Buffer<E>> extends MutableSeqAsJava<E, C> {
        public BufferAsJava(@NotNull C source) {
            super(source);
        }

        @Override
        public boolean add(E e) {
            source.append(e);
            return true;
        }

        @Override
        public void add(int index, E element) {
            source.insert(index, element);
        }

        @Override
        public boolean addAll(int index, @NotNull java.util.Collection<? extends E> c) {
            source.insertAll(index, c);
            return !c.isEmpty();
        }

        @Override
        public E remove(int index) {
            return source.removeAt(index);
        }

        @Override
        public void clear() {
            source.clear();
        }
    }

    public static class IndexedBufferAsJava<E, C extends Buffer<E>>
            extends BufferAsJava<E, C> implements RandomAccess {
        public IndexedBufferAsJava(@NotNull C source) {
            super(source);
        }
    }

    public static class MutableSetAsJava<E, C extends MutableSet<E>>
            extends SetAsJava<E, C> implements java.util.Set<E> {
        public MutableSetAsJava(C source) {
            super(source);
        }

        @Override
        public boolean add(E e) {
            return source.add(e);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            return source.remove((E) o);
        }

        @Override
        public boolean addAll(@NotNull java.util.Collection<? extends E> c) {
            return source.addAll(c);
        }

        @Override
        public boolean removeAll(java.util.@NotNull Collection<?> c) {
            return source.removeAll(c);
        }

        @Override
        public boolean retainAll(java.util.@NotNull Collection<?> c) {
            return super.retainAll(c);
        }

        @Override
        public final void clear() {
            source.clear();
        }
    }

    public static class MapAsJava<K, V, C extends org.glavo.kala.collection.Map<K, V>> extends AbstractMap<K, V> {

        public final C source;

        public MapAsJava(@NotNull C source) {
            this.source = source;
        }

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final boolean containsKey(Object key) {
            try {
                return source.containsKey((K) key);
            } catch (ClassCastException ignored) {
                return false;
            }
        }

        @Override
        public boolean containsValue(Object value) {
            try {
                return source.containsValue((V) value);
            } catch (ClassCastException ignored) {
                return false;
            }
        }

        @Override
        public final V get(Object key) {
            try {
                return source.getOrNull((K) key);
            } catch (ClassCastException ignored) {
                return null;
            }
        }


        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
            return new EntrySet<>(source);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public static class EntrySet<K, V, M extends org.glavo.kala.collection.Map<K, V>>
                extends AbstractSet<Entry<K, V>> {
            protected final M source;

            public EntrySet(M source) {
                this.source = source;
            }

            @Override
            public @NotNull Iterator<Entry<K, V>> iterator() {
                return ((Iterator) source.iterator());
            }

            @Override
            public final boolean isEmpty() {
                return source.isEmpty();
            }

            @Override
            public final int size() {
                return source.size();
            }

            @Override
            public final boolean contains(Object o) {
                if (!(o instanceof Entry<?, ?>)) {
                    return false;
                }
                Entry<?, ?> entry = (Entry<?, ?>) o;
                try {
                    return source.getOption((K) entry.getKey()).contains(entry.getValue());
                } catch (Throwable e) {
                    return false;
                }
            }
        }
    }

    public static class MutableMapAsJava<K, V, C extends MutableMap<K, V>> extends MapAsJava<K, V, C> {
        public MutableMapAsJava(@NotNull C source) {
            super(source);
        }

        @Override
        public @Nullable V put(K key, V value) {
            return source.put(key, value).getOrNull();
        }

        @Override
        public void putAll(@NotNull Map<? extends K, ? extends V> m) {
            source.putAll(m);
        }

        @Override
        public V remove(Object key) {
            return source.remove(((K) key)).getOrNull();
        }

        @Override
        public void clear() {
            source.clear();
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            return source.replace(key, oldValue, newValue);
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            source.replaceAll(function);
        }
    }
}
