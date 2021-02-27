package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

public abstract class AbstractCollection<@Covariant E> implements Collection<E> {

    @Override
    public int hashCode() {
        return Iterators.hash(iterator());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Collection<?>)
                || !(canEqual(obj))
                || !(((Collection<?>) obj).canEqual(this))) {
            return false;
        }
        return sameElements(((Collection<?>) obj));
    }

    @Override
    public String toString() {
        return joinToString(", ", className() + "[", "]");
    }
}
