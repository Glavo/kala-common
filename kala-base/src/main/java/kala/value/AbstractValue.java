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

import java.util.Objects;

public abstract class AbstractValue<T> implements Value<T> {
    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnyValue<?> other && this.canEqual(other) && other.canEqual(this) && Objects.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "Value[" + get() + "]";
    }
}
