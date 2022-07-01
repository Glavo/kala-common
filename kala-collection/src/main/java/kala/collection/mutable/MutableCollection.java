package kala.collection.mutable;

import kala.collection.Collection;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collector;

public interface MutableCollection<E> extends Collection<E>, MutableAnyCollection<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableCollection<E>> factory() {
        return CollectionFactory.narrow(MutableSeq.factory());
    }

    static <E> @NotNull MutableCollection<E> of() {
        return MutableSeq.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1) {
        return MutableSeq.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2) {
        return MutableSeq.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3) {
        return MutableSeq.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3, E value4) {
        return MutableSeq.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableSeq.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableCollection<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableCollection<E> from(E @NotNull [] values) {
        return MutableSeq.from(values);
    }

    static <E> @NotNull MutableCollection<E> from(@NotNull Iterable<? extends E> values) {
        return MutableSeq.from(values);
    }

    static <E, C extends MutableCollection<E>> @NotNull MutableCollectionEditor<E, C> edit(@NotNull C collection) {
        return new MutableCollectionEditor<>(collection);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "MutableCollection";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableCollection<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull java.util.Collection<E> asJava() {
        return new AsJavaConvert.MutableCollectionAsJava<>(this);
    }

    //endregion
}
