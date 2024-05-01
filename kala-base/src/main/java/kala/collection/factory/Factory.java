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
package kala.collection.factory;

import kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface Factory<Builder, @Covariant R> {
    Builder newBuilder();

    R build(Builder builder);

    default <U> @NotNull Factory<Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final class MappedFactory implements Factory<Builder, U> {
            @Override
            public Builder newBuilder() {
                return Factory.this.newBuilder();
            }

            @Override
            public U build(@NotNull Builder builder) {
                return mapper.apply(Factory.this.build(builder));
            }
        }

        return new MappedFactory();
    }
}
