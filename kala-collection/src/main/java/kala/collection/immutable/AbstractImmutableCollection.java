package kala.collection.immutable;

import kala.collection.base.AnyTraversable;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.collection.factory.CollectionFactory;
import kala.annotations.Covariant;
import kala.collection.AbstractCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractImmutableCollection<@Covariant E>
        extends AbstractCollection<E> implements ImmutableCollection<E> {

    static <E, T, Builder> T filter(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        for (E e : collection) {
            if (predicate.test(e)) {
                factory.addToBuilder(builder, e);
            }
        }

        return factory.build(builder);
    }

    static <E, T, Builder> T filterNot(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        for (E e : collection) {
            if (!predicate.test(e)) {
                factory.addToBuilder(builder, e);
            }
        }

        return factory.build(builder);
    }

    static <E, U, T, Builder> T map(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Function<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, collection);

        for (E e : collection) {
            factory.addToBuilder(builder, mapper.apply(e));
        }
        return factory.build(builder);
    }

    static <E, U, T, Builder> T mapNotNull(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Function<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, collection);

        for (E e : collection) {
            U u = mapper.apply(e);
            if (u != null) {
                factory.addToBuilder(builder, u);
            }
        }
        return factory.build(builder);
    }

    static <E, U, T, Builder> T mapMulti(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();
        Consumer<U> consumer = u -> factory.addToBuilder(builder, u);

        for (E e : collection) {
            mapper.accept(e, consumer);
        }
        return factory.build(builder);
    }


    static <E, U, T, Builder> T flatMap(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        for (E e : collection) {
            Iterable<? extends U> us = mapper.apply(e);
            factory.sizeHint(builder, us);

            for (U u : us) {
                factory.addToBuilder(builder, u);
            }
        }

        return factory.build(builder);
    }

    static <E, U, T, Builder> T zip(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Iterable<? extends U> other,
            @NotNull CollectionFactory<? super Tuple2<E, U>, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);
        if (AnyTraversable.knownSize(collection) == 0 || AnyTraversable.knownSize(other) == 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        Iterator<? extends E> it1 = collection.iterator();
        Iterator<? extends U> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            factory.addToBuilder(builder, Tuple.of(it1.next(), it2.next()));
        }
        return factory.build(builder);
    }

}
