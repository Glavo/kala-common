package kala.collection.mutable;

import kala.Conditions;
import kala.collection.internal.view.SeqViews;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MutableArraySliceView<E> extends SeqViews.OfArraySlice<E> {
    MutableArraySliceView(Object[] array, int beginIndex, int endIndex) {
        super(array, beginIndex, endIndex);
    }

    @Override
    public @NotNull String className() {
        return "MutableArraySliceView";
    }

    public void set(int index, E value) {
        Objects.checkIndex(index, endIndex - beginIndex);
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
