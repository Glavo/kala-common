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

    static ${Type}CollectionFactory<?, Immutable${Type}Set> factory() {
<#if Type == "Boolean" || Type == "Byte">
        return ${Type}CollectionFactory.narrow(DefaultImmutable${Type}Set.factory());
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set empty() {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.empty();
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of() {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of();
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(value1);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(value1, value2);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(value1, value2, value3);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(value1, value2, value3, value4);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(value1, value2, value3, value4, value5);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set of(${PrimitiveType}... values) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.of(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set from(${PrimitiveType} @NotNull [] values) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.from(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set from(@NotNull ${Type}Traversable values) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.from(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    static @NotNull Immutable${Type}Set from(@NotNull ${Type}Iterator it) {
<#if Type == "Boolean" || Type == "Byte">
        return DefaultImmutable${Type}Set.from(it);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
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
