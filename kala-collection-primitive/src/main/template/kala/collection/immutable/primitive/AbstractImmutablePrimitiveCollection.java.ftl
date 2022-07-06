package kala.collection.immutable.primitive;

import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.Abstract${Type}Collection;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public abstract class AbstractImmutable${Type}Collection extends Abstract${Type}Collection implements Immutable${Type}Collection {
    static <T, Builder> T filter(
            @NotNull Immutable${Type}Collection collection,
            @NotNull ${Type}Predicate predicate,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        collection.forEach(e -> {
            if (predicate.test(e)) {
                factory.addToBuilder(builder, e);
            }
        });

        return factory.build(builder);
    }

    static <T, Builder> T filterNot(
            @NotNull Immutable${Type}Collection collection,
            @NotNull ${Type}Predicate predicate,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        collection.forEach(e -> {
            if (!predicate.test(e)) {
                factory.addToBuilder(builder, e);
            }
        });

        return factory.build(builder);
    }

    static <U, T, Builder> T map(
            @NotNull Immutable${Type}Collection collection,
            @NotNull ${Type}UnaryOperator mapper,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, collection);
        collection.forEach(e -> factory.addToBuilder(builder, mapper.applyAs${Type}(e)));
        return factory.build(builder);
    }

    static <T, Builder> T flatMap(
            @NotNull Immutable${Type}Collection collection,
            @NotNull ${Type}Function<? extends ${Type}Traversable> mapper,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        collection.forEach(e -> {
            ${Type}Traversable us = mapper.apply(e);
            factory.sizeHint(builder, us);

            us.forEach(u -> factory.addToBuilder(builder, u));
        });

        return factory.build(builder);
    }
}
