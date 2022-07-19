package kala.collection.mutable;

import kala.collection.AbstractSet;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSet<E> extends AbstractSet<E> implements MutableSet<E> {
}
