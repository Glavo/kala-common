package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableSeqDeque<E> extends MutableSeq<E>, MutableDeque<E> {
    @Override
    default boolean isEmpty() {
        return MutableSeq.super.isEmpty();
    }

    @Override
    default boolean isNotEmpty() {
        return MutableSeq.super.isNotEmpty();
    }

    @Override
    default E first() {
        return MutableSeq.super.first();
    }

    @Override
    default @Nullable E firstOrNull() {
        return MutableSeq.super.firstOrNull();
    }

    @Override
    default @NotNull Option<E> firstOption() {
        return MutableSeq.super.firstOption();
    }

    @Override
    default E last() {
        return MutableSeq.super.last();
    }

    @Override
    default @Nullable E lastOrNull() {
        return MutableSeq.super.lastOrNull();
    }

    @Override
    default @NotNull Option<E> lastOption() {
        return MutableSeq.super.lastOption();
    }
}
