package kala.collection.mutable;

import kala.collection.IndexedSeq;
import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableIndexedSeqDeque<E> extends MutableSeqDeque<E>, IndexedSeq<E> {
    @Override
    default boolean isEmpty() {
        return IndexedSeq.super.isEmpty();
    }

    @Override
    default E first() {
        return IndexedSeq.super.first();
    }

    @Override
    default @Nullable E firstOrNull() {
        return IndexedSeq.super.firstOrNull();
    }

    @Override
    default @NotNull Option<E> firstOption() {
        return IndexedSeq.super.firstOption();
    }

    @Override
    default E last() {
        return IndexedSeq.super.last();
    }

    @Override
    default @Nullable E lastOrNull() {
        return IndexedSeq.super.lastOrNull();
    }

    @Override
    default @NotNull Option<E> lastOption() {
        return IndexedSeq.super.lastOption();
    }
}
