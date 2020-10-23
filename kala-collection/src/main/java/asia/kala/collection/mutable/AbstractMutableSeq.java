package asia.kala.collection.mutable;

import asia.kala.collection.*;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMutableSeq<E> extends AbstractMutableCollection<E> implements MutableSeq<E> {
    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + Collection.SEQ_HASH_MAGIC;
    }
}
