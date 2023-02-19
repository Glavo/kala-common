package kala.collection.base;

import kala.annotations.Contravariant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface Growable<@Contravariant T> {

    @ApiStatus.Experimental
    static <T> @NotNull Growable<T> by(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        return consumer::accept;
    }

    @ApiStatus.Experimental
    static <T> @NotNull Growable<T> by(java.util.@NotNull Collection<? super T> list) {
        Objects.requireNonNull(list);
        return list::add;
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
