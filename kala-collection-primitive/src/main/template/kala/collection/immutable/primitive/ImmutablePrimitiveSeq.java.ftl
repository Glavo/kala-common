package kala.collection.immutable.primitive;

import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface Immutable${Type}Seq extends ImmutablePrimitiveSeq<${WrapperType}>, ${Type}Seq, Immutable${Type}Collection {
    
    //region Static Factories

    static <E> @NotNull ${Type}CollectionFactory<?, Immutable${Type}Seq> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Array.factory());
    }

    static @NotNull Immutable${Type}Seq empty() {
        return Immutable${Type}Array.empty();
    }

    static @NotNull Immutable${Type}Seq of() {
        return Immutable${Type}Array.of();
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1) {
        return Immutable${Type}Array.of(value1);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Array.of(value1, value2);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Array.of(value1, value2, value3);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Array.of(value1, value2, value3, value4);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Array.of(value1, value2, value3, value4, value5);
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType}... values) {
        return Immutable${Type}Array.of(values);
    }

    static @NotNull Immutable${Type}Seq from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Array.from(values);
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Array.from(values);
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Array.from(it);
    }

<#if IsSpecialized>
    /*
    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
    }
     */

</#if>
    static @NotNull Immutable${Type}Seq fill(int n, ${PrimitiveType} value) {
        return Immutable${Type}Array.fill(n, value);
    }

    static @NotNull Immutable${Type}Seq fill(int n, @NotNull ${Type}Supplier supplier) {
        return Immutable${Type}Array.fill(n, supplier);
    }

    /*
    static @NotNull Immutable${Type}Seq fill(int n, @NotNull IntTo${Type}Function init) {
        return Immutable${Type}Array.fill(n, init);
    }
     */

    //endregion

    @Override
    @NotNull
    default String className() {
        return "Immutable${Type}Seq";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Immutable${Type}Seq> iterableFactory() {
        return Immutable${Type}Seq.factory();
    }

    @Override
    default @NotNull ${Type}SeqView view() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq slice(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq drop(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq dropWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq take(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq takeWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq updated(int index, ${PrimitiveType} newValue) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq concat(@NotNull ${Type}SeqLike other) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prepended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq prependedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq appendedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq sorted() {
        throw new UnsupportedOperationException(); // TODO
    }


    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq reversed() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull Immutable${Type}Seq flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default @NotNull Immutable${Type}Seq toImmutableSeq() {
        return this;
    }
}
