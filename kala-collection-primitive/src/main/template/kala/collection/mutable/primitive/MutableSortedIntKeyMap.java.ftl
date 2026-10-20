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
import kala.collection.primitive.${Name}MapLike;
import kala.collection.primitive.Sorted${Name}Map;
import kala.comparator.primitive.IntComparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// A mutable sorted map whose iterator visits keys in comparator order.
/// A null comparator denotes ascending numerical order. Keys comparing as zero identify
/// the same mapping. The comparator must impose a total order and must not change its
/// ordering while the map is in use. It must be consistent with integer equality for
/// this map to satisfy the general map equality contract.
/// Comparator callbacks must not modify the map.
<#if !IsPrimitiveValue>
///
/// @param <V> the value type; null values are permitted
</#if>
public interface MutableSorted${Name}Map${Generic} extends Mutable${Name}Map${Generic}, Sorted${Name}Map${Generic} {

    /// Returns a factory using natural key order.
    static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, MutableSorted${Name}Map${Generic}> factory() {
        return MutableSorted${Name}Map.<#if !IsPrimitiveValue><V></#if>factory(null);
    }

    /// Returns a factory using the given comparator, or natural order if it is null.
    /// Boxed keys<#if IsPrimitiveValue> and values</#if> are unboxed when added.
    @SuppressWarnings({"unchecked", "rawtypes"})
    static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, MutableSorted${Name}Map${Generic}> factory(
            @Nullable IntComparator comparator) {
        return (MapFactory) Mutable${Name}TreeMap.factory(comparator);
    }

    /// Creates a builder using natural key order.
    /// The builder returns its mutable accumulation map.
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, MutableSorted${Name}Map${Generic}> newMapBuilder() {
        return MutableSorted${Name}Map.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder using the given comparator, or natural order if it is null.
    /// The builder returns its mutable accumulation map.
    static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, MutableSorted${Name}Map${Generic}> newMapBuilder(
            @Nullable IntComparator comparator) {
        return MutableSorted${Name}Map.<#if !IsPrimitiveValue><V></#if>factory(comparator).newMapBuilder();
    }

    /// Creates an empty mutable map using natural key order.
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} create() {
        return of();
    }

    /// Creates an empty mutable map using the comparator, or natural order if it is null.
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} create(@Nullable IntComparator comparator) {
        return of(comparator);
    }

<#list 0..5 as arity>
    /// Creates a map in natural key order; the last value wins for duplicate keys.
<#if arity == 0>
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} of() {
<#else>
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        return MutableSorted${Name}Map.<#if !IsPrimitiveValue><V></#if>of(null<#list 1..arity as index><#if arity gt 0>, key${index}, value${index}</#if></#list>);
    }

    /// Creates a map using the comparator, or natural order if it is null.
    /// Keys comparing as zero share a mapping; the first key and last value are retained.
<#if arity == 0>
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} of(@Nullable IntComparator comparator) {
<#else>
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} of(
            @Nullable IntComparator comparator,
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        return Mutable${Name}TreeMap.of(comparator<#list 1..arity as index><#if arity gt 0>, key${index}, value${index}</#if></#list>);
    }

</#list>
    /// Copies parallel arrays into a map using natural key order.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return from(null, keys, values);
    }

    /// Copies parallel arrays using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first key and last value.
    /// Subsequent array changes do not affect the map; object values are not cloned.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(
            @Nullable IntComparator comparator, int @NotNull [] keys, ${Value} @NotNull [] values) {
        return Mutable${Name}TreeMap.from(comparator, keys, values);
    }

    /// Copies mappings, preserving the comparator of a [Sorted${Name}Map] source.
    /// Other sources use natural key order. Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return from(source instanceof Sorted${Name}Map${Wildcard} sorted ? sorted.comparator() : null, source);
    }

    /// Copies mappings using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first encountered key and last value.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(
            @Nullable IntComparator comparator, @NotNull ${Name}MapLike${Covariant} source) {
        return Mutable${Name}TreeMap.from(comparator, source);
    }

    /// Copies a Java map using natural key order.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return from(null, source);
    }

    /// Copies a Java sorted map, preserving its ordering.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(
            java.util.@NotNull SortedMap<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        var comparator = source.comparator();
        return from(comparator == null ? null
                : comparator instanceof IntComparator primitive ? primitive : comparator::compare, source);
    }

    /// Copies a Java map using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first encountered key and last value.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    static ${MethodGeneric}@NotNull MutableSorted${Name}Map${Generic} from(
            @Nullable IntComparator comparator,
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return Mutable${Name}TreeMap.from(comparator, source);
    }

    /// Returns a factory preserving this map's ordering.
    @Override
    default @NotNull MapFactory<Integer, ${BoxedValue}, ?, ? extends MutableSorted${Name}Map${Generic}> sortedMapFactory() {
        return factory(comparator());
    }

    /// Returns a factory using the comparator, or natural order if it is null.
    @Override
    default <#if !IsPrimitiveValue><U> </#if>@NotNull MapFactory<Integer, <#if IsPrimitiveValue>Integer<#else>U</#if>, ?, ? extends MutableSorted${Name}Map<#if !IsPrimitiveValue><U></#if>> sortedMapFactory(
            @Nullable IntComparator comparator) {
        return factory(comparator);
    }

    @Override
    default @NotNull String className() {
        return "MutableSorted${Name}Map";
    }
}
