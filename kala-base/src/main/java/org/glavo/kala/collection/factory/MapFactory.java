package org.glavo.kala.collection.factory;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface MapFactory<K, V, Builder, R> extends Factory<Builder, R> {
    Builder newBuilder();

    R build(Builder builder);

    void addToBuilder(Builder builder, K key, V value);

    Builder mergeBuilder(Builder builder1, Builder builder2);

    default void sizeHint(@NotNull Builder builder, int size) {

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
