package kala.collection.factory;

import kala.collection.base.Growable;

import java.util.function.Consumer;

public interface CollectionBuilder<E, R> extends Growable<E>, Consumer<E> {

    void plusAssign(E value);

    R build();

    default void sizeHint(int size) {
    }

    @Override
    default void accept(E value) {
        plusAssign(value);
    }
}
