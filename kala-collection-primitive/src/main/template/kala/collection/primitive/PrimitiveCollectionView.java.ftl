package kala.collection.primitive;

import kala.collection.CollectionView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.primitive.internal.view.${Type}CollectionViews;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
        Objects.requireNonNull(predicate);
        return new ${Type}CollectionViews.Filter(this, predicate);
    }

    default @NotNull ${Type}CollectionView filterNot(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}CollectionViews.FilterNot(this, predicate);
    }

    default @NotNull ${Type}CollectionView map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.Mapped(this, mapper);
    }

    default <U> @NotNull CollectionView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.MapToObj<>(this, mapper);
    }

    @Contract(pure = true)
    default @NotNull ${Type}CollectionView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}CollectionViews.FlatMapped(this, mapper);
    }
}
