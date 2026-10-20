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

import kala.collection.base.primitive.${Name}MapIterator;
import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.mutable.primitive.Mutable${Name}TreeMap;
import kala.collection.primitive.${Name}MapLike;
import kala.collection.primitive.Abstract${Name}Map;
import kala.collection.primitive.Sorted${Name}Map;
import kala.comparator.primitive.IntComparator;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// An immutable map with primitive int keys in comparator order.
/// Snapshots own their tree nodes; later source changes do not affect their mappings.
/// The comparator requirements are specified by [Sorted${Name}Map].
///
/// @implNote Updates copy the tree in O(n) time before applying the change.
/// Lookup takes O(log n) comparisons.
<#if !IsPrimitiveValue>
///
/// @param <V> the value type; null values are permitted and values are retained by reference
</#if>
public final class Immutable${Name}TreeMap${Generic} extends Abstract${Name}Map${Generic}
        implements ImmutableSorted${Name}Map${Generic} {
    /// Privately owned storage, never modified after construction or exposed to callers.
    private final Mutable${Name}TreeMap${Generic} entries;

    /// Takes exclusive ownership of an independent tree.
    private Immutable${Name}TreeMap(Mutable${Name}TreeMap${Generic} entries) {
        this.entries = entries;
    }

    /// Returns a factory using natural key order.
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Immutable${Name}TreeMap${Generic}> factory() {
        return Immutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory(null);
    }

    /// Returns a factory using the given comparator, or natural order if it is null.
    /// Boxed keys<#if IsPrimitiveValue> and values</#if> are unboxed when added.
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Immutable${Name}TreeMap${Generic}> factory(
            @Nullable IntComparator comparator) {
        return new Factory${Diamond}(comparator);
    }

    /// Creates a builder using natural key order.
    /// Each build returns an immutable snapshot unaffected by subsequent additions.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}TreeMap${Generic}> newMapBuilder() {
        return Immutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder using the given comparator, or natural order if it is null.
    /// Each build returns an immutable snapshot unaffected by subsequent additions.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}TreeMap${Generic}> newMapBuilder(
            @Nullable IntComparator comparator) {
        return Immutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory(comparator).newMapBuilder();
    }

    /// Returns an empty immutable map using natural key order.
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} empty() {
        return of();
    }

    /// Returns an empty immutable map using the comparator, or natural order if it is null.
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} empty(@Nullable IntComparator comparator) {
        return of(comparator);
    }

<#list 0..5 as arity>
    /// Creates a map in natural key order; the last value wins for duplicate keys.
<#if arity == 0>
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} of() {
<#else>
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        return Immutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>of(null<#list 1..arity as index><#if arity gt 0>, key${index}, value${index}</#if></#list>);
    }

    /// Creates a map using the comparator, or natural order if it is null.
    /// Keys comparing as zero share a mapping; the first key and last value are retained.
<#if arity == 0>
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} of(@Nullable IntComparator comparator) {
<#else>
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} of(
            @Nullable IntComparator comparator,
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        return new Immutable${Name}TreeMap${Diamond}(Mutable${Name}TreeMap.of(comparator<#list 1..arity as index><#if arity gt 0>, key${index}, value${index}</#if></#list>));
    }

</#list>
    /// Copies parallel arrays into a map using natural key order.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return from(null, keys, values);
    }

    /// Copies parallel arrays using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first key and last value.
    /// Subsequent array changes do not affect the map; object values are not cloned.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator, int @NotNull [] keys, ${Value} @NotNull [] values) {
        return new Immutable${Name}TreeMap${Diamond}(Mutable${Name}TreeMap.from(comparator, keys, values));
    }

    /// Copies mappings, preserving the comparator of a [Sorted${Name}Map] source.
    /// Other sources use natural key order. Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return from(source instanceof Sorted${Name}Map${Wildcard} sorted ? sorted.comparator() : null, source);
    }

    /// Copies mappings using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first encountered key and last value.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator, @NotNull ${Name}MapLike${Covariant} source) {
        return new Immutable${Name}TreeMap${Diamond}(Mutable${Name}TreeMap.from(comparator, source));
    }

    /// Copies a Java map using natural key order.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return from(null, source);
    }

    /// Copies a Java sorted map, preserving its ordering.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(
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
    public static ${MethodGeneric}@NotNull Immutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator,
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return new Immutable${Name}TreeMap${Diamond}(Mutable${Name}TreeMap.from(comparator, source));
    }

    @Override
    public @Nullable IntComparator comparator() {
        return entries.comparator();
    }

    @Override
    public int firstKey() {
        return entries.firstKey();
    }

    @Override
    public int lastKey() {
        return entries.lastKey();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean containsKey(int key) {
        return entries.containsKey(key);
    }

    @Override
    public ${Value} get(int key) {
        return entries.get(key);
    }

    @Override
    public ${Value} getOrDefault(int key, ${Value} defaultValue) {
        return entries.getOrDefault(key, defaultValue);
    }

    @Override
    public @NotNull ${Option} getOption(int key) {
        return entries.getOption(key);
    }

    @Override
    public @NotNull ${Name}MapIterator${Generic} iterator() {
        return entries.iterator();
    }

    @Override
    public @NotNull Immutable${Name}TreeMap${Generic} updated(int key, ${Value} value) {
        var copy = entries.clone();
        copy.set(key, value);
        return new Immutable${Name}TreeMap${Diamond}(copy);
    }

    @Override
    public @NotNull Immutable${Name}TreeMap${Generic} removed(int key) {
        if (!containsKey(key)) {
            return this;
        }
        var copy = entries.clone();
        copy.remove(key);
        return new Immutable${Name}TreeMap${Diamond}(copy);
    }

    @Override
    public @NotNull Immutable${Name}TreeMap${Generic} toImmutableMap() {
        return this;
    }

    @Override
    public @NotNull String className() {
        return "Immutable${Name}TreeMap";
    }

    /// Builds maps using a fixed key ordering.
<#if !IsPrimitiveValue>
    ///
    /// @param <V> the value type
</#if>
    private static final class Factory${Generic}
            implements MapFactory<Integer, ${BoxedValue}, Mutable${Name}TreeMap${Generic}, Immutable${Name}TreeMap${Generic}> {
        /// Key ordering, or null for natural order.
        private final IntComparator comparator;

        /// Creates a factory with the supplied key ordering.
        private Factory(IntComparator comparator) {
            this.comparator = comparator;
        }

        @Override
        public Mutable${Name}TreeMap${Generic} newBuilder() {
            return new Mutable${Name}TreeMap${Diamond}(comparator);
        }

        @Override
        public Immutable${Name}TreeMap${Generic} build(Mutable${Name}TreeMap${Generic} builder) {
            return new Immutable${Name}TreeMap${Diamond}(builder.clone());
        }

        @Override
        public void addToBuilder(Mutable${Name}TreeMap${Generic} builder, Integer key, ${BoxedValue} value) {
            builder.set(key, value);
        }

        @Override
        public Mutable${Name}TreeMap${Generic} mergeBuilder(
                Mutable${Name}TreeMap${Generic} builder1, Mutable${Name}TreeMap${Generic} builder2) {
            builder1.putAll(builder2);
            return builder1;
        }
    }
}
