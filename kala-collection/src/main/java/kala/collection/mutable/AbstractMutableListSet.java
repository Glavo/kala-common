package kala.collection.mutable;

import kala.collection.base.OrderedTraversable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.IntFunction;

abstract class AbstractMutableListSet<E, L extends MutableList<E>> extends AbstractMutableSet<E>
        implements OrderedTraversable<E>, Serializable {
    private static final long serialVersionUID = 0L;

    protected final L values;

    AbstractMutableListSet(L values) {
        this.values = values;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public int knownSize() {
        return values.size();
    }

    //region Modification Operations

    @Override
    public boolean add(E value) {
        if (values.contains(value))
            return false;

        values.append(value);
        return true;
    }

    @Override
    public boolean remove(Object value) {
        return values.remove(value);
    }

    @Override
    public void clear() {
        values.clear();
    }

    //endregion

    //region Element Conditions

    @Override
    public boolean contains(Object value) {
        return values.contains(value);
    }

    //endregion

    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        return values.toArray(generator);
    }
}
