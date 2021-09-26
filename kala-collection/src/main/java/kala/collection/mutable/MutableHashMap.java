package kala.collection.mutable;

import kala.collection.base.MapBase;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.hash.HashMapBase;
import kala.tuple.Tuple2;
import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class MutableHashMap<K, V> extends HashMapBase<K, V>
        implements MutableMapOps<K, V, MutableHashMap<?, ?>, MutableHashMap<K, V>>, Cloneable {
    private static final long serialVersionUID = 4445503260710443405L;

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

    static <T, K, V> @NotNull Collector<T, ?, MutableHashMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
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

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull MapBase<? extends K, ? extends V> values) {
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
    public @NotNull String className() {
        return "MutableHashMap";
    }

    @Override
    public @NotNull <NK, NV> MapFactory<NK, NV, ?, MutableHashMap<NK, NV>> mapFactory() {
        return MutableHashMap.factory();
    }

    @Override
    public @NotNull MutableMapEditor<K, V, MutableHashMap<K, V>> edit() {
        return new MutableMapEditor<>(this);
    }

    @Override
    public @NotNull java.util.Map<K, V> asJava() {
        return new AsJava<>(this);
    }

    @NotNull MutableHashMap<K, V> shallowClone() {
        try {
            return (MutableHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableHashMap<K, V> clone() {
        return new MutableHashMap<>(this);
    }

    //endregion

    private static final class Factory<K, V> extends AbstractMutableMapFactory<K, V, MutableHashMap<K, V>> {
        @Override
        public MutableHashMap<K, V> newBuilder() {
            return new MutableHashMap<>();
        }
    }

    private static final class AsJava<K, V> extends AsJavaConvert.MutableMapAsJava<K, V, MutableHashMap<K, V>> {

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
            public @NotNull Iterator<java.util.Map.Entry<K, V>> iterator() {
                return (Iterator) source.nodeIterator();
            }
        }
    }
}
