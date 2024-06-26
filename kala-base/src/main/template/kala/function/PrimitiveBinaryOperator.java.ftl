/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
