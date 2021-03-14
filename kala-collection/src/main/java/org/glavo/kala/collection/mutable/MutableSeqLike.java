package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SeqLike;
import org.glavo.kala.comparator.Comparators;
import org.glavo.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

public interface MutableSeqLike<E> extends MutableCollectionLike<E>, SeqLike<E> {

    @Override
    default @NotNull String className() {
        return "MutableSeqLike";
    }

    @Contract(mutates = "this")
    void set(int index, E newValue);

    @Contract(mutates = "this")
    default void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void mapInPlaceIndexed(@NotNull IndexedFunction<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
        mapInPlace(mapper);
    }

    @Contract(mutates = "this")
    default void sort() {
        sort(Comparators.naturalOrder());
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void sort(Comparator<? super E> comparator) {
        Object[] values = toArray();
        Arrays.sort(values, (Comparator<? super Object>) comparator);

        for (int i = 0; i < values.length; i++) {
            this.set(i, (E) values[i]);
        }
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size / 2; i++) {
            E tem = get(i);
            set(i, get(size - i - 1));
            set(size - i - 1, tem);
        }
    }
}
