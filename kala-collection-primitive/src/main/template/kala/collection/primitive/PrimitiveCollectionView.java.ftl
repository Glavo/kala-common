package kala.collection.primitive;

import kala.collection.CollectionView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.primitive.internal.view.${Type}CollectionViews;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}CollectionView extends ${Type}CollectionLike, PrimitiveCollectionView<${WrapperType}> {

    static @NotNull ${Type}CollectionView empty() {
        return ${Type}CollectionViews.EMPTY;
    }

    @Override
    default @NotNull String className() {
        return "${Type}CollectionView";
    }

    @Override
    default @NotNull ${Type}CollectionView view() {
        return this;
    }

    default @NotNull ${Type}CollectionView filter(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @NotNull
    default ${Type}CollectionView filterNot(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @NotNull
    default ${Type}CollectionView map(@NotNull ${Type}UnaryOperator mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    default <U> @NotNull CollectionView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(pure = true)
    @NotNull
    default ${Type}CollectionView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        throw new UnsupportedOperationException(); // TODO
    }

}
