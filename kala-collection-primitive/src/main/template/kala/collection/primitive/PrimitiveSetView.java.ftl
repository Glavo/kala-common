package kala.collection.primitive;

import kala.annotations.Covariant;
import kala.collection.primitive.internal.view.SetViews;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface ${Type}SetView extends ${Type}SetLike, ${Type}CollectionView, PrimitiveSetView<${WrapperType}> {

    @Override
    default @NotNull String className() {
        return "${Type}SetView";
    }

    @Override
    default @NotNull ${Type}SetView view() {
        return this;
    }

    @Override
    default @NotNull ${Type}SetView filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SetViews.Filter(this, predicate);
    }

    @Override
    default @NotNull ${Type}SetView filterNot(@NotNull ${Type}Predicate predicate) {
        return filter(predicate.negate());
    }
}
