package kala.collection;

public interface SeqIterator<E> extends java.util.ListIterator<E> {

    /**
     * {@inheritDoc}
     */
    boolean hasNext();

    /**
     * {@inheritDoc}
     */
    E next();

    /**
     * {@inheritDoc}
     */
    boolean hasPrevious();

    /**
     * {@inheritDoc}
     */
    E previous();

    /**
     * {@inheritDoc}
     */
    int nextIndex();

    /**
     * {@inheritDoc}
     */
    int previousIndex();

    //region Modification Operations

    /**
     * @see kala.collection.mutable.MutableSeqIterator#set(Object)
     */
    @Deprecated
    default void set(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see kala.collection.mutable.DynamicSeqIterator#add(Object)
     */
    @Deprecated
    default void add(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see kala.collection.mutable.DynamicSeqIterator#remove()
     */
    @Deprecated
    default void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

}
