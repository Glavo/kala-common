package kala.function;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of one {@code ${PrimitiveType}}-valued
 * argument. This is the {@code ${PrimitiveType}}-consuming primitive type specialization of
 * {@link java.util.function.Predicate}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(${PrimitiveType})}.
 *
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface ${Type}Predicate {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(${PrimitiveType} value);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default ${Type}Predicate and(${Type}Predicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default ${Type}Predicate negate() {
        return (value) -> !test(value);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default ${Type}Predicate or(${Type}Predicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }

    static ${Type}Predicate isEqual(${PrimitiveType} target) {
        return (value) -> value == target;
    }

    static ${Type}Predicate not(${Type}Predicate target) {
        Objects.requireNonNull(target);
        return target.negate();
    }
}
