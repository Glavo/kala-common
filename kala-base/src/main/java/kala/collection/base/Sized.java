package kala.collection.base;

import kala.annotations.UnstableName;

@UnstableName
public interface Sized {
    boolean isEmpty();

    boolean isNotEmpty();

    int size();

    int knownSize();
}
