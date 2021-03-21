package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.Collection;
import org.jetbrains.annotations.Debug;

import java.util.Random;

@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSeq<E> extends AbstractMutableCollection<E> implements MutableSeq<E> {
    static final int SHUFFLE_THRESHOLD = 5;

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }
}
