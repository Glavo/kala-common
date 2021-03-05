package org.glavo.kala.collection;

public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    @Override
    public int hashCode() {
        int h = SET_HASH_MAGIC;
        for (E e : this) {
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Set<?>) || !canEqual(obj) || !((Set<?>) obj).canEqual(this)) {
            return false;
        }
        return containsAll(((Set<?>) obj));
    }
}
