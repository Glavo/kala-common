package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

public abstract class AbstractCollection<@Covariant E> implements Collection<E> {
    static <E, R, Builder> R collectTo(
            @NotNull Collection<? extends E> collection,
            @NotNull Collector<? super E, Builder, ? extends R> collector
    ) {
        Objects.requireNonNull(collector);

        final BiConsumer<Builder, ? super E> accumulator = collector.accumulator();

        Builder builder = collector.supplier().get();

        for (E e : collection) {
            accumulator.accept(builder, e);
        }

        return collector.finisher().apply(builder);
    }

    static <E, R, Builder> R collectTo(
            @NotNull Collection<? extends E> collection,
            @NotNull CollectionFactory<? super E, Builder, ? extends R> factory
    ) {
        Objects.requireNonNull(factory);

        if (collection.isEmpty()) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();

        for (E e : collection) {
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    @Override
    public int hashCode() {
        return Iterators.hash(iterator());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Collection<?>)
                || !(canEqual(obj))
                || !(((Collection<?>) obj).canEqual(this))) {
            return false;
        }
        return sameElements(((Collection<?>) obj));
    }

    @Override
    public String toString() {
        return joinToString(", ", className() + "[", "]");
    }
}
