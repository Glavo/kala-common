package kala.collection.immutable;

import kala.collection.base.AnyTraversable;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.collection.factory.CollectionFactory;
import kala.annotations.Covariant;
import kala.collection.AbstractCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutableCollection<@Covariant E>
        extends AbstractCollection<E> implements ImmutableCollection<E> {

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

    protected final <U, To extends ImmutableCollection<U>> @NotNull To mapImpl(@NotNull Function<? super E, ? extends U> mapper) {
        return (To) AbstractImmutableCollection.map(this, mapper, iterableFactory());
    }

    protected final <U, To extends ImmutableCollection<@NotNull U>> @NotNull To mapNotNullImpl(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return (To) AbstractImmutableCollection.mapNotNull(this, mapper, iterableFactory());
    }

    protected final <To extends ImmutableCollection<E>> @NotNull To filterImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    protected final <To extends ImmutableCollection<E>> @NotNull To filterNotImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    protected final <To extends ImmutableCollection<E>> @NotNull To filterNotNullImpl() {
        return (To) this.filter(Objects::nonNull);
    }

    protected final <U, To extends ImmutableCollection<U>> @NotNull To flatMapImpl(
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return (To) AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    protected final <U, To extends ImmutableCollection<Tuple2<E, U>>> @NotNull To zipImpl(
            @NotNull Iterable<? extends U> other) {
        return (To) AbstractImmutableCollection.zip(this, other, this.<Tuple2<E, U>>iterableFactory());
    }

}
