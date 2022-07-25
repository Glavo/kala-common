package kala.collection;

import java.util.Spliterator;

public interface AnySet<E> extends AnyCollection<E>, AnySetLike<E> {
    @Override
    default int characteristics() {
        return this instanceof SortedSet ? Spliterator.SORTED | Spliterator.DISTINCT : Spliterator.DISTINCT;
    }
}
