/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    default @NotNull SeqIterator<E> freeze() {
        return this;
    }

}
