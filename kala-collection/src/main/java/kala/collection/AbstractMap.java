package kala.collection;

import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMap<K, V> implements Map<K, V> {

    @Override
    public int hashCode() {
        return Map.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Map<?, ?> && Map.equals(this, ((Map<?, ?>) obj)));
    }

    @Override
    public String toString() {
        return className() + '{' + joinToString() + '}';
    }
}
