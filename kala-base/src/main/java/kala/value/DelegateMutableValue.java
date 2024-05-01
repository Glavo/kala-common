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
import java.util.function.Consumer;
import java.util.function.Supplier;

final class DelegateMutableValue<T> extends AbstractMutableValue<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private final @NotNull Supplier<? extends T> getter;
    private final @NotNull Consumer<? super T> setter;

    DelegateMutableValue(@NotNull Supplier<? extends T> getter, @NotNull Consumer<? super T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public void set(T value) {
        setter.accept(value);
    }
}
