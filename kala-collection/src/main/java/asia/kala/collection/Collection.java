package asia.kala.collection;

import asia.kala.Equals;
import asia.kala.annotations.Covariant;
import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.collection.immutable.*;
import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.Traversable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Collection<@Covariant E> extends Traversable<E>, Equals {

    int SEQ_HASH_MAGIC = -1140647423;

    int SET_HASH_MAGIC = 1045751549;

    //region Static Factories

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> Collection<E> narrow(Collection<? extends E> collection) {
        return (Collection<E>) collection;
    }

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, ? extends Collection<E>> factory() {
        return ImmutableCollection.factory();
    }

    //endregion

    //region Collection Operations

    default String className() {
        return "Traversable";
    }

    default <U> @NotNull CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    default @NotNull View<E> view() {
        return new Views.Of<>(this);
    }

    default @NotNull java.util.Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    //endregion

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Collection<?>;
    }

    default @NotNull Seq<E> toSeq() {
        return toImmutableSeq();
    }

    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return toImmutableVector();
    }

    @SuppressWarnings("unchecked")
    default @NotNull ImmutableArray<E> toImmutableArray() {
        return (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(toArray());
    }

    default @NotNull ImmutableList<E> toImmutableList() {
        return ImmutableList.from(this);
    }

    default @NotNull ImmutableVector<E> toImmutableVector() {
        return ImmutableVector.from(this);
    }
}
