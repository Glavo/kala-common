package kala.collection.primitive;

import kala.collection.SeqView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.primitive.internal.view.${Type}SeqViews;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}SeqView extends ${Type}SeqLike, ${Type}CollectionView, PrimitiveSeqView<${WrapperType}> {

    static @NotNull ${Type}SeqView empty() {
        return ${Type}SeqViews.EMPTY;
    }

    @Override
    default @NotNull String className() {
        return "${Type}SeqView";
    }

    @Override
    default @NotNull ${Type}SeqView view() {
        return this;
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView slice(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView drop(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView dropLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView dropWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView take(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    default @NotNull ${Type}SeqView takeLast(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView takeWhile(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView updated(int index, ${PrimitiveType} newValue) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView concat(@NotNull ${Type}SeqLike other) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prepended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prependedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prependedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appended(${PrimitiveType} value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appendedAll(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appendedAll(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView sorted() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView reversed() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default  @NotNull ${Type}SeqView filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    default @Contract(pure = true)
    <U> @NotNull SeqView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }
}
