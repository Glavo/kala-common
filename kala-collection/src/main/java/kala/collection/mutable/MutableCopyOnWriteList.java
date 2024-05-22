package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.convert.AsImmutableConvert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MutableCopyOnWriteList<E> extends MutableCopyOnWriteSeqBase<E, MutableList<E>> implements FreezableMutableList<E> {
    MutableCopyOnWriteList(MutableList<E> source, boolean exclusive) {
        super(source, exclusive);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableCopyOnWriteList<E> create() {
        return new MutableCopyOnWriteList<>(new MutableArrayList<>(), true);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCopyOnWriteList<E> create(@NotNull CollectionFactory<E, ?, ? extends MutableList<E>> factory) {
        return new MutableCopyOnWriteList<>(factory.empty(), true);
    }

    @Override
    public @NotNull String className() {
        return "MutableCopyOnWriteList";
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableCopyOnWriteList<E> clone() {
        this.exclusive = false;
        return new MutableCopyOnWriteList<>(source, false);
    }

    @Override
    public void append(E value) {
        ensureExclusive();
        source.append(value);
    }

    @Override
    public void prepend(E value) {
        ensureExclusive();
        source.prepend(value);
    }

    @Override
    public void insert(int index, E value) {
        ensureExclusive();
        source.insert(index, value);
    }

    @Override
    public E removeAt(int index) {
        ensureExclusive();
        return source.removeAt(index);
    }

    @Override
    public void removeInRange(int beginIndex, int endIndex) {
        ensureExclusive();
        source.removeInRange(beginIndex, endIndex);
    }

    @Override
    public void clear() {
        if (exclusive) {
            source.clear();
        } else {
            exclusive = true;
            source = source.<E>iterableFactory().empty();
        }
    }

    @Override
    public @NotNull ImmutableSeq<E> freeze() {
        this.exclusive = false;
        return new AsImmutableConvert.SeqWrapper<>(source);
    }
}
