/*
 * Copyright 2025 Glavo
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

public record HashCollisionsValue(int value) implements Comparable<HashCollisionsValue> {

    @Override
    public int hashCode() {
        int mod = value % 16;
        return (mod << 28) | (mod << 24) | (mod << 20) | (mod << 16) | (mod << 12) | (mod << 8) | (mod << 4) | (mod);
    }

    @Override
    public int compareTo(@NotNull HashCollisionsValue that) {
        return Integer.compare(this.value, that.value);
    }
}
