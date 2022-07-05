package kala.collection.primitive;

import kala.collection.AnySeq;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class Abstract${Type}Seq extends Abstract${Type}Collection implements ${Type}Seq {
    @Override
    public int hashCode() {
        return ${Type}Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySeq<?> && ${Type}Seq.equals(this, ((AnySeq<?>) obj));
    }
}
