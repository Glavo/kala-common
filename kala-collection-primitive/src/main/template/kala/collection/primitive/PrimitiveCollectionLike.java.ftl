package kala.collection.primitive;

import kala.collection.CollectionLike;
import kala.function.*;
import kala.collection.base.primitive.${Type}Traversable;
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
}
