package kala.collection.mutable.primitive;

import kala.collection.base.primitive.CharIterator;
import org.jetbrains.annotations.NotNull;

final class DefaultMutableCharSet extends AbstractMutableCharSet {

    private int size;
    private Node latin1;
    private Node root;

    @Override
    public @NotNull CharIterator iterator() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public boolean add(char value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public boolean remove(char value) {
        throw new UnsupportedOperationException(); // TODO
    }

    private static final class Node {
        final short number;

        long bits0, bits1, bits2, bits3;

        Node parent, left, right;

        Node(short number) {
            this.number = number;
        }
    }
}
