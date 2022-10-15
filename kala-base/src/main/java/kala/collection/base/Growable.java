package kala.collection.base;

import kala.annotations.Contravariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface Growable<@Contravariant T> {
    static <T> @NotNull Growable<T> wrapJavaList(java.util.@NotNull List<? super T> list) {
        Objects.requireNonNull(list);
        return list::add;
    }

    static <T> @NotNull Growable<T> wrapJavaSet(java.util.@NotNull Set<? super T> set) {
        Objects.requireNonNull(set);
        return set::add;
    }

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
