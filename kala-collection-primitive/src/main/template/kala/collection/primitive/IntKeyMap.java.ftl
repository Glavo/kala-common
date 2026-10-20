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
import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.primitive.Immutable${Name}HashMap;
import kala.collection.immutable.primitive.Immutable${Name}Map;
import kala.collection.mutable.primitive.Mutable${Name}HashMap;
import org.jetbrains.annotations.NotNull;

/// A map with primitive int keys and <#if IsPrimitiveValue>int<#else>object</#if> values.
/// Factory methods return immutable snapshots; duplicate keys retain the last supplied value.
/// Equality compares mappings with other [AnyMap] instances that permit comparison,
/// independent of iteration order and primitive specialization.
<#if !IsPrimitiveValue>
/// Values, including null, are compared using [java.util.Objects#equals(Object, Object)].
///
/// @param <V> the value type
</#if>
public interface ${Name}Map${Generic} extends ${Name}MapLike${Generic}, PrimitiveMap<Integer, ${BoxedValue}> {

    //region Static Factories

    /// Returns a factory accepting boxed keys and values for interoperability with [MapFactory].
    /// Primitive components are unboxed when entries are added.
    @SuppressWarnings({"unchecked", "rawtypes"})
    static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, ${Name}Map${Generic}> factory() {
        return (MapFactory) Immutable${Name}HashMap.factory();
    }

    /// Creates a builder for this map type.
    /// Each build returns an immutable snapshot unaffected by later additions.
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, ${Name}Map${Generic}> newMapBuilder() {
        return ${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder sized for the expected number of entries.
    /// Each build returns an immutable snapshot unaffected by later additions.
    ///
    /// @throws IllegalArgumentException if capacity is negative or exceeds 2^29
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, ${Name}Map${Generic}> newMapBuilder(int capacity) {
        return ${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder(capacity);
    }

    /// Returns an empty immutable map.
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} empty() {
        return Immutable${Name}Map.of();
    }

    /// Returns an empty immutable map.
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} of() {
        return empty();
    }

    /// Returns an immutable map containing the given entry.
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} of(int key, ${Value} value) {
        return Immutable${Name}Map.of(key, value);
    }

    /// Returns an immutable map of the entries; the second value wins for equal keys.
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} of(int key1, ${Value} value1, int key2, ${Value} value2) {
        return Immutable${Name}Map.of(key1, value1, key2, value2);
    }

<#list 3..5 as arity>
    /// Creates an immutable map containing the supplied entries; the last value wins for duplicate keys.
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
        return Immutable${Name}HashMap.of(<#list 1..arity as index>key${index}, value${index}<#sep>, </#list>);
    }

</#list>
    /// Copies parallel key and value arrays into an immutable map.
    /// Later occurrences of a key replace earlier values; subsequent array changes have no effect.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return Immutable${Name}Map.from(keys, values);
    }

    /// Copies the mappings into an immutable snapshot.
    ///
    /// @throws NullPointerException if the source is null
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return Immutable${Name}Map.from(source);
    }

    /// Copies a Java map into an immutable snapshot.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull ${Name}Map${Generic} from(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return Immutable${Name}Map.from(source);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "${Name}Map";
    }

    /// Returns the number of mappings.
    ///
    /// @implSpec The default implementation delegates to [#size()].
    @Override
    default int knownSize() {
        return size();
    }

    /// Returns whether the other object belongs to this map's equality domain.
    @Override
    default boolean canEqual(Object other) {
        return other instanceof AnyMap<?, ?>;
    }

    /// Returns an immutable snapshot with the given association, leaving this map unchanged.
    default @NotNull Immutable${Name}Map${Generic} updated(int key, ${Value} value) {
        var copy = Mutable${Name}HashMap.from(this);
        copy.set(key, value);
        return Immutable${Name}Map.from(copy);
    }

    /// Returns an immutable snapshot without the key, leaving this map unchanged.
    default @NotNull Immutable${Name}Map${Generic} removed(int key) {
        var copy = Mutable${Name}HashMap.from(this);
        copy.remove(key);
        return Immutable${Name}Map.from(copy);
    }

    /// Returns an immutable snapshot; object values themselves are not copied.
    default @NotNull Immutable${Name}Map${Generic} toImmutableMap() {
        return Immutable${Name}Map.from(this);
    }
}
