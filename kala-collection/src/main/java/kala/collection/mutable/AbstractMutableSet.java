package kala.collection.mutable;

import kala.collection.AbstractSet;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSet<E> extends AbstractSet<E> implements MutableSet<E> {
    @Override
    public @NotNull MutableSet<E> clone() {
        return MutableSet.super.clone();
    }
}
