/*
 * Copyright 2026 Glavo
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
package kala.collection.primitive;

import kala.collection.AnyMap;
import kala.collection.Map;

/// Supplies mapping-based equality, hashing, and string formatting for [${Name}Map].
<#if !IsPrimitiveValue>
///
/// @param <V> the value type
</#if>
public abstract class Abstract${Name}Map${Generic} implements ${Name}Map${Generic} {
    /// Creates a map base instance.
    protected Abstract${Name}Map() {
    }

    @Override
    public int hashCode() {
        return Map.HASH_MAGIC + iterator().hash();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnyMap<?, ?> that)
                || !canEqual(that) || !that.canEqual(this) || size() != that.size()) {
            return false;
        }
        if (that instanceof ${Name}Map${Wildcard} specialized) {
            var it = specialized.iterator();
            while (it.hasNext()) {
                if (!contains(it.nextIntKey(), it.${ValueGetter}())) {
                    return false;
                }
            }
        } else {
            var it = that.iterator();
            while (it.hasNext()) {
                if (!(it.nextKey() instanceof Integer key)) {
                    return false;
                }
<#if IsPrimitiveValue>
                if (!(it.getValue() instanceof Integer value) || !contains(key.intValue(), value.intValue())) {
                    return false;
                }
<#else>
                if (!contains(key.intValue(), it.getValue())) {
                    return false;
                }
</#if>
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return joinToString(", ", className() + "{", "}");
    }
}
