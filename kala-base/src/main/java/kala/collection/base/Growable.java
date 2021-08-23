package kala.collection.base;

import kala.annotations.Contravariant;
import org.jetbrains.annotations.NotNull;

public interface Growable<@Contravariant T> {
    
    void plusAssign(T value);

    default void plusAssign(@NotNull Iterable<? extends T> values) {
        for (T value : values) {
            this.plusAssign(value);
        }
    }

    default void plusAssign(T @NotNull [] values) {
        for (T value : values) {
            this.plusAssign(value);
        }
    }
}
