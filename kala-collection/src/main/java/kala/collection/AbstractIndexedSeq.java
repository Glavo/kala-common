package kala.collection;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractIndexedSeq<E> extends AbstractSeq<E> implements IndexedSeqLike<E>, Seq<E>, RandomAccess {
    public E first() {
        return IndexedSeqLike.super.first();
    }

    public @Nullable E firstOrNull() {
        return IndexedSeqLike.super.firstOrNull();
    }

    public @NotNull Option<E> firstOption() {
        return IndexedSeqLike.super.firstOption();
    }

    public E last() {
        return IndexedSeqLike.super.last();
    }

    public @Nullable E lastOrNull() {
        return IndexedSeqLike.super.lastOrNull();
    }

    public @NotNull Option<E> lastOption() {
        return IndexedSeqLike.super.lastOption();
    }

}
