package kala.value;

import kala.Equatable;
import kala.collection.base.AnyTraversable;

public interface AnyValue<T> extends AnyTraversable<T>, Equatable {
    int HASH_MAGIC = 1966548148;

    T getValue();
}
