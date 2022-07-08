package kala.collection.mutable;

import kala.collection.MapLike;
import kala.collection.factory.MapFactory;
import kala.collection.internal.convert.FromJavaConvert;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Collector;

public final class MutableLinkedHashMap<K, V> extends FromJavaConvert.MutableMapFromJava<K, V> {

    private static final Factory<?, ?> FACTORY = new Factory<>();

    public MutableLinkedHashMap() {
        this(new java.util.LinkedHashMap<>());
    }

    public MutableLinkedHashMap(int initialCapacity) {
        this(new java.util.LinkedHashMap<>(initialCapacity));
    }

    public MutableLinkedHashMap(int initialCapacity, double loadFactor) {
        this(new java.util.LinkedHashMap<>(initialCapacity, ((float) loadFactor)));
    }

    private MutableLinkedHashMap(java.util.@NotNull LinkedHashMap<K, V> source) {
        super(source);
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull MapFactory<K, V, ?, MutableLinkedHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    static <T, K, V> @NotNull Collector<T, ?, MutableLinkedHashMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of() {
        return new MutableLinkedHashMap<>();
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of(K k1, V v1) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.set(k1, v1);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        m.set(k5, v5);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries() {
        return new MutableLinkedHashMap<>();
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        res.set(entry1);
        return res;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        res.set(entry1);
        res.set(entry2);
        return res;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        return res;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        return res;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        res.set(entry5);
        return res;
    }

    @SafeVarargs
    public static <K, V> @NotNull MutableLinkedHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableLinkedHashMap<K, V> res = new MutableLinkedHashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K, V> @NotNull MutableLinkedHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        MutableLinkedHashMap<K, V> m = new MutableLinkedHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableLinkedHashMap";
    }

    @Override
    public @NotNull <NK, NV> MapFactory<NK, NV, ?, MutableLinkedHashMap<NK, NV>> mapFactory() {
        return factory();
    }

    @Override
    public @NotNull MutableMapEditor<K, V, MutableLinkedHashMap<K, V>> edit() {
        return new MutableMapEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableLinkedHashMap<K, V> clone() {
        return new MutableLinkedHashMap<>(new java.util.LinkedHashMap<>(this.source));
    }

    private static final class Factory<K, V> extends AbstractMutableMapFactory<K, V, MutableLinkedHashMap<K, V>> {
        @Override
        public MutableLinkedHashMap<K, V> newBuilder() {
            return new MutableLinkedHashMap<>();
        }
    }
}
