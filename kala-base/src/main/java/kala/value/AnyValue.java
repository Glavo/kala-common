package kala.value;

import kala.Equatable;
import kala.collection.base.AnyTraversable;

public interface AnyValue<T> extends AnyTraversable<T>, Equatable {
    T getValue();
}
