package asia.kala.collection.immutable;

import asia.kala.traversable.Transformable;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ImmutableCollection<@Covariant E> extends Collection<E>, Transformable<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> ImmutableCollection<E> narrow(ImmutableCollection<? extends E> collection) {
        return (ImmutableCollection<E>) collection;
    }

    //endregion

    //region Factory methods

    @NotNull
    static <E> CollectionFactory<E, ?, ? extends ImmutableCollection<E>> factory() {
        return ImmutableSeq.factory();
    }

    @NotNull
    @SafeVarargs
    static <E> ImmutableCollection<E> of(E... values) {
        return ImmutableCollection.<E>factory().from(values);
    }

    @NotNull
    static <E> ImmutableCollection<E> from(E @NotNull [] values) {
        return ImmutableCollection.<E>factory().from(values);
    }

    @NotNull
    static <E> ImmutableCollection<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableCollection.<E>factory().from(values);
    }

    //endregion

    @Override
    default String className() {
        return "ImmutableCollection";
    }

    @NotNull
    @Override
    default Spliterator<E> spliterator() {
        final int knownSize = knownSize();
        if (knownSize != 0) {
            return Spliterators.spliterator(iterator(), knownSize, Spliterator.IMMUTABLE);
        }
        return Spliterators.spliterator(iterator(), size(), Spliterator.IMMUTABLE);
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default <U> CollectionFactory<U, ?, ? extends ImmutableCollection<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default <U> ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return AbstractImmutableCollection.map(this, mapper, this.<U>iterableFactory());
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default ImmutableCollection<@NotNull E> filterNotNull() {
        return this.filter(Objects::nonNull);
    }

    @NotNull
    @Contract(pure = true)
    default <U> ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @NotNull
    @Override
    @Contract(pure = true)
    default Tuple2<? extends ImmutableCollection<E>, ? extends ImmutableCollection<E>> span(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.span(this, predicate, iterableFactory());
    }
}
