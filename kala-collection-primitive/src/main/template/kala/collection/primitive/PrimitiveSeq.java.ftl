package kala.collection.primitive;

import kala.collection.Seq;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}Seq extends PrimitiveSeq<${WrapperType}>, ${Type}SeqLike {
    //region Static Factories

    static <E> @NotNull ${Type}CollectionFactory<?, ${Type}Seq> factory() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq empty() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} values2) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} values2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} values2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} values2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq fill(int n, ${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull ${Type}Seq fill(int n, @NotNull ${Type}Supplier supplier) {
        throw new UnsupportedOperationException(); // TODO
    }
    /*
    static @NotNull ${Type}Seq fill(int n, @NotNull IntTo${Type}Function init) {
        throw new UnsupportedOperationException(); // TODO
    }
    */

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "${Type}Seq";
    }

    //endregion

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq slice(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq drop(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq dropLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq dropWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq take(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq takeLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq takeWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq updated(int index, ${PrimitiveType} newValue) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq concat(@NotNull ${Type}SeqLike other) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq prepended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq prependedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq prependedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq appended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq appendedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq appendedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq sorted() {
        throw new UnsupportedOperationException(); // TODO
    }


    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq reversed() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull Seq<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}Seq flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }
}
