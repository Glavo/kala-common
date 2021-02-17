package org.glavo.kala.traversable;

import org.glavo.kala.annotations.Covariant;

import java.util.Iterator;

public abstract class AbstractIterator<@Covariant E> implements Iterator<E> {
    @Override
    public String toString() {
        if (hasNext()) {
            return super.toString() + "[]";
        } else {
            return super.toString() + "[<not computed>]";
        }
    }
}
