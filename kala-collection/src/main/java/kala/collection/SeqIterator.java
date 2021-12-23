package kala.collection;

import kala.collection.internal.SeqIterators;
import kala.collection.mutable.MutableListIterator;
import org.jetbrains.annotations.NotNull;

public interface SeqIterator<E> extends java.util.ListIterator<E> {

    @SuppressWarnings("unchecked")
    static <E> SeqIterator<E> empty() {
        return (SeqIterator<E>) SeqIterators.EMPTY;
    }

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
    // @Deprecated
    default void set(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see MutableListIterator#add(Object)
     */
    // @Deprecated
    default void add(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see MutableListIterator#remove()
     */
    // @Deprecated
    default void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

    default @NotNull SeqIterator<E> frozen() {
        return this;
    }

}
