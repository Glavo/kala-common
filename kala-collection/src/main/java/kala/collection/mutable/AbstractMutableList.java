package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMutableList<E> extends AbstractMutableSeq<E> implements MutableList<E> {
    @Override
    public E removeFirst() {
        return MutableList.super.removeFirst();
    }

    @Override
    public @Nullable E removeFirstOrNull() {
        return MutableList.super.removeFirstOrNull();
    }

    @Override
    public @NotNull Option<E> removeFirstOption() {
        return MutableList.super.removeFirstOption();
    }

    @Override
    public E removeLast() {
        return MutableList.super.removeLast();
    }

    @Override
    public @Nullable E removeLastOrNull() {
        return MutableList.super.removeLastOrNull();
    }

    @Override
    public @NotNull Option<E> removeLastOption() {
        return MutableList.super.removeLastOption();
    }
}
