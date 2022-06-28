package kala.collection.factory;

import kala.collection.base.AnyTraversable;
import kala.collection.base.MapBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

public interface MapFactory<K, V, Builder, R> extends Factory<Builder, R> {
    static <T, K, V, Builder, R> Collector<T, ?, R> collector(
            @NotNull MapFactory<K, V, Builder, R> factory,
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper) {
        return factory.collector(keyMapper, valueMapper);
    }

    Builder newBuilder();

    R build(Builder builder);

    void addToBuilder(Builder builder, K key, V value);

    default void addAllToBuilder(Builder builder, java.util.@NotNull Map<? extends K, ? extends V> values) {
        values.forEach((k, v) -> addToBuilder(builder, k, v));
    }

    default void addAllToBuilder(Builder builder, @NotNull MapBase<? extends K, ? extends V> values) {
        values.forEach((k, v) -> addToBuilder(builder, k, v));
    }

    Builder mergeBuilder(Builder builder1, Builder builder2);

    default <T> @NotNull Collector<T, Builder, R> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper) {
        return Collector.of(
                this::newBuilder,
                (builder, t) -> addToBuilder(builder, keyMapper.apply(t), valueMapper.apply(t)),
                this::mergeBuilder,
                this::build
        );
    }

    default void sizeHint(@NotNull Builder builder, int size) {

    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it) {
        this.sizeHint(builder, it, 0);
    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it, int delta) {
        if (it instanceof Collection<?>) {
            int s = ((Collection<?>) it).size();
            this.sizeHint(builder, s + delta);
        } else if (it instanceof AnyTraversable) {
            int ks = ((AnyTraversable<?>) it).knownSize();
            if (ks > 0) {
                this.sizeHint(builder, ks + delta);
            }
        }
    }

    default void sizeHint(@NotNull Builder builder, java.util.@NotNull Map<?, ?> it) {
        sizeHint(builder, it.size());
    }

    default void sizeHint(@NotNull Builder builder, MapBase<?, ?> it) {
        int ks = it.knownSize();
        if (ks >= 0) {
            sizeHint(builder, ks);
        }
    }

    @Override
    default <U> @NotNull MapFactory<K, V, Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final class MappedFactory implements MapFactory<K, V, Builder, U> {

            @Override
            public final Builder newBuilder() {
                return MapFactory.this.newBuilder();
            }

            @Override
            public final U build(Builder builder) {
                return mapper.apply(MapFactory.this.build(builder));
            }

            @Override
            public final void addToBuilder(Builder builder, K key, V value) {
                MapFactory.this.addToBuilder(builder, key, value);
            }

            @Override
            public final Builder mergeBuilder(Builder builder1, Builder builder2) {
                return MapFactory.this.mergeBuilder(builder1, builder2);
            }

            @Override
            public final void sizeHint(@NotNull Builder builder, int size) {
                MapFactory.this.sizeHint(builder, size);
            }
        }
        return new MappedFactory();
    }
}
