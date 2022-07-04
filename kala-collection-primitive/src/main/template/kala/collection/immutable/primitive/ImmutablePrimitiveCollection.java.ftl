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
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }
<#if IsSpecialized>

    /*
    static @NotNull Immutable${Type}Collection from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
    }
     */
</#if>

    static @NotNull Immutable${Type}Collection fill(int n, ${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Collection fill(int n, @NotNull ${Type}Supplier supplier) {
        throw new UnsupportedOperationException(); // TODO
    }

    /*
    static @NotNull Immutable${Type}Collection fill(int n, @NotNull IntTo${Type}Function init) {
        throw new UnsupportedOperationException(); // TODO
    }
    */

    //endregion
    
    @Override
    default @NotNull String className() {
        return "Immutable${Type}Collection";
    }

    @Override
    default @NotNull Immutable${Type}Collection filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default @NotNull Immutable${Type}Collection filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default @NotNull Immutable${Type}Collection map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default <U> @NotNull ImmutableCollection<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Collection flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }
}
