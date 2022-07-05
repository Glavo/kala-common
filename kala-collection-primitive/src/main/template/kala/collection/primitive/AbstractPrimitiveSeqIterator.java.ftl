package kala.collection.primitive;

import kala.collection.base.primitive.Abstract${Type}Iterator;

public abstract class Abstract${Type}SeqIterator extends Abstract${Type}Iterator implements ${Type}SeqIterator {
    protected int cursor;

    protected Abstract${Type}SeqIterator(int index) {
        this.cursor = index;
    }

    @Override
    public int nextIndex() {
        return cursor;
    }

    @Override
    public int previousIndex() {
        return cursor - 1;
    }

    @Override
    public boolean hasPrevious() {
        return cursor > 0;
    }
}
