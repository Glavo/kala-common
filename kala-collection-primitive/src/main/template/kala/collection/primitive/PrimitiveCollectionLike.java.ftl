package kala.collection.primitive;

import kala.collection.CollectionLike;
import kala.function.*;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.immutable.primitive.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}CollectionLike extends PrimitiveCollectionLike<${WrapperType}>, ${Type}Traversable {
    @Override
    default @NotNull String className() {
        return "${Type}CollectionLike";
    }

    @Override
    @NotNull ${Type}CollectionView view();

    @NotNull ${Type}CollectionLike filter(@NotNull ${Type}Predicate predicate);

    @NotNull ${Type}CollectionLike filterNot(@NotNull ${Type}Predicate predicate);

    @NotNull ${Type}CollectionLike map(@NotNull ${Type}UnaryOperator mapper);

    <U> @NotNull CollectionLike<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper);

    @Contract(pure = true)
    @NotNull ${Type}CollectionLike flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper);

    @Contract(pure = true)
    <T> @NotNull CollectionLike<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper);

    default @NotNull ${Type}Seq toSeq() {
        return toImmutableSeq();
    }

    default @NotNull Immutable${Type}Seq toImmutableSeq() {
        return Immutable${Type}Seq.from(this);
    }

    default @NotNull Immutable${Type}Array toImmutableArray() {
        return Immutable${Type}Array.from(this);
    }
}
