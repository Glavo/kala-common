package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableListDeque<E> extends MutableList<E>, MutableDeque<E> {
    @Override
    default boolean isEmpty() {
        return MutableList.super.isEmpty();
    }

    @Override
    default E first() {
        return MutableList.super.first();
    }

    @Override
    default @Nullable E firstOrNull() {
        return MutableList.super.firstOrNull();
    }

    @Override
    default @NotNull Option<E> firstOption() {
        return MutableList.super.firstOption();
    }

    @Override
    default E last() {
        return MutableList.super.last();
    }

    @Override
    default @Nullable E lastOrNull() {
        return MutableList.super.lastOrNull();
    }

    @Override
    default @NotNull Option<E> lastOption() {
        return MutableList.super.lastOption();
    }

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
