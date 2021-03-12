package org.glavo.kala.collection;

public abstract class AbstractMapView<K, V> implements MapView<K, V> {
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return className() + "{<not computed>}";
    }
}
