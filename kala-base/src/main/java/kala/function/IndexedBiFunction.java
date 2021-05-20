package kala.function;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface IndexedBiFunction<T, U, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param index the index
     * @param t     the first function argument
     * @param u     the second function argument
     * @return the function result
     */
    R apply(int index, T t, U u);

    default <V> @NotNull IndexedBiFunction<T, U, V> andThen(@NotNull Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (IndexedBiFunction<T, U, V> & Serializable) (index, t, u) -> after.apply(apply(index, t, u));
    }
}
