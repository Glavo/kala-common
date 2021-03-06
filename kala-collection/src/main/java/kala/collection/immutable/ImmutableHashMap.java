package kala.collection.immutable;

import kala.control.Option;
import kala.collection.base.MapBase;
import kala.collection.base.MapIterator;
import kala.collection.factory.MapFactory;
import kala.collection.internal.hash.HashMapBase;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class ImmutableHashMap<K, V> extends AbstractImmutableMap<K, V>
        implements ImmutableMapOps<K, V, ImmutableHashMap<?, ?>, ImmutableHashMap<K, V>> {

    private static final ImmutableHashMap<?, ?> EMPTY = new ImmutableHashMap<>(new Impl<>());
    private static final Factory<?, ?> FACTORY = new Factory<>();

    final Impl<K, V> source;

    private ImmutableHashMap(Impl<K, V> source) {
        this.source = source;
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, ImmutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    static <T, K, V> @NotNull Collector<T, ?, ImmutableHashMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull ImmutableHashMap<K, V> empty() {
        return (ImmutableHashMap<K, V>) EMPTY;
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(K k1, V v1) {
        Impl<K, V> impl = new Impl<>();
        impl.set(k1, v1);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        impl.set(k4, v4);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        impl.set(k4, v4);
        impl.set(k5, v5);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(entry1);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(entry1);
        impl.set(entry2);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        impl.set(entry4);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        Impl<K, V> impl = new Impl<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        impl.set(entry4);
        impl.set(entry5);
        return new ImmutableHashMap<>(impl);
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        Impl<K, V> impl = new Impl<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            impl.set(entry);
        }
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }
        Impl<K, V> impl = new Impl<>();
        impl.putAll(values);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull MapBase<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }
        Impl<K, V> impl = new Impl<>();
        impl.putAll(values);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }
        Impl<K, V> impl = new Impl<>();
        for (Map.Entry<? extends K, ? extends V> value : values) {
            impl.set(value.getKey(), value.getValue());
        }
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        Iterator<? extends Map.Entry<? extends K, ? extends V>> it = values.iterator();
        if (!it.hasNext()) {
            return empty();
        }
        Impl<K, V> impl = new Impl<>();
        while (it.hasNext()) {
            Map.Entry<? extends K, ? extends V> entry = it.next();
            impl.set(entry.getKey(), entry.getValue());
        }
        return new ImmutableHashMap<>(impl);
    }

    //endregion

    @Override
    public final @NotNull String className() {
        return "ImmutableHashMap";
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

    @Override
    public final @NotNull ImmutableHashMap<K, V> updated(K key, V value) {
        if (source.contains(key, value)) {
            return this;
        }
        Impl<K, V> nm = source.clone();
        nm.put(key, value);
        return new ImmutableHashMap<>(nm);
    }

    @Override
    public final @NotNull ImmutableHashMap<K, V> removed(K key) {
        if (!source.containsKey(key)) {
            return this;
        }
        Impl<K, V> nm = source.clone();
        nm.remove(key);
        return nm.isEmpty() ? empty() : new ImmutableHashMap<>(nm);
    }

    private static final class Factory<K, V> implements MapFactory<K, V, Builder<K, V>, ImmutableHashMap<K, V>> {
        @Override
        public final Builder<K, V> newBuilder() {
            return new Builder<>();
        }

        @Override
        public final ImmutableHashMap<K, V> build(Builder<K, V> builder) {
            return builder.build();
        }

        @Override
        public final void addToBuilder(Builder<K, V> builder, K key, V value) {
            builder.add(key, value);
        }

        @Override
        public final Builder<K, V> mergeBuilder(Builder<K, V> builder1, Builder<K, V> builder2) {
            return builder1.merge(builder2);
        }

        @Override
        public final void sizeHint(@NotNull Builder<K, V> builder, int size) {
            builder.sizeHint(size);
        }
    }

    static final class Impl<K, V> extends HashMapBase<K, V> {
        protected Impl() {
            super(HashMapBase.DEFAULT_INITIAL_CAPACITY, HashMapBase.DEFAULT_LOAD_FACTOR);
        }

        public Impl(@NotNull HashMapBase<K, V> old) {
            super(old);
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public final Impl<K, V> clone() {
            return new Impl<>(this);
        }
    }

    static final class Builder<K, V> {
        Impl<K, V> impl = new Impl<>();
        boolean aliased = false;

        private void ensureUnaliased() {
            if (aliased) {
                impl = impl.clone();
            }
        }

        final void add(K key, V value) {
            ensureUnaliased();
            impl.set(key, value);
        }

        final Builder<K, V> merge(Builder<K, V> other) {
            ensureUnaliased();
            this.impl.putAll(other.impl);
            return this;
        }

        final void sizeHint(int size) {
            ensureUnaliased();
            impl.sizeHint(size);
        }

        final ImmutableHashMap<K, V> build() {
            if (impl.isEmpty()) {
                return ImmutableHashMap.empty();
            }
            aliased = true;
            return new ImmutableHashMap<>(impl);
        }
    }
}