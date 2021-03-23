package org.glavo.kala.collection;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.base.GenericArrays;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ArraySliceView<E> extends AbstractIndexedSeqView<E> {
    protected final Object[] array;
    protected final int beginIndex;
    protected final int endIndex;

    protected ArraySliceView(Object[] array, int beginIndex, int endIndex) {
        this.array = array;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    @Override
    public @NotNull String className() {
        return "ArraySliceView";
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(array, beginIndex, endIndex);
    }

    @Override
    public final int size() {
        return endIndex - beginIndex;
    }

    public final int beginIndex() {
        return beginIndex;
    }

    public final int endIndex() {
        return endIndex;
    }

    @Override
    public final E get(int index) {
        Conditions.checkElementIndex(index, size());
        return (E) array[index + beginIndex];
    }

    @Override
    public final @Nullable E getOrNull(int index) {
        return index < 0 || index >= size()
                ? null
                : (E) array[index + beginIndex];
    }

    @Override
    public final @NotNull Option<E> getOption(int index) {
        return index < 0 || index >= size()
                ? Option.none()
                : Option.some((E) array[index + beginIndex]);
    }

    @Override
    public @NotNull ArraySliceView<E> slice(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        return new ArraySliceView<>(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
    }

    @Override
    public @NotNull ArraySliceView<E> sliceView(int beginIndex, int endIndex) {
        return slice(beginIndex, endIndex);
    }
}
