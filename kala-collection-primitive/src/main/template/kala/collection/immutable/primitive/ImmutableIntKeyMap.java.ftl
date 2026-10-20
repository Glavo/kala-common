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
package kala.collection.immutable.primitive;

import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.primitive.${Name}Map;
import kala.collection.primitive.${Name}MapLike;
import org.jetbrains.annotations.NotNull;

/// A map whose associations cannot change after construction.
<#if !IsPrimitiveValue>
/// Object values themselves may remain mutable. Copying does not clone those values.
///
/// @param <V> the value type; null values are permitted
</#if>
public interface Immutable${Name}Map${Generic} extends ${Name}Map${Generic} {

    //region Static Factories

    /// Returns a factory accepting boxed keys and values for interoperability with [MapFactory].
    /// Primitive components are unboxed when entries are added.
    @SuppressWarnings({"unchecked", "rawtypes"})
    static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Immutable${Name}Map${Generic}> factory() {
        return (MapFactory) Immutable${Name}HashMap.factory();
    }

    /// Creates a builder for this map type.
    /// Each build returns an immutable snapshot unaffected by later additions.
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}Map${Generic}> newMapBuilder() {
        return Immutable${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder sized for the expected number of entries.
    /// Each build returns an immutable snapshot unaffected by later additions.
    ///
    /// @throws IllegalArgumentException if capacity is negative or exceeds 2^29
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}Map${Generic}> newMapBuilder(int capacity) {
        return Immutable${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder(capacity);
    }

    /// Returns an empty immutable map.
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} empty() {
        return Immutable${Name}HashMap.empty();
    }

    /// Returns an empty immutable map.
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} of() {
        return empty();
    }

    /// Returns an immutable map containing the given entry.
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} of(int key, ${Value} value) {
        return Immutable${Name}HashMap.of(key, value);
    }

    /// Returns an immutable map of the entries; the second value wins for equal keys.
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} of(int key1, ${Value} value1, int key2, ${Value} value2) {
        return Immutable${Name}HashMap.of(key1, value1, key2, value2);
    }

<#list 3..5 as arity>
    /// Creates an immutable map containing the supplied entries; the last value wins for duplicate keys.
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
        return Immutable${Name}HashMap.of(<#list 1..arity as index>key${index}, value${index}<#sep>, </#list>);
    }

</#list>
    /// Copies parallel arrays; later values replace earlier values for duplicate keys.
    /// Subsequent array changes do not affect the map.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return Immutable${Name}HashMap.from(keys, values);
    }

    /// Copies the mappings into an immutable snapshot.
    ///
    /// @throws NullPointerException if the source is null
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return Immutable${Name}HashMap.from(source);
    }

    /// Copies a Java map into an immutable snapshot.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull Immutable${Name}Map${Generic} from(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return Immutable${Name}HashMap.from(source);
    }

    //endregion

    @Override
    default @NotNull Immutable${Name}Map${Generic} toImmutableMap() {
        return this;
    }

    @Override
    default @NotNull String className() {
        return "Immutable${Name}Map";
    }
}
