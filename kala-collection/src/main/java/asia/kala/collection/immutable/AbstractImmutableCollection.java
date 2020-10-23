package asia.kala.collection.immutable;

import asia.kala.Tuple2;
import asia.kala.factory.CollectionFactory;
import asia.kala.annotations.Covariant;
import asia.kala.collection.AbstractCollection;
import org.jetbrains.annotations.NotNull;

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
        assert collection != null;
        assert factory != null;

        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, collection);

        for (E e : collection) {
            factory.addToBuilder(builder, mapper.apply(e));
        }
        return factory.build(builder);
    }

    static <E, T, Builder> T filter(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory) {
        assert collection != null;
        assert factory != null;

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
        assert collection != null;
        assert factory != null;

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
        assert collection != null;
        assert factory != null;

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

    static <E, T, Builder> Tuple2<T, T> span(
            @NotNull ImmutableCollection<? extends E> collection,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory) {
        assert collection != null;
        assert factory != null;

        Objects.requireNonNull(predicate);

        Builder builder1 = factory.newBuilder();
        Builder builder2 = factory.newBuilder();

        boolean flag = true;

        for (E e : collection) {
            if (flag) {
                if (predicate.test(e)) {
                    factory.addToBuilder(builder1, e);
                } else {
                    factory.addToBuilder(builder2, e);
                    flag = false;
                }
            } else {
                factory.addToBuilder(builder2, e);
            }
        }
        return new Tuple2<>(factory.build(builder1), factory.build(builder2));
    }


    @NotNull
    protected final <U, To extends ImmutableCollection<U>> To mapImpl(@NotNull Function<? super E, ? extends U> mapper) {
        return (To) AbstractImmutableCollection.map(this, mapper, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableCollection<E>> To filterImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableCollection<E>> To filterNotImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableCollection<E>> To filterNotNullImpl() {
        return (To) this.filter(Objects::nonNull);
    }

    @NotNull
    protected final <U, To extends ImmutableCollection<U>> To flatMapImpl(
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return (To) AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @SuppressWarnings("rawtypes")
    protected final <To extends ImmutableCollection<E>> Tuple2<To, To> spanImpl(@NotNull Predicate<? super E> predicate) {
        return (Tuple2) AbstractImmutableCollection.span(this, predicate, iterableFactory());
    }
}
