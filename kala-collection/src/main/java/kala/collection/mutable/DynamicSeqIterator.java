package kala.collection.mutable;

public interface DynamicSeqIterator<E> extends MutableSeqIterator<E> {
    @Override
    @SuppressWarnings("deprecation")
    void add(E e);


    @Override
    @SuppressWarnings("deprecation")
    void remove();
}
