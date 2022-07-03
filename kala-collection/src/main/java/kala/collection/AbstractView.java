package kala.collection;

import kala.annotations.Covariant;

public abstract class AbstractView<@Covariant E> implements CollectionView<E> {
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return className() + "[<not computed>]";
    }
}
