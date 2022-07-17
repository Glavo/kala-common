package kala.collection.primitive;

import kala.collection.AnySet;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class Abstract${Type}Set extends Abstract${Type}Collection implements ${Type}Set {
    @Override
    public int hashCode() {
        return ${Type}Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySet<?> && ${Type}Set.equals(this, ((AnySet<?>) obj));
    }
}
