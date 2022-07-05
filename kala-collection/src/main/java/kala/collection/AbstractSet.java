package kala.collection;

public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    @Override
    public int hashCode() {
        return Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySet<?> && Set.equals(this, ((AnySet<?>) obj));
    }
}
