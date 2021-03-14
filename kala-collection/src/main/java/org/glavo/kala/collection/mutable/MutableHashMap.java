package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.immutable.AbstractImmutableMap;
import org.glavo.kala.collection.immutable.ImmutableHashMap;
import org.glavo.kala.collection.internal.hash.HashMapBase;
import org.glavo.kala.collection.internal.hash.HashMapNodeIterator;
import org.glavo.kala.collection.internal.hash.HashMapIterator;
import org.glavo.kala.collection.internal.hash.HashMapNode;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.control.Option;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static org.glavo.kala.collection.internal.hash.HashMapUtils.*;

@SuppressWarnings("unchecked")
public final class MutableHashMap<K, V> extends HashMapBase<K, V>
        implements MutableMapOps<K, V, MutableHashMap<?, ?>, MutableHashMap<K, V>>, Cloneable {
    private static final Factory<?, ?> FACTORY = new Factory<>();

    public static final int DEFAULT_INITIAL_CAPACITY = HashMapBase.DEFAULT_INITIAL_CAPACITY;
    public static final double DEFAULT_LOAD_FACTOR = HashMapBase.DEFAULT_LOAD_FACTOR;

    public MutableHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity, double loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * @see #clone()
     */
    private MutableHashMap(@NotNull MutableHashMap<K, V> old) {
        super(old);
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, MutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(K k1, V v1) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        m.set(k5, v5);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        res.set(entry5);
        return res;
    }

    @SafeVarargs
    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "MutableHashMap";
    }

    @Override
    public final @NotNull <NK, NV> MapFactory<NK, NV, ?, MutableHashMap<NK, NV>> mapFactory() {
        return MutableHashMap.factory();
    }

    @Override
    public final @NotNull MutableMapEditor<K, V, MutableHashMap<K, V>> edit() {
        return new MutableMapEditor<>(this);
    }

    @Override
    public final @NotNull java.util.Map<K, V> asJava() {
        return new AsJava<>(this);
    }

    final @NotNull MutableHashMap<K, V> shallowClone() {
        try {
            return (MutableHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull MutableHashMap<K, V> clone() {
        return new MutableHashMap<>(this);
    }

    //endregion

    private static final class Factory<K, V> extends AbstractMutableMapFactory<K, V, MutableHashMap<K, V>> {
        @Override
        public final MutableHashMap<K, V> newBuilder() {
            return new MutableHashMap<>();
        }
    }

    static final class AsJava<K, V> extends AsJavaConvert.MutableMapAsJava<K, V, MutableHashMap<K, V>> {

        public AsJava(@NotNull MutableHashMap<K, V> source) {
            super(source);
        }

        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
            return new EntrySet<>(source);
        }

        static final class EntrySet<K, V> extends AsJavaConvert.MapAsJava.EntrySet<K, V, MutableHashMap<K, V>> {
            EntrySet(MutableHashMap<K, V> source) {
                super(source);
            }

            @Override
            @SuppressWarnings("rawtypes")
            public final @NotNull Iterator<java.util.Map.Entry<K, V>> iterator() {
                return (Iterator) source.nodeIterator();
            }
        }
    }

    @ApiStatus.Internal
    public static class Frozen<K, V> extends AbstractImmutableMap<K, V> {
        protected final MutableHashMap<K, V> source;

        protected Frozen(MutableHashMap<K, V> source) {
            this.source = source;
        }

        @Override
        public final @NotNull MapIterator<K, V> iterator() {
            return source.iterator();
        }

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
            return source.knownSize();
        }

        //endregion

        @Override
        public final V get(K key) {
            return source.get(key);
        }

        @Override
        public final @Nullable V getOrNull(K key) {
            return source.getOrNull(key);
        }

        @Override
        public final @NotNull Option<V> getOption(K key) {
            return source.getOption(key);
        }

        @Override
        public final V getOrDefault(K key, V defaultValue) {
            return source.getOrDefault(key, defaultValue);
        }

        @Override
        public final V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
            return source.getOrElse(key, supplier);
        }

        @Override
        public final <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
            return source.getOrThrow(key, supplier);
        }

        @Override
        public final <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
            return source.getOrThrowException(key, exception);
        }

        //region Element Conditions

        @Override
        public final boolean contains(K key, Object value) {
            return source.contains(key, value);
        }

        @Override
        public final boolean containsKey(K key) {
            return source.containsKey(key);
        }

        @Override
        public final boolean containsValue(Object value) {
            return source.containsValue(value);
        }

        @Override
        public final boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.anyMatch(predicate);
        }

        @Override
        public final boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.allMatch(predicate);
        }

        @Override
        public final boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.noneMatch(predicate);
        }

        //endregion

        @Override
        public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
            source.forEach(consumer);
        }
    }
}
