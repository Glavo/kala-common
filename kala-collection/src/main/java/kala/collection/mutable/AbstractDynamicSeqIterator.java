package kala.collection.mutable;

public abstract class AbstractDynamicSeqIterator<E> extends AbstractMutableSeqIterator<E> implements DynamicSeqIterator<E> {
    protected AbstractDynamicSeqIterator(int index) {
        super(index);
    }
}
