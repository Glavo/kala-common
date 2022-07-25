package kala.collection.mutable;

import kala.collection.immutable.ImmutableSeq;
import org.jetbrains.annotations.NotNull;

public interface FreezableMutableSeq<E> extends MutableSeq<E> {

    @NotNull ImmutableSeq<E> freeze();

    @Override
    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return freeze();
    }
}
