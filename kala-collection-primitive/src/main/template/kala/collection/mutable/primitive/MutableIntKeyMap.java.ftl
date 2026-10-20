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
package kala.collection.mutable.primitive;

import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.primitive.${Name}Map;
import kala.collection.primitive.${Name}MapLike;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
<#if IsPrimitiveValue>
import java.util.function.IntSupplier;
<#else>
import java.util.function.Supplier;
</#if>

/// A mutable map with primitive int keys. Implementations need not be thread-safe.
<#if !IsPrimitiveValue>
/// Null values are permitted and count as present mappings.
///
/// @param <V> the value type
</#if>
public interface Mutable${Name}Map${Generic} extends ${Name}Map${Generic} {

    //region Static Factories

    /// Returns a factory accepting boxed keys and values for interoperability with [MapFactory].
    /// Primitive components are unboxed when entries are added.
    @SuppressWarnings({"unchecked", "rawtypes"})
    static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Mutable${Name}Map${Generic}> factory() {
        return (MapFactory) Mutable${Name}HashMap.factory();
    }

    /// Creates a builder for this map type.
    /// The builder returns its mutable accumulation map; later additions modify that map.
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}Map${Generic}> newMapBuilder() {
        return Mutable${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder sized for the expected number of entries.
    /// The builder returns its mutable accumulation map; later additions modify that map.
    ///
    /// @throws IllegalArgumentException if capacity is negative or exceeds 2^29
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}Map${Generic}> newMapBuilder(int capacity) {
        return Mutable${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder(capacity);
    }

    /// Creates an empty mutable hash map.
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} create() {
        return new Mutable${Name}HashMap${Diamond}();
    }

    /// Creates an empty mutable hash map.
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} of() {
        return create();
    }

    /// Creates a mutable hash map containing the given entry.
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} of(int key, ${Value} value) {
        return Mutable${Name}HashMap.of(key, value);
    }

    /// Creates a mutable map of the entries; the second value wins for equal keys.
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} of(int key1, ${Value} value1, int key2, ${Value} value2) {
        return Mutable${Name}HashMap.of(key1, value1, key2, value2);
    }

<#list 3..5 as arity>
    /// Creates a mutable map containing the supplied entries; the last value wins for duplicate keys.
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
        return Mutable${Name}HashMap.of(<#list 1..arity as index>key${index}, value${index}<#sep>, </#list>);
    }

</#list>
    /// Copies the mappings into an independent mutable hash map.
    ///
    /// @throws NullPointerException if the source is null
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return Mutable${Name}HashMap.from(source);
    }

    /// Copies parallel arrays; later values replace earlier values for duplicate keys.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return Mutable${Name}HashMap.from(keys, values);
    }

    /// Copies a Java map into an independent mutable map.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull Mutable${Name}Map${Generic} from(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return Mutable${Name}HashMap.from(source);
    }

    //endregion

    /// Associates the key with the value and returns the previous value, if present.
    /// An empty option means that a new key was inserted.
    @Contract(mutates = "this")
    @NotNull ${Option} put(int key, ${Value} value);

    /// Associates the key with the value, discarding any previous value.
    @Contract(mutates = "this")
    default void set(int key, ${Value} value) {
        put(key, value);
    }

    /// Removes the key and returns its previous value, or an empty option if absent.
    @Contract(mutates = "this")
    @NotNull ${Option} remove(int key);

    /// Removes all mappings; repeated calls on an empty map have no effect.
    @Contract(mutates = "this")
    void clear();

    /// Inserts the value only if the key is absent, returning the existing value if present.
    default @NotNull ${Option} putIfAbsent(int key, ${Value} value) {
        var old = getOption(key);
        if (old.isEmpty()) {
            set(key, value);
        }
        return old;
    }

    /// Returns the associated value, or computes, inserts, and returns one for an absent key.
    /// If the supplier throws, this method does not insert a value.
    /// The supplier must not modify this map. This operation is not atomic.
    ///
    /// @throws NullPointerException if the supplier is null
    default ${Value} getOrPut(int key, @NotNull ${Supplier} supplier) {
        Objects.requireNonNull(supplier);
        var old = getOption(key);
        if (old.isDefined()) {
            return old.get();
        }
        ${Value} value = supplier.${Supply}();
        set(key, value);
        return value;
    }

    /// Copies the source mappings, replacing values of existing keys.
    /// Copying this map into itself has no effect. If traversal fails, entries already
    /// copied are retained.
    ///
    /// @throws NullPointerException if the source is null
    default void putAll(@NotNull ${Name}MapLike${Covariant} source) {
        Objects.requireNonNull(source);
        if (source == this) {
            return;
        }
        var it = source.iterator();
        while (it.hasNext()) {
            set(it.nextIntKey(), it.${ValueGetter}());
        }
    }

    /// Copies a Java map, replacing existing mappings. Partial progress is retained on failure.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    default void putAll(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        Objects.requireNonNull(source);
        source.forEach((key, value) -> set(key, value));
    }

    @Override
    default @NotNull String className() {
        return "Mutable${Name}Map";
    }
}
