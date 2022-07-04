package kala.collection.primitive;

import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.primitive.Immutable${Type}Collection;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}Collection extends PrimitiveCollection<${WrapperType}>, ${Type}CollectionLike {

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "${Type}CollectionLike";
    }

    //endregion

    default @NotNull Immutable${Type}Collection filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    default @NotNull Immutable${Type}Collection filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    default @NotNull Immutable${Type}Collection map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    default <U> @NotNull ImmutableCollection<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull Immutable${Type}Collection flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }
}