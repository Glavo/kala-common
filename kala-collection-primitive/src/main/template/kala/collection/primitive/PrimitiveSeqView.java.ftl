package kala.collection.primitive;

import kala.collection.SeqView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}SeqView extends ${Type}SeqLike, ${Type}CollectionView, PrimitiveSeqView<${WrapperType}> {

    @Override
    default @NotNull String className() {
        return "${Type}SeqView";
    }

    @Override
    default @NotNull ${Type}SeqView view() {
        return this;
    }

    @Contract(pure = true)
    @NotNull ${Type}SeqView slice(int beginIndex, int endIndex);

    @Contract(pure = true)
    @NotNull ${Type}SeqView drop(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqView dropLast(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqView dropWhile(@NotNull ${Type}Predicate predicate);

    @Contract(pure = true)
    @NotNull ${Type}SeqView take(int n);

    @NotNull ${Type}SeqView takeLast(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqView takeWhile(@NotNull ${Type}Predicate predicate);

    @Contract(pure = true)
    @NotNull ${Type}SeqView updated(int index, ${PrimitiveType} newValue);

    @Contract(pure = true)
    @NotNull ${Type}SeqView concat(@NotNull ${Type}SeqLike other);

    @Contract(pure = true)
    @NotNull ${Type}SeqView prepended(${PrimitiveType} value);

    @Contract(pure = true)
    @NotNull ${Type}SeqView prependedAll(${PrimitiveType} @NotNull [] values);

    @Contract(pure = true)
    @NotNull ${Type}SeqView prependedAll(@NotNull ${Type}Traversable values);

    @Contract(pure = true)
    @NotNull ${Type}SeqView appended(${PrimitiveType} value);

    @Contract(pure = true)
    @NotNull ${Type}SeqView appendedAll(@NotNull ${Type}Traversable values);

    @Contract(pure = true)
    @NotNull ${Type}SeqView appendedAll(${PrimitiveType} @NotNull [] values);

    @Contract(pure = true)
    @NotNull ${Type}SeqView sorted();

    @Contract(pure = true)
    @NotNull ${Type}SeqView reversed();

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqView filter(@NotNull ${Type}Predicate predicate);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqView filterNot(@NotNull ${Type}Predicate predicate);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqView map(@NotNull ${Type}UnaryOperator mapper);

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper);
}
