package org.glavo.kala.collection;

import org.glavo.kala.Equatable;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.immutable.*;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.collection.internal.view.Views;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.stream.Collector;

public interface Collection<@Covariant E> extends Traversable<E>, CollectionLike<E>, Equatable {

    int SEQ_HASH_MAGIC = -1140647423;

    int SET_HASH_MAGIC = 1045751549;

    //region Static Factories

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> Collection<E> narrow(Collection<? extends E> collection) {
        return (Collection<E>) collection;
    }

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, Collection<E>> factory() {
        return CollectionFactory.narrow(ImmutableCollection.factory());
    }

    static <E> @NotNull Collector<E, ?, Collection<E>> collector() {
        return factory();
    }

    //endregion

    //region Collection Operations

    default @NotNull String className() {
        return "Collection";
    }

    default <U> @NotNull CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    default @NotNull View<E> view() {
        return knownSize() == 0 ? View.empty() : new Views.Of<>(this);
    }

    default java.util.@NotNull @UnmodifiableView Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    //endregion

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Collection<?>;
    }

}
