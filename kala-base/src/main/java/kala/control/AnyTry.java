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
package kala.control;

import kala.annotations.Covariant;
import kala.control.primitive.PrimitiveTry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.Supplier;

public sealed abstract class AnyTry<@Covariant T> implements Serializable permits Try, PrimitiveTry {

    protected static final int SUCCESS_HASH_MAGIC = 518848667;
    protected static final int FAILURE_HASH_MAGIC = 1918688519;

    /**
     * Returns {@code true} if the {@code Try} is {@code Success}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Success}, otherwise {@code false}
     */
    public abstract boolean isSuccess();

    /**
     * Returns {@code true} if the {@code Try} is {@code Failure}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Failure}, otherwise {@code false}
     */
    public abstract boolean isFailure();

    public abstract T getValue();

    public abstract @Nullable T getOrNull();

    public abstract AnyOption<T> getOption();

    public abstract @NotNull Throwable getCause();

    public @Nullable Throwable getCauseOrNull() {
        return isFailure() ? getCause() : null;
    }

    public @NotNull Option<Throwable> getCauseOption() {
        return isFailure() ? Option.some(getCause()) : Option.none();
    }

    public Throwable getCauseOrDefault(Throwable defaultValue) {
        return isFailure() ? getCause() : defaultValue;
    }

    public Throwable getCauseOrElse(@NotNull Supplier<? extends Throwable> supplier) {
        return isFailure() ? getCause() : supplier.get();
    }

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws Ex if the {@code Try} is a {@code Failure}
     */
    public abstract <Ex extends Throwable> @NotNull AnyTry<T> rethrow() throws Ex;

    /**
     * If the {@code Try} is a {@code Failure} and the {@code throwable} is an instance of {@code type},
     * throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * or the {@code throwable} not an instance of {@code type}
     * @throws Ex if the {@code Try} is a {@code Failure} and the {@code throwable}'s type is {@code type}
     */
    public abstract <Ex extends Throwable> @NotNull AnyTry<T> rethrow(@NotNull Class<? extends Ex> type) throws Ex;

    public abstract @NotNull AnyTry<T> rethrowFatal();
}
