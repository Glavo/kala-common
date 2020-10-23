package asia.kala.function;

/**
 * Represents an operation that accepts an index and an object-valued argument, and returns no result.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(int, Object)}.
 *
 * @param <T> the type of the input to the operation
 * @author Glavo
 */
@FunctionalInterface
public interface IndexedConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param index the index of {@code t} in the container
     * @param t     the input argument
     */
    void accept(int index, T t);
}
