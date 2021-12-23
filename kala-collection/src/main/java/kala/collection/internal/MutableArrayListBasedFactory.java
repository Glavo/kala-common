package kala.collection.internal;

import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.MutableArrayList;
import org.jetbrains.annotations.NotNull;

public abstract class MutableArrayListBasedFactory<E, R> implements CollectionFactory<E, MutableArrayList<E>, R> {
    @Override
    public MutableArrayList<E> newBuilder() {
        return new MutableArrayList<>();
    }

    @Override
    public void addToBuilder(@NotNull MutableArrayList<E> builder, E value) {
        builder.append(value);
    }

    @Override
    public void sizeHint(@NotNull MutableArrayList<E> builder, int size) {
        builder.sizeHint(size);
    }

    @Override
    public MutableArrayList<E> mergeBuilder(@NotNull MutableArrayList<E> builder1, @NotNull MutableArrayList<E> builder2) {
        if (builder1.isEmpty()) {
            return builder2;
        }
        builder1.appendAll(builder2);
        return builder1;
    }
}
