package kala.collection.immutable;

import kala.collection.AnySet;
import kala.collection.SortedSet;

import java.util.Spliterator;

public interface ImmutableAnySet<E> extends ImmutableAnyCollection<E>, AnySet<E> {
    @Override
    default int characteristics() {
        return this instanceof SortedSet
                ? Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.SORTED
                : Spliterator.DISTINCT;
    }
}
