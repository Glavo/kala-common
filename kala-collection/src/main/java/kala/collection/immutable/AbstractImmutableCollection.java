package kala.collection.immutable;

import kala.annotations.Covariant;
import kala.collection.AbstractCollection;
import kala.collection.base.AnyTraversable;
import kala.collection.factory.CollectionFactory;
import kala.tuple.Tuple;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class AbstractImmutableCollection<@Covariant E>
        extends AbstractCollection<E> implements ImmutableCollection<E> {

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

    static <E, U, R, T, Builder> T zip(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Iterable<? extends U> other,
            @NotNull BiFunction<? super E, ? super U, ? extends R> mapper,
            @NotNull CollectionFactory<? super R, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(mapper);
        if (AnyTraversable.knownSize(collection) == 0 || AnyTraversable.knownSize(other) == 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        Iterator<? extends E> it1 = collection.iterator();
        Iterator<? extends U> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            factory.addToBuilder(builder, mapper.apply(it1.next(), it2.next()));
        }
        return factory.build(builder);
    }

    static <E, U, V, T, Builder> T zip3(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Iterable<? extends U> other1,
            @NotNull Iterable<? extends V> other2,
            @NotNull CollectionFactory<? super Tuple3<E, U, V>, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other1);
        Objects.requireNonNull(other2);
        if (AnyTraversable.knownSize(collection) == 0 || AnyTraversable.knownSize(other1) == 0 || AnyTraversable.knownSize(other2) == 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        Iterator<? extends E> it1 = collection.iterator();
        Iterator<? extends U> it2 = other1.iterator();
        Iterator<? extends V> it3 = other2.iterator();

        while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
            factory.addToBuilder(builder, Tuple.of(it1.next(), it2.next(), it3.next()));
        }
        return factory.build(builder);
    }

    static <E, T, Builder> T distinct(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (AnyTraversable.knownSize(collection) == 0) {
            return factory.empty();
        }
        Builder builder = factory.newBuilder();
        HashSet<E> set = new HashSet<>();
        for (E e : collection) {
            if (set.add(e))
                factory.addToBuilder(builder, e);
        }
        return factory.build(builder);
    }
}
