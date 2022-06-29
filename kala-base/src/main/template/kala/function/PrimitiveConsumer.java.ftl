package kala.function;

import java.util.Objects;
import java.io.Serializable;

/**
 * Represents an operation that accepts a single {@code ${PrimitiveType}}-valued argument and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.Consumer} for {@code ${PrimitiveType}}.  Unlike most other functional interfaces,
 * {@code ${Type}Consumer} is expected to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(${PrimitiveType})}.
 *
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface ${Type}Consumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(${PrimitiveType} value);

    /**
     * Returns a composed {@code ${Type}Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code ${Type}Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ${Type}Consumer andThen(${Type}Consumer after) {
        Objects.requireNonNull(after);
        return (${Type}Consumer & Serializable) (${PrimitiveType} t) -> { accept(t); after.accept(t); };
    }
}
