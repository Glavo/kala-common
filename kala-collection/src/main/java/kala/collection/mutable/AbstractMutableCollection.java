package kala.collection.mutable;

import kala.collection.AbstractCollection;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMutableCollection<E> extends AbstractCollection<E> implements MutableCollection<E> {
    @Override
    public @NotNull MutableCollection<E> clone() {
        return MutableCollection.super.clone();
    }
}
