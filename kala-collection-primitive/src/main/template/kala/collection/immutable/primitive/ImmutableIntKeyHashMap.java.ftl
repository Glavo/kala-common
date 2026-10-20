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
import kala.collection.mutable.primitive.Mutable${Name}HashMap;
import kala.collection.primitive.${Name}MapLike;
import kala.collection.primitive.Abstract${Name}Map;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import org.jetbrains.annotations.NotNull;

/// An immutable hash map with primitive int keys and unspecified iteration order.
/// Factories produce immutable snapshots; later input mutations do not affect the stored associations.
<#if !IsPrimitiveValue>
/// Object values, including null, are retained by reference.
///
/// @param <V> the value type
</#if>
public final class Immutable${Name}HashMap${Generic} extends Abstract${Name}Map${Generic}
        implements Immutable${Name}Map${Generic} {
    /// Shared empty map; its privately owned table is never modified.
    private static final Immutable${Name}HashMap${Wildcard} EMPTY =
            new Immutable${Name}HashMap${Diamond}(new Mutable${Name}HashMap${Diamond}());

    /// Shared stateless factory for this map implementation.
    private static final Factory${Wildcard} FACTORY = new Factory${Diamond}();

    /// Privately owned storage that is never modified after construction or exposed to callers.
    private final Mutable${Name}HashMap${Generic} entries;

    /// Takes exclusive ownership of a newly created table.
    private Immutable${Name}HashMap(Mutable${Name}HashMap${Generic} entries) {
        this.entries = entries;
    }

    //region Static Factories

    /// Returns a factory accepting boxed keys and values for interoperability with [MapFactory].
    /// Primitive components are unboxed when entries are added.
<#if !IsPrimitiveValue>
    @SuppressWarnings("unchecked")
</#if>
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Immutable${Name}HashMap${Generic}> factory() {
        return <#if !IsPrimitiveValue>(Factory<V>) </#if>FACTORY;
    }

    /// Creates a builder for this map type.
    /// Each build returns an immutable snapshot unaffected by later additions.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}HashMap${Generic}> newMapBuilder() {
        return Immutable${Name}HashMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder sized for the expected number of entries.
    /// Each build returns an immutable snapshot unaffected by later additions.
    ///
    /// @throws IllegalArgumentException if capacity is negative or exceeds 2^29
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Immutable${Name}HashMap${Generic}> newMapBuilder(int capacity) {
        return Immutable${Name}HashMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder(capacity);
    }

    /// Returns an empty immutable map.
<#if !IsPrimitiveValue>
    @SuppressWarnings("unchecked")
</#if>
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} empty() {
        return <#if !IsPrimitiveValue>(Immutable${Name}HashMap<V>) </#if>EMPTY;
    }

    /// Returns an empty immutable map.
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} of() {
        return empty();
    }

    /// Returns an immutable map containing the given entry.
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} of(int key, ${Value} value) {
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.of(key, value));
    }

    /// Returns an immutable map of the entries; the second value wins for equal keys.
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} of(int key1, ${Value} value1, int key2, ${Value} value2) {
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.of(key1, value1, key2, value2));
    }

<#list 3..5 as arity>
    /// Creates an immutable map containing the supplied entries; the last value wins for duplicate keys.
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.of(<#list 1..arity as index>key${index}, value${index}<#sep>, </#list>));
    }

</#list>
    /// Copies parallel arrays; later values replace earlier values for duplicate keys.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        if (keys.length == 0 && values.length == 0) {
            return empty();
        }
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.from(keys, values));
    }

    /// Copies mappings into an immutable snapshot; object values themselves are not copied.
    ///
    /// @throws NullPointerException if the source is null
<#if !IsPrimitiveValue>
    @SuppressWarnings("unchecked")
</#if>
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        if (source instanceof Immutable${Name}HashMap${Wildcard} immutable) {
            return <#if !IsPrimitiveValue>(Immutable${Name}HashMap<V>) </#if>immutable;
        }
        if (source.isEmpty()) {
            return empty();
        }
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.from(source));
    }

    /// Copies a Java map into an immutable snapshot.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Immutable${Name}HashMap${Generic} from(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        if (source.isEmpty()) {
            return empty();
        }
        return new Immutable${Name}HashMap${Diamond}(Mutable${Name}HashMap.from(source));
    }

    //endregion

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
    public @NotNull Immutable${Name}HashMap${Generic} updated(int key, ${Value} value) {
        var copy = entries.clone();
        copy.set(key, value);
        return new Immutable${Name}HashMap${Diamond}(copy);
    }

    @Override
    public @NotNull Immutable${Name}HashMap${Generic} removed(int key) {
        if (!containsKey(key)) {
            return this;
        }
        if (size() == 1) {
            return empty();
        }
        var copy = entries.clone();
        copy.remove(key);
        return new Immutable${Name}HashMap${Diamond}(copy);
    }

    @Override
    public @NotNull String className() {
        return "Immutable${Name}HashMap";
    }

    /// Builds maps from mutable hash-table accumulators.
<#if !IsPrimitiveValue>
    ///
    /// @param <V> the value type
</#if>
    private static final class Factory${Generic}
            implements MapFactory<Integer, ${BoxedValue}, Mutable${Name}HashMap${Generic}, Immutable${Name}HashMap${Generic}> {

        /// Creates a stateless map factory.
        private Factory() {
        }

        @Override
        public Mutable${Name}HashMap${Generic} newBuilder() {
            return new Mutable${Name}HashMap${Diamond}();
        }

        @Override
        public Mutable${Name}HashMap${Generic} newBuilder(int capacity) {
            return new Mutable${Name}HashMap${Diamond}(capacity);
        }

        @Override
        public Immutable${Name}HashMap${Generic} build(Mutable${Name}HashMap${Generic} builder) {
            return Immutable${Name}HashMap.from(builder);
        }

        @Override
        public void addToBuilder(Mutable${Name}HashMap${Generic} builder, Integer key, ${BoxedValue} value) {
            builder.set(key, value);
        }

        @Override
        public Mutable${Name}HashMap${Generic} mergeBuilder(
                Mutable${Name}HashMap${Generic} builder1,
                Mutable${Name}HashMap${Generic} builder2
        ) {
            builder1.putAll(builder2);
            return builder1;
        }
    }
}
