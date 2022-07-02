package kala.collection.primitive;

import kala.collection.Collection;
import kala.collection.base.primitive.${Type}Traversable;
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

    default @NotNull ${Type}Collection filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    default @NotNull ${Type}Collection filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    default @NotNull ${Type}Collection map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    default <U> @NotNull Collection<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    default @NotNull ${Type}Collection flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }
}