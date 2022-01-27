package kala.collection.mutable;

import kala.collection.AbstractSeq;
import kala.collection.Seq;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSeq<E> extends AbstractSeq<E> implements MutableSeq<E> {
    static final int SHUFFLE_THRESHOLD = 5;
}
