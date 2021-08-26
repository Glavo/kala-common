package kala.collection.mutable;

public interface DynamicSeqIterator<E> extends MutableSeqIterator<E> {

    @Override
    void add(E e);

    @Override
    void remove();
}
