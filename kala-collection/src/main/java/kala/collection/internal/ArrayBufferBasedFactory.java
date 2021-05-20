package kala.collection.internal;

import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.ArrayBuffer;
import org.jetbrains.annotations.NotNull;

public abstract class ArrayBufferBasedFactory<E, R> implements CollectionFactory<E, ArrayBuffer<E>, R> {
    @Override
    public ArrayBuffer<E> newBuilder() {
        return new ArrayBuffer<>();
    }

    @Override
    public void addToBuilder(@NotNull ArrayBuffer<E> builder, E value) {
        builder.append(value);
    }

    @Override
    public void sizeHint(@NotNull ArrayBuffer<E> builder, int size) {
        builder.sizeHint(size);
    }

    @Override
    public ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
        if (builder1.isEmpty()) {
            return builder2;
        }
        builder1.appendAll(builder2);
        return builder1;
    }
}
