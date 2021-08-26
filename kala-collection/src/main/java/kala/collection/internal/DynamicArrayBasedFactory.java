package kala.collection.internal;

import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.DynamicArray;
import org.jetbrains.annotations.NotNull;

public abstract class DynamicArrayBasedFactory<E, R> implements CollectionFactory<E, DynamicArray<E>, R> {
    @Override
    public DynamicArray<E> newBuilder() {
        return new DynamicArray<>();
    }

    @Override
    public void addToBuilder(@NotNull DynamicArray<E> builder, E value) {
        builder.append(value);
    }

    @Override
    public void sizeHint(@NotNull DynamicArray<E> builder, int size) {
        builder.sizeHint(size);
    }

    @Override
    public DynamicArray<E> mergeBuilder(@NotNull DynamicArray<E> builder1, @NotNull DynamicArray<E> builder2) {
        if (builder1.isEmpty()) {
            return builder2;
        }
        builder1.appendAll(builder2);
        return builder1;
    }
}
