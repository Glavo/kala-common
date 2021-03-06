package org.glavo.kala.collection;

public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {
    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Seq<?> && Seq.equals(this, ((Seq<?>) obj));
    }
}
