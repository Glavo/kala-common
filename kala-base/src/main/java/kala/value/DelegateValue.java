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
package kala.value;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

final class DelegateValue<T> extends AbstractValue<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private final @NotNull Supplier<? extends T> getter;

    DelegateValue(@NotNull Supplier<? extends T> getter) {
        this.getter = getter;
    }

    @Override
    public T get() {
        return getter.get();
    }
}
