package asia.kala.collection;

import asia.kala.iterator.Iterators;

public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {
    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + Collection.SEQ_HASH_MAGIC;
    }
}
