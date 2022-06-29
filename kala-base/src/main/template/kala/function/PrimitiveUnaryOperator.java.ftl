package kala.function;

import java.util.Objects;

/**
 * Represents an operation on a single {@code ${PrimitiveType}}-valued operand that produces
 * a {@code ${PrimitiveType}}-valued result.  This is the primitive type specialization of
 * {@link java.util.function.UnaryOperator} for {@code ${PrimitiveType}}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAs${Type}(${PrimitiveType})}.
 *
 * @see java.util.function.UnaryOperator
 */
@FunctionalInterface
public interface ${Type}UnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    ${PrimitiveType} applyAs${Type}(${PrimitiveType} operand);

    /**
     * Returns a composed operator that first applies the {@code before}
     * operator to its input, and then applies this operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param before the operator to apply before this operator is applied
     * @return a composed operator that first applies the {@code before}
     * operator and then applies this operator
     * @throws NullPointerException if before is null
     *
     * @see #andThen(${Type}UnaryOperator)
     */
    default ${Type}UnaryOperator compose(${Type}UnaryOperator before) {
        Objects.requireNonNull(before);
        return (${PrimitiveType} v) -> applyAs${Type}(before.applyAs${Type}(v));
    }

    /**
     * Returns a composed operator that first applies this operator to
     * its input, and then applies the {@code after} operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param after the operator to apply after this operator is applied
     * @return a composed operator that first applies this operator and then
     * applies the {@code after} operator
     * @throws NullPointerException if after is null
     *
     * @see #compose(${Type}UnaryOperator)
     */
    default ${Type}UnaryOperator andThen(${Type}UnaryOperator after) {
        Objects.requireNonNull(after);
        return (${PrimitiveType} t) -> after.applyAs${Type}(applyAs${Type}(t));
    }

    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @return a unary operator that always returns its input argument
     */
    static ${Type}UnaryOperator identity() {
        return t -> t;
    }
}
