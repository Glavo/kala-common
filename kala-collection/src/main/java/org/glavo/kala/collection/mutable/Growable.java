package org.glavo.kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

public interface Growable<T> {
    void addValue(T value);

    default void addValues(@NotNull Iterable<? extends T> values) {
        for (T value : values) {
            this.addValue(value);
        }
    }

    default void addValues(T @NotNull [] values) {
        for (T value : values) {
            this.addValue(value);
        }
    }
}
