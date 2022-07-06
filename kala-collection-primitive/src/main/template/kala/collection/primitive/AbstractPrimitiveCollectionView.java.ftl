package kala.collection.primitive;

public abstract class Abstract${Type}CollectionView implements ${Type}CollectionView {
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
        return className() + "[<not computed>]";
    }
}
