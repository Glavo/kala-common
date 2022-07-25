package kala.collection;

import kala.Equatable;

public interface AnyCollection<E> extends AnyCollectionLike<E>, Equatable {
    int SEQ_HASH_MAGIC = -1140647423;

    int SET_HASH_MAGIC = 1045751549;
}
