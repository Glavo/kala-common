package kala.collection.factory;

import kala.annotations.Covariant;
import kala.collection.base.AnyTraversable;
import kala.collection.base.Traversable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public interface CollectionFactory<E, Builder, @Covariant R>
        extends Factory<Builder, R>, Collector<E, Builder, R> {

    @SuppressWarnings("unchecked")
    static <E, Builder, R> CollectionFactory<E, Builder, R> narrow(CollectionFactory<E, Builder, ? extends R> factory) {
        return (CollectionFactory<E, Builder, R>) factory;
    }

    @Contract(pure = true)
    static <E, Builder, R> @NotNull CollectionFactory<E, Builder, R> ofCollector(
            @NotNull Collector<E, Builder, R> collector
    ) {
        Objects.requireNonNull(collector);
        if (collector instanceof CollectionFactory<?, ?, ?>) {
            return ((CollectionFactory<E, Builder, R>) collector);
        }
        return new CollectionFactory<E, Builder, R>() {
            @Override
            public Builder newBuilder() {
                return collector.supplier().get();
            }

            @Override
            public R build(@NotNull Builder builder) {
                return collector.finisher().apply(builder);
            }

            @Override
            public void addToBuilder(@NotNull Builder builder, E value) {
                collector.accumulator().accept(builder, value);
            }

            @Override
            public Builder mergeBuilder(@NotNull Builder builder1, @NotNull Builder builder2) {
                return collector.combiner().apply(builder1, builder2);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return collector.characteristics();
            }
        };
    }

    @ApiStatus.Experimental
    static <E, Builder, R> R buildBy(@NotNull CollectionFactory<E, Builder, R> factory, Consumer<Consumer<E>> consumer) {
        Builder builder = factory.newBuilder();
        consumer.accept(value -> factory.addToBuilder(builder, value));
        return factory.build(builder);
    }

    Builder newBuilder();

    R build(Builder builder);

    void addToBuilder(@NotNull Builder builder, E value);

    default void addAllToBuilder(@NotNull Builder builder, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        for (E value : values) {
            addToBuilder(builder, value);
        }
    }

    default void addAllToBuilder(@NotNull Builder builder, @NotNull Iterator<? extends E> it) {
        while (it.hasNext()) {
            addToBuilder(builder, it.next());
        }
    }

    default void addAllToBuilder(@NotNull Builder builder, E @NotNull [] values) {
        Objects.requireNonNull(values);
        for (E value : values) {
            addToBuilder(builder, value);
        }
    }

    Builder mergeBuilder(@NotNull Builder builder1, @NotNull Builder builder2);

    default void sizeHint(@NotNull Builder builder, int size) {

    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it) {
        this.sizeHint(builder, it, 0);
    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it, int delta) {
        Objects.requireNonNull(it);

        if (it instanceof Collection<?>) {
            int s = ((Collection<?>) it).size();
            this.sizeHint(builder, s + delta);
        } else if (it instanceof AnyTraversable) {
            final int ks = ((AnyTraversable<?>) it).knownSize();
            if (ks >= 0) {
                this.sizeHint(builder, ks + delta);
            }
        }
    }

    default void sizeHint(@NotNull Builder builder, @NotNull AnyTraversable<?> it) {
        this.sizeHint(builder, it, 0);
    }

    default void sizeHint(@NotNull Builder builder, @NotNull AnyTraversable<?> it, int delta) {
        Objects.requireNonNull(it);
        final int ks = it.knownSize();
        if (ks >= 0) {
            this.sizeHint(builder, ks + delta);
        }
    }

    default void sizeHint(@NotNull Builder builder, @NotNull Traversable<?> it) {
        this.sizeHint(builder, it, 0);
    }

    default void sizeHint(@NotNull Builder builder, @NotNull Traversable<?> it, int delta) {
        this.sizeHint(builder, (AnyTraversable<?>) it, delta);
    }

    default R empty() {
        return build(newBuilder());
    }

    default R from(E @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, values.length);
        addAllToBuilder(builder, values);
        return build(builder);
    }

    default R from(@NotNull Iterable<? extends E> values) {
        Iterator<? extends E> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return empty();
        }
        Builder builder = newBuilder();
        sizeHint(builder, values);
        addAllToBuilder(builder, iterator);
        return build(builder);
    }

    default R from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }
        Builder builder = newBuilder();
        addAllToBuilder(builder, it);
        return build(builder);
    }

    default R fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, n);
        for (int i = 0; i < n; i++) {
            addToBuilder(builder, value);
        }
        return build(builder);
    }

    default R fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, n);
        for (int i = 0; i < n; i++) {
            addToBuilder(builder, supplier.get());
        }
        return build(builder);
    }

    default R fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, n);
        for (int i = 0; i < n; i++) {
            addToBuilder(builder, init.apply(i));
        }
        return build(builder);
    }

    default <U> @NotNull CollectionFactory<E, Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final CollectionFactory<E, Builder, R> self = this;
        return new CollectionFactory<E, Builder, U>() {
            @Override
            public Builder newBuilder() {
                return self.newBuilder();
            }

            @Override
            public U build(@NotNull Builder builder) {
                return mapper.apply(self.build(builder));
            }

            @Override
            public void addToBuilder(@NotNull Builder builder, E value) {
                self.addToBuilder(builder, value);
            }

            @Override
            public Builder mergeBuilder(@NotNull Builder builder1, @NotNull Builder builder2) {
                return self.mergeBuilder(builder1, builder2);
            }

            @Override
            public void sizeHint(@NotNull Builder builder, int size) {
                self.sizeHint(builder, size);
            }
        };
    }

    @Override
    default Supplier<Builder> supplier() {
        return this::newBuilder;
    }

    @Override
    default BiConsumer<Builder, E> accumulator() {
        return this::addToBuilder;
    }

    @Override
    default Function<Builder, R> finisher() {
        return this::build;
    }

    @Override
    default BinaryOperator<Builder> combiner() {
        return this::mergeBuilder;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
