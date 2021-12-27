package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableIndexedListDeque<E> extends MutableList<E>, MutableIndexedSeqDeque<E> {
    @Override
    default E removeFirst() {
        return MutableList.super.removeFirst();
    }

    @Override
    default @Nullable E removeFirstOrNull() {
        return MutableList.super.removeFirstOrNull();
    }

    @Override
    default @NotNull Option<E> removeFirstOption() {
        return MutableList.super.removeFirstOption();
    }

    @Override
    default E removeLast() {
        return MutableList.super.removeLast();
    }

    @Override
    default @Nullable E removeLastOrNull() {
        return MutableList.super.removeLastOrNull();
    }

    @Override
    default @NotNull Option<E> removeLastOption() {
        return MutableList.super.removeLastOption();
    }
}
