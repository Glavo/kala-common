package kala.collection;

import kala.Equatable;

public interface AnyCollection<E> extends AnyCollectionLike<E>, Equatable {

    @Override
    default boolean canEqual(Object other) {
        return other instanceof AnyCollection;
    }
}
