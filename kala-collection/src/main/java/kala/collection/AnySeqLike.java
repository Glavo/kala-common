package kala.collection;

import java.util.RandomAccess;

public interface AnySeqLike<E> extends AnyCollectionLike<E> {
    default boolean supportsFastRandomAccess() {
        return this instanceof RandomAccess;
    }
}
