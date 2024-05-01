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

import java.util.Objects;

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
        return (${PrimitiveType} t) -> { accept(t); after.accept(t); };
    }
}
