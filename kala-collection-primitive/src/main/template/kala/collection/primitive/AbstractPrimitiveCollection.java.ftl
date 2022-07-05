package kala.collection.primitive;

import kala.collection.AnyCollection;
import kala.collection.Collection;

public abstract class Abstract${Type}Collection implements ${Type}Collection {
    @Override
    public int hashCode() {
        return iterator().hash();
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
