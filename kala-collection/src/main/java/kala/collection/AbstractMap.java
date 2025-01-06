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
package kala.collection;

import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMap<K, V> implements Map<K, V> {

    @Override
    public int hashCode() {
        return Map.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AnyMap<?, ?> other)) {
            return false;
        }

        return this.canEqual(other) && other.canEqual(this) && Map.equals(this, other);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(className());
        builder.append('{');
        joinTo(builder);
        builder.append('}');
        return builder.toString();
    }
}
