package kala.function;


/**
 * Represents an operation upon two {@code ${PrimitiveType}}-valued operands and producing an
 * {@code ${PrimitiveType}}-valued result.   This is the primitive type specialization of
 * {@link java.util.function.BinaryOperator} for {@code ${PrimitiveType}}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAs${Type}(${PrimitiveType}, ${PrimitiveType})}.
 *
 * @see java.util.function.BinaryOperator
 */
@FunctionalInterface
public interface ${Type}BinaryOperator {

    /**
     * Applies this operator to the given operands.
     *
     * @param left the first operand
     * @param right the second operand
     * @return the operator result
     */
    ${PrimitiveType} applyAs${Type}(${PrimitiveType} left, ${PrimitiveType} right);
}
