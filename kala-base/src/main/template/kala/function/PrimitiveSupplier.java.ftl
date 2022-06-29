package kala.function;

/**
 * Represents a supplier of {@code ${PrimitiveType}}-valued results.  This is the
 * {@code ${PrimitiveType}}-producing primitive specialization of {@link java.util.function.Supplier}.
 *
 * <p>There is no requirement that a distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #getAs${Type}()}.
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ${Type}Supplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    ${PrimitiveType} getAs${Type}();
}
