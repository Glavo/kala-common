package kala.collection.immutable;

import kala.collection.AnyCollection;

import java.util.Spliterator;

public interface ImmutableAnyCollection<E> extends AnyCollection<E> {
    @Override
    default int characteristics() {
        return Spliterator.IMMUTABLE;
    }
}
