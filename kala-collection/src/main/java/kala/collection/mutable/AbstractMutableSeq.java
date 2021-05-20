package kala.collection.mutable;

import kala.collection.Seq;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSeq<E> extends AbstractMutableCollection<E> implements MutableSeq<E> {
    static final int SHUFFLE_THRESHOLD = 5;

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }
}
