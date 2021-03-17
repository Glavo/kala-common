package org.glavo.kala.collection.mutable;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.ArraySliceView;
import org.jetbrains.annotations.NotNull;

public final class MutableArraySliceView<E> extends ArraySliceView<E> {
    MutableArraySliceView(Object[] array, int beginIndex, int endIndex) {
        super(array, beginIndex, endIndex);
    }

    @Override
    public final @NotNull String className() {
        return "MutableArraySliceView";
    }

    public final void set(int index, E value) {
        Conditions.checkElementIndex(index, endIndex - beginIndex);
        array[beginIndex + index] = value;
    }

    @Override
    public @NotNull MutableArraySliceView<E> slice(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        return new MutableArraySliceView<>(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
    }

    @Override
    public @NotNull MutableArraySliceView<E> sliceView(int beginIndex, int endIndex) {
        return slice(beginIndex, endIndex);
    }
}
