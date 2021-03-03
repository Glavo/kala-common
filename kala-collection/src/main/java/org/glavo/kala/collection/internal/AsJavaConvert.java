package org.glavo.kala.collection.internal;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.mutable.Buffer;
import org.glavo.kala.collection.mutable.MutableCollection;
import org.glavo.kala.collection.mutable.MutableSeq;
import org.glavo.kala.collection.mutable.MutableSet;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@StaticClass
public final class AsJavaConvert {
    public static class CollectionAsJava<@Covariant E, C extends org.glavo.kala.collection.Collection<E>>
            extends java.util.AbstractCollection<E> {
        @NotNull
        protected final C collection;

        public CollectionAsJava(@NotNull C collection) {
            this.collection = collection;
        }

        @Override
        public final boolean isEmpty() {
            return collection.isEmpty();
        }

        @Override
        public final int size() {
            return collection.size();
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @Override
        public final boolean contains(Object o) {
            return collection.contains(o);
        }

        @Override
        public final boolean containsAll(@NotNull java.util.Collection<?> c) {
            return collection.containsAll(c);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return collection.toArray();
        }

        public final <T> T @NotNull [] toArray(@NotNull IntFunction<T[]> generator) {
            return collection.toArray(generator);
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
            collection.forEach(action);
        }

    }

    public static class SeqAsJava<E, C extends Seq<E>> extends AbstractList<E> implements List<E> {
        public final C collection;

        public SeqAsJava(@NotNull C collection) {
            this.collection = collection;
        }

        @Override
        public boolean addAll(int index, @NotNull java.util.Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E get(int index) {
            return collection.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return collection.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return collection.lastIndexOf(o);
        }

        @Override
        public int size() {
            return collection.size();
        }

        @Override
        public boolean isEmpty() {
            return collection.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return collection.contains(o);
        }

        @Override
        public boolean containsAll(@NotNull java.util.Collection<?> c) {
            return collection.containsAll((c));
        }

        public <T> T[] toArray(IntFunction<T[]> generator) {
            return collection.toArray(generator);
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return collection.toArray();
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public final <T> T[] toArray(@NotNull T[] a) {
            Objects.requireNonNull(a);

            T[] arr = toArray(i -> (T[]) Array.newInstance(a.getClass().getComponentType(), i));
            if (a.length < arr.length) {
                return arr;
            }
            System.arraycopy(arr, 0, a, 0, arr.length);
            return a;
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            collection.forEach(action);
        }
    }

    public static class IndexedSeqAsJava<E, C extends IndexedSeq<E>> extends SeqAsJava<E, C> implements RandomAccess {
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
        protected final C collection;

        public MutableCollectionAsJava(C collection) {
            this.collection = collection;
        }

        @Override
        public int size() {
            return collection.size();
        }

        public <T> T[] toArray(IntFunction<T[]> generator) {
            return collection.toArray(generator);
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            collection.forEach(action);
        }
    }

    public static class MutableSeqAsJava<E, C extends MutableSeq<E>>
            extends SeqAsJava<E, C> {
        public MutableSeqAsJava(@NotNull C collection) {
            super(collection);
        }

        @Override
        public E set(int index, E element) {
            E ans = collection.get(index);
            collection.set(index, element);
            return ans;
        }

        @Override
        public void sort(Comparator<? super E> c) {
            collection.sort(c);
        }

    }

    public static class MutableIndexedSeqAsJava<E, C extends MutableSeq<E> & IndexedSeq<E>>
            extends MutableSeqAsJava<E, C> implements RandomAccess {
        public MutableIndexedSeqAsJava(@NotNull C collection) {
            super(collection);
        }
    }

    public static class BufferAsJava<E, C extends Buffer<E>> extends MutableSeqAsJava<E, C> {
        public BufferAsJava(@NotNull C collection) {
            super(collection);
        }

        @Override
        public boolean add(E e) {
            collection.append(e);
            return true;
        }

        @Override
        public void add(int index, E element) {
            collection.insert(index, element);
        }

        @Override
        public boolean addAll(int index, @NotNull java.util.Collection<? extends E> c) {
            collection.insertAll(index, c);
            return !c.isEmpty();
        }

        @Override
        public E remove(int index) {
            return collection.removeAt(index);
        }

        @Override
        public void clear() {
            collection.clear();
        }
    }

    public static class IndexedBufferAsJava<E, C extends Buffer<E> & IndexedSeq<E>>
            extends BufferAsJava<E, C> implements RandomAccess {
        public IndexedBufferAsJava(@NotNull C collection) {
            super(collection);
        }
    }

    public static class MutableSetAsJava<E, C extends MutableSet<E>>
            extends SetAsJava<E, C> implements java.util.Set<E> {
        public MutableSetAsJava(C collection) {
            super(collection);
        }

        @Override
        public boolean add(E e) {
            return collection.add(e);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            return collection.remove((E) o);
        }

        @Override
        public boolean addAll(@NotNull java.util.Collection<? extends E> c) {
            return collection.addAll(c);
        }

        @Override
        public boolean removeAll(java.util.@NotNull Collection<?> c) {
            return collection.removeAll(c);
        }

        @Override
        public boolean retainAll(java.util.@NotNull Collection<?> c) {
            return super.retainAll(c);
        }

        @Override
        public final void clear() {
            collection.clear();
        }
    }

    /*
    public static class MapAsJava<K, V, C extends org.glavo.kala.collection.Map<K, V>> extends AbstractMap<K, V> {

        protected final C source;

        public MapAsJava(@NotNull C source) {
            this.source = source;
        }

        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
        }
    }
    */
}
