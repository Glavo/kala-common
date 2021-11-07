package kala.collection.internal.convert;

import kala.collection.IndexedSeq;
import kala.collection.mutable.*;
import kala.control.Option;
import kala.internal.InternalIdentifyObject;
import kala.tuple.Tuple2;
import kala.annotations.StaticClass;
import kala.collection.base.MapIterator;
import kala.collection.Seq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

@StaticClass
public final class FromJavaConvert {
    private static final Object NULL_HOLE = new InternalIdentifyObject();

    public static class CollectionFromJava<E> extends kala.collection.AbstractCollection<E> implements kala.collection.Collection<E> {
        protected final @NotNull java.util.Collection<E> source;

        public CollectionFromJava(@NotNull java.util.Collection<E> source) {
            this.source = source;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public @NotNull java.util.Collection<E> asJava() {
            return source;
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return size();
        }
    }

    public static class SeqFromJava<E> implements Seq<E> {
        protected final @NotNull List<E> source;

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

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return new Iterator<E>() {
                private final java.util.ListIterator<E> it = source.listIterator(source.size());

                @Override
                public boolean hasNext() {
                    return it.hasPrevious();
                }

                @Override
                public E next() {
                    return it.previous();
                }
            };
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
        public final @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public @NotNull List<E> asJava() {
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

    public static class DynamicSeqFromJava<E>
            extends MutableSeqFromJava<E> implements DynamicSeq<E> {
        public DynamicSeqFromJava(@NotNull List<E> list) {
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

    public static class DynamicIndexedSeqFromJava<E>
            extends DynamicSeqFromJava<E> implements IndexedSeq<E> {
        public DynamicIndexedSeqFromJava(@NotNull List<E> list) {
            super(list);
        }
    }

    public static class SetFromJava<E> extends kala.collection.AbstractSet<E>
            implements kala.collection.Set<E> {

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

    @SuppressWarnings("unchecked")
    public static class MapFromJava<K, V> extends kala.collection.AbstractMap<K, V> {
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
            final Object res = ((Map<Object, Object>) source).getOrDefault(key, NULL_HOLE);
            if (res == NULL_HOLE) {
                throw new NoSuchElementException();
            }
            return (V) res;
        }

        @Override
        public @Nullable V getOrNull(K key) {
            return source.get(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            final Object res = ((Map<Object, Object>) source).getOrDefault(key, NULL_HOLE);
            return res == NULL_HOLE ? Option.none() : Option.some((V) res);
        }

        @Override
        public V getOrDefault(K key, V defaultValue) {
            return source.getOrDefault(key, defaultValue);
        }

        @Override
        public V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
            final Object res = ((Map<Object, Object>) source).getOrDefault(key, NULL_HOLE);
            return res == NULL_HOLE ? supplier.get() : (V) res;
        }

        @Override
        public <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
            Objects.requireNonNull(supplier);
            final Object res = ((Map<Object, Object>) source).getOrDefault(key, NULL_HOLE);
            if (res == null) {
                throw supplier.get();
            }
            return (V) res;
        }

        @Override
        public <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
            Objects.requireNonNull(exception);
            final Object res = ((Map<Object, Object>) source).getOrDefault(key, NULL_HOLE);
            if (res == null) {
                throw exception;
            }
            return (V) res;
        }

        @Override
        public boolean containsKey(K key) {
            return source.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            //noinspection SuspiciousMethodCalls
            return source.containsValue(value);
        }

    }

    public static class MutableMapFromJava<K, V> extends MapFromJava<K, V> implements MutableMap<K, V> {

        public MutableMapFromJava(java.util.Map<K, V> source) {
            super(source);
        }

        @Override
        public @NotNull Map<K, V> asJava() {
            return source;
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
        public @NotNull Option<V> putIfAbsent(K key, V value) {
            if (source.containsKey(key)) {
                return Option.some(source.put(key, value));
            } else {
                return Option.none();
            }
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
        public boolean replace(K key, V oldValue, V newValue) {
            return source.replace(key, oldValue, newValue);
        }

        @Override
        public final void clear() {
            source.clear();
        }

        @Override
        public final @NotNull MutableSet<Tuple2<K, V>> asMutableSet() {
            return new AbstractMutableSet<Tuple2<K, V>>() {
                @Override
                public boolean add(@NotNull Tuple2<K, V> value) {
                    return MutableMapFromJava.this.put(value._1, value._2).isDefined();
                }

                @Override
                @SuppressWarnings("unchecked")
                public boolean remove(Object value) {
                    if (!(value instanceof Tuple2)) {
                        return false;
                    }
                    Tuple2<K, V> tuple = (Tuple2<K, V>) value;
                    if (MutableMapFromJava.this.contains(tuple)) {
                        source.remove(tuple._1);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public void clear() {
                    source.clear();
                }

                @Override
                public @NotNull Iterator<Tuple2<K, V>> iterator() {
                    return MutableMapFromJava.this.iterator();
                }
            };
        }

        @Override
        public final void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
            source.replaceAll(function);
        }
    }
}
