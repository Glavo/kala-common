package kala.collection.internal.vector;

import kala.Conditions;
import kala.collection.IndexedSeq;
import kala.collection.base.ObjectArrays;
import kala.collection.immutable.AbstractImmutableSeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@SuppressWarnings("unchecked")
public final class Vector<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {

    private static final int LEAF_LENGTH = 32;

    private static final Vector<?> EMPTY = new Vector<>(ObjectArrays.EMPTY, 0);

    private final Object[] array;
    private final int offset;
    private final int size;

    private final int depth;

    private Vector(Object[] array, @Range(from = 0, to = 4) int size) {
        this(array, 0, size, 0);
    }

    private Vector(Object[] array, int offset, int size, int depth) {
        this.offset = offset;
        this.size = size;
        this.array = array;
        this.depth = depth;
    }

    @Override
    public @NotNull String className() {
        return "Vector";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        Conditions.checkElementIndex(index, size);

        // fastpath
        if (depth == 0) {
            return (E) array[index];
        }

        return get0(index);
    }

    private E get0(int index) {
//        index += offset;
//        switch (depth) {
//            case 1:
//                return ((Object[][]) array)[index % LEAF_LENGTH];
//        }

        throw new UnsupportedOperationException(); // TODO
    }
}
