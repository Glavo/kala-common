package org.glavo.kala.collection;

import org.glavo.kala.collection.internal.AsJavaConvert;
import org.glavo.kala.collection.immutable.ImmutableSet;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.traversable.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;

public interface Set<E> extends Collection<E> {

    static <E> CollectionFactory<E, ?, ? extends Set<E>> factory() {
        return ImmutableSet.factory();
    }

    @Override
    default boolean contains(Object value) {
        return Iterators.contains(iterator(), value);
    }

    default Predicate<E> asPredicate() {
        return this::contains;
    }

    //region Collection Operations

    @Override
    default String className() {
        return "Set";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Set<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull Spliterator<E> spliterator() {
        int knownSize = this.knownSize();
        return knownSize >= 0
                ? Spliterators.spliterator(iterator(), knownSize, Spliterator.DISTINCT)
                : Spliterators.spliteratorUnknownSize(iterator(), Spliterator.DISTINCT);
    }

    @Override
    default @NotNull SetView<E> view() {
        return new SetViews.Of<>(this);
    }

    @Override
    default @NotNull java.util.Set<E> asJava() {
        return new AsJavaConvert.SetAsJava<>(this);
    }

    //endregion

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Set<?>;
    }
}
