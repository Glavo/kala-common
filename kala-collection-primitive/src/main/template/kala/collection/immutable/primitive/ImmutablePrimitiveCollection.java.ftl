package kala.collection.immutable.primitive;

import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.primitive.${Type}Collection;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface Immutable${Type}Collection extends ImmutablePrimitiveCollection<${WrapperType}>, ${Type}Collection {

    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Immutable${Type}Collection> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Seq.factory());
    }

    static @NotNull Immutable${Type}Collection empty() {
        return Immutable${Type}Seq.empty();
    }

    static @NotNull Immutable${Type}Collection of() {
        return Immutable${Type}Seq.of();
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1) {
        return Immutable${Type}Seq.of(value1);
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Seq.of(value1, value2);
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Seq.of(value1, value2, value3);
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Seq.of(value1, value2, value3, value4);
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Seq.of(value1, value2, value3, value4, value5);
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType}... values) {
        return Immutable${Type}Seq.of(values);
    }

    static @NotNull Immutable${Type}Collection from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Seq.from(values);
    }

    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Seq.from(values);
    }

    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Seq.from(it);
    }
<#if IsSpecialized>

    /*
    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Stream stream) {
        return Immutable${Type}Seq.from(stream);
    }
     */
</#if>

    static @NotNull Immutable${Type}Collection fill(int n, ${PrimitiveType} value) {
        return Immutable${Type}Seq.fill(n, value);
    }

    static @NotNull Immutable${Type}Collection fill(int n, @NotNull ${Type}Supplier supplier) {
        return Immutable${Type}Seq.fill(n, supplier);
    }

    /*
    static @NotNull Immutable${Type}Collection fill(int n, @NotNull IntTo${Type}Function init) {
        return Immutable${Type}Seq.fill(n, init);
    }
    */

    //endregion
    
    @Override
    default @NotNull String className() {
        return "Immutable${Type}Collection";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Immutable${Type}Collection> iterableFactory() {
        return Immutable${Type}Collection.factory();
    }

    @Override
    default @NotNull Immutable${Type}Collection filter(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filter(this, predicate, iterableFactory());
    }

    @Override
    default @NotNull Immutable${Type}Collection filterNot(@NotNull ${Type}Predicate predicate) {
        return AbstractImmutable${Type}Collection.filterNot(this, predicate, iterableFactory());
    }

    @Override
    default @NotNull Immutable${Type}Collection map(@NotNull ${Type}UnaryOperator mapper) {
        return AbstractImmutable${Type}Collection.map(this, mapper, iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableCollection<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        return ${Type}Collection.super.mapToObj(mapper);
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Collection flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        return AbstractImmutable${Type}Collection.flatMap(this, mapper, iterableFactory());
    }
}
