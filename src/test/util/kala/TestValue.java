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
package kala;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record TestValue<T extends Comparable<T>>(T value, int hash) implements Comparable<TestValue<T>> {

    public TestValue(T value) {
        this(value, Objects.hashCode(value));
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int compareTo(@NotNull TestValue<T> other) {
        return value.compareTo(other.value);
    }
}
