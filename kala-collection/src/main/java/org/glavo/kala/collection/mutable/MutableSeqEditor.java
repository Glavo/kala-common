package org.glavo.kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public class MutableSeqEditor<E, C extends MutableSeq<E>> extends MutableCollectionEditor<E, C> {
    protected MutableSeqEditor(@NotNull C source) {
        super(source);
    }

    @Contract("_, _ -> this")
    public @NotNull MutableSeqEditor<E, C> set(int index, E newValue) {
        source.set(index, newValue);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSeqEditor<E, C> mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        source.mapInPlace(mapper);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSeqEditor<E, C> replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
        source.replaceAll(mapper);
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSeqEditor<E, C> reverse() {
        source.reverse();
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSeqEditor<E, C> sort() {
        source.sort();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSeqEditor<E, C> sort(@NotNull Comparator<? super E> comparator) {
        source.sort(comparator);
        return this;
    }
}
