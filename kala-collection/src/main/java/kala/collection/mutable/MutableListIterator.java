package kala.collection.mutable;

public interface MutableListIterator<E> extends MutableSeqIterator<E> {
    @Override
    void add(E e);


    @Override
    void remove();
}
