package asia.kala.function;

/**
 * Represents a function that accepts an index and an object-valued argument, and produces a result.
 *
 * <p>This is a functional interface whose functional method is {@link #apply(int, Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author Glavo
 */
@FunctionalInterface
public interface IndexedFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param index the index of {@code t} in the container
     * @param t     the function argument
     * @return the function result
     */
    R apply(int index, T t);
}
