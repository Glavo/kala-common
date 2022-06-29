package kala.function;

/**
 * Represents a function that accepts a ${PrimitiveType}-valued argument and produces a
 * result.  This is the {@code ${PrimitiveType}}-consuming primitive specialization for
 * {@link java.util.function.Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(${PrimitiveType})}.
 *
 * @param <R> the type of the result of the function
 *
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface ${Type}Function<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    R apply(${PrimitiveType} value);
}
