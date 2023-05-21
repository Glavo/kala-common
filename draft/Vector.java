package kala.collection.internal.vector;

import kala.Conditions;
import kala.collection.IndexedSeq;
import kala.collection.base.ObjectArrays;
import kala.collection.immutable.AbstractImmutableSeq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * @see <a href="https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/collection/BitMappedTrie.java">BitMappedTrie.java</a>
 */
@SuppressWarnings("unchecked")
public final class Vector<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {

    static final int BRANCHING_BASE = 5;
    static final int BRANCHING_FACTOR = 1 << BRANCHING_BASE;

    @FunctionalInterface
    private interface NodeModifier {
        Object[] apply(Object[] array, int index);

        NodeModifier COPY_NODE = (array, i) -> Arrays.copyOf(array, Math.max(i + 1, array.length), Object[].class);
        NodeModifier IDENTITY = (array, i) -> array;
    }

    static int firstDigit(int num, int depthShift) {
        return num >> depthShift;
    }

    static int digit(int num, int depthShift) {
        return lastDigit(firstDigit(num, depthShift));
    }

    static int lastDigit(int num) {
        return num % BRANCHING_FACTOR;
    }

    private static final Vector<?> EMPTY = new Vector<>(ObjectArrays.EMPTY, 0);

    private final Object[] array;
    private final int offset;
    private final int size;

    final int depthShift;

    private Vector(Object[] array, @Range(from = 0, to = 32) int size) {
        this(array, 0, size, 0);
    }

    private Vector(Object[] array, int offset, int size, int depthShift) {
        this.offset = offset;
        this.size = size;
        this.array = array;
        this.depthShift = depthShift;
    }

    private Object[] getArray(int index) {
        // keep small for inline
        return depthShift == 0 ? array : getLeaf(index);
    }

    private Object[] getLeaf(int index) {
        index += offset;
        Object[] leaf = (Object[]) array[firstDigit(index, depthShift)];
        for (int shift = depthShift - BRANCHING_BASE; shift > 0; shift -= BRANCHING_BASE) {
            leaf = (Object[]) leaf[digit(index, shift)];
        }
        return leaf;
    }

    /* descend the tree from root to leaf, applying the given modifications along the way, returning the new root */
    private static Object[] modify(Object[] root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        return (depthShift == 0)
                ? leaf.apply(root, index)
                : modifyNonLeaf(root, depthShift, index, node, leaf);
    }

    private static Object[] modifyNonLeaf(Object[] root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        int previousIndex = firstDigit(index, depthShift);
        root = node.apply(root, previousIndex);

        Object[] array = root;
        for (int shift = depthShift - BRANCHING_BASE; shift >= BRANCHING_BASE; shift -= BRANCHING_BASE) {
            final int prev = previousIndex;
            previousIndex = digit(index, shift);
            array = setNewNode(node, prev, array, previousIndex);
        }

        final Object newLeaf = leaf.apply((Object[]) array[previousIndex], lastDigit(index));
        array[previousIndex] = newLeaf;
        return root;
    }

    private static Object[] setNewNode(NodeModifier node, int previousIndex, Object[] array, int offset) {
        final Object[] previous = (Object[]) array[previousIndex];
        final Object[] newNode = node.apply(previous, offset);
        array[previousIndex] = newNode;
        return newNode;
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
        if (depthShift == 0) {
            return (E) array[index];
        } else {
            final Object[] leaf = getLeaf(index);
            final int leafIndex = lastDigit(offset + index);
            return (E) leaf[leafIndex];
        }
    }


}
