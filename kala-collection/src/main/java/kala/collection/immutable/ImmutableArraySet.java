package kala.collection.immutable;

import kala.collection.base.ObjectArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class ImmutableArraySet<E> extends AbstractImmutableSet<E> implements Serializable {
    private final Object[] values;

    private ImmutableArraySet(Object[] values) {
        this.values = values;
    }


    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) ObjectArrays.iterator(values);
    }
}
