package kala.collection.base;

import kala.annotations.Covariant;
import kala.annotations.UnstableName;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<@Covariant E> implements Iterator<E> {

    @UnstableName
    protected void checkStatus() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String toString() {
        if (!hasNext()) {
            return super.toString() + "[]";
        } else {
            return super.toString() + "[<not computed>]";
        }
    }
}
