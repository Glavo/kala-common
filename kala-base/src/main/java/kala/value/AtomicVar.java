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

import java.io.Serial;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("NullableProblems")
public final class AtomicVar<T> extends AtomicReference<T> implements MutableValue<T> {
    @Serial
    private static final long serialVersionUID = 0L;

    public AtomicVar() {
    }

    public AtomicVar(T initialValue) {
        super(initialValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ||
               obj instanceof AnyValue<?> other
               && this.canEqual(other)
               && !other.canEqual(this)
               && Objects.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "AtomicVar[" + get() + "]";
    }
}
