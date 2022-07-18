package kala.collection.immutable.primitive;

import kala.collection.base.primitive.*;
import kala.collection.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public interface Immutable${Type}Set extends ImmutablePrimitiveSet<${WrapperType}>, ${Type}Set, Immutable${Type}Collection {

    //region Static Factories

    static ${Type}CollectionFactory<?, ? extends Immutable${Type}Set> factory() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set empty() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Set from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Immutable${Type}Set";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Immutable${Type}Set> iterableFactory() {
        return factory();
    }

    default @NotNull Immutable${Type}Set added(${PrimitiveType} value) {
        if (contains(value)) {
            return this;
        }
        /*
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends Immutable${Type}Set> factory =
                    (CollectionFactory<E, ?, ? extends Immutable${Type}Set>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutable${Type}Set.added(this, value, factory);
        }
         */
        return AbstractImmutable${Type}Set.added(this, value, iterableFactory());
    }

    default @NotNull Immutable${Type}Set addedAll(@NotNull ${Type}Traversable values) {
        /*
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends Immutable${Type}Set> factory =
                    (CollectionFactory<E, ?, ? extends Immutable${Type}Set>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutable${Type}Set.addedAll(this, values, factory);
        }
        */
        return AbstractImmutable${Type}Set.addedAll(this, values, iterableFactory());
    }

    default @NotNull Immutable${Type}Set addedAll(${PrimitiveType} @NotNull [] values) {
        return addedAll(${Type}ArraySeq.wrap(values));
    }

    @Override
    default @NotNull Immutable${Type}Set filter(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filter(this, predicate, factory());
    }

    @Override
    default @NotNull Immutable${Type}Set filterNot(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filterNot(this, predicate, factory());
    }
}
