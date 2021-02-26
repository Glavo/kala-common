package org.glavo.kala.collection;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.base.GenericArrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ArraySliceView<E> extends AbstractIndexedSeqView<E> {
    protected final Object[] array;
    protected final int beginIndex;
    protected final int size;

    protected ArraySliceView(Object[] array, int beginIndex, int size) {
        this.array = array;
        this.beginIndex = beginIndex;
        this.size = size;
    }

    @Override
    public String className() {
        return "ArraySliceView";
    }

    @Override
    public final int size() {
        return this.size;
    }

    @Override
    public final E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        Conditions.checkElementIndex(index, size);
        return (E) array[beginIndex + index];
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(array, beginIndex, beginIndex + size);
    }
}
