package kala.collection.immutable.primitive;

import kala.collection.base.primitive.*;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface Immutable${Type}Seq extends ImmutablePrimitiveSeq<${WrapperType}>, ${Type}Seq, Immutable${Type}Collection {
    
    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq empty() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }

<#if IsSpecialized>
    /*
    static @NotNull Immutable${Type}Seq from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
    }
     */

</#if>
    static @NotNull Immutable${Type}Seq fill(int n, ${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Immutable${Type}Seq fill(int n, @NotNull ${Type}Supplier supplier) {
        throw new UnsupportedOperationException(); // TODO
    }

    /*
    static @NotNull Immutable${Type}Seq fill(int n, @NotNull IntTo${Type}Function init) {
        throw new UnsupportedOperationException(); // TODO
    }
     */

    //endregion

    @Override
    @NotNull
    default String className() {
        return "Immutable${Type}Seq";
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
}
