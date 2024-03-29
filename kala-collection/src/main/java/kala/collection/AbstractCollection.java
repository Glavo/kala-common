package kala.collection;

import kala.annotations.Covariant;
import kala.collection.base.Iterators;

public abstract class AbstractCollection<@Covariant E> implements Collection<E> {

    @Override
    public boolean isNotEmpty() {
        return Collection.super.isNotEmpty();
    }

    @Override
    public int hashCode() {
        return Iterators.hash(iterator());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnyCollection<?>)
                || !(canEqual(obj))
                || !(((AnyCollection<?>) obj).canEqual(this))) {
            return false;
        }
        return sameElements(((Collection<?>) obj));
    }

    @Override
    public String toString() {
        return joinToString(", ", className() + "[", "]");
    }
}
