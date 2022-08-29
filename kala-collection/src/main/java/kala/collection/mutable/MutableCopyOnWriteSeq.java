package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MutableCopyOnWriteSeq<E> extends MutableCopyOnWriteSeqBase<E, MutableSeq<E>> {
    MutableCopyOnWriteSeq(MutableSeq<E> source, boolean exclusive) {
        super(source, exclusive);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCopyOnWriteSeq<E> create(@NotNull CollectionFactory<E, ?, ? extends MutableSeq<E>> factory) {
        return new MutableCopyOnWriteSeq<>(factory.empty(), true);
    }

    @Override
    public @NotNull String className() {
        return "MutableCopyOnWriteSeq";
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public MutableCopyOnWriteSeq<E> clone() {
        this.exclusive = false;
        return new MutableCopyOnWriteSeq<>(source, false);
    }
}
