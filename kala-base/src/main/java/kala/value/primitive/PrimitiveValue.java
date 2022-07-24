package kala.value.primitive;

import kala.collection.base.primitive.PrimitiveTraversable;
import kala.value.AnyValue;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveValue<T> extends AnyValue<@NotNull T>, PrimitiveTraversable<T> {
    @Override
    @NotNull T getValue();

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default int size() {
        return 1;
    }

    @Override
    default int knownSize() {
        return 1;
    }
}
