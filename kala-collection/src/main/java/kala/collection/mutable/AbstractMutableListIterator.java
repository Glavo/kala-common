package kala.collection.mutable;

public abstract class AbstractMutableListIterator<E> extends AbstractMutableSeqIterator<E> implements MutableListIterator<E> {
    protected AbstractMutableListIterator(int index) {
        super(index);
    }
}
