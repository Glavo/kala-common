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

import kala.collection.base.primitive.${Name}MapIterator;
import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.primitive.${Name}MapLike;
import kala.collection.primitive.Abstract${Name}Map;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/// Stores mappings in an open-addressed hash table with primitive int keys.
/// All int keys are supported, including zero and both integer limits.
/// Iteration order is unspecified. This class is not thread-safe.
///
/// Iterators reject modifications made after their creation, including value replacements,
/// on a best-effort basis by throwing [ConcurrentModificationException]. Iterator removal
/// is unsupported. External synchronization is required for concurrent mutation.
<#if !IsPrimitiveValue>
/// Null values are permitted. Copies share value references, but not the table storage.
///
/// @param <V> the value type
</#if>
public final class Mutable${Name}HashMap${Generic} extends Abstract${Name}Map${Generic}
        implements Mutable${Name}Map${Generic}, Cloneable {
    /// Shared stateless factory for this map implementation.
    private static final Factory${Wildcard} FACTORY = new Factory${Diamond}();

    /// Keys indexed by hash-table slot.
    private int[] keys;
    /// Values indexed by hash-table slot.
    private ${ArrayValue}[] values;
    /// Slot states: zero for empty, one for occupied, and two for deleted.
    private byte[] states;
    /// Number of occupied slots.
    private int size;
    /// Number of occupied or deleted slots.
    private int used;
    /// Modification counter checked by iterators.
    private int version;

    /// Creates an empty map.
    public Mutable${Name}HashMap() {
        this(0);
    }

    /// Creates an empty map sized for the expected number of entries without growth.
    ///
    /// @throws IllegalArgumentException if expectedSize is negative or exceeds 2^29
    public Mutable${Name}HashMap(int expectedSize) {
        if (expectedSize < 0 || expectedSize > (1 << 29)) {
            throw new IllegalArgumentException("Invalid expected size: " + expectedSize);
        }
        int capacity = 8;
        while (capacity / 2 < expectedSize) {
            capacity <<= 1;
        }
        keys = new int[capacity];
        values = new ${ArrayValue}[capacity];
        states = new byte[capacity];
    }

    //region Static Factories

    /// Returns a factory accepting boxed keys and values for interoperability with [MapFactory].
    /// Primitive components are unboxed when entries are added.
<#if !IsPrimitiveValue>
    @SuppressWarnings("unchecked")
</#if>
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Mutable${Name}HashMap${Generic}> factory() {
        return <#if !IsPrimitiveValue>(Factory<V>) </#if>FACTORY;
    }

    /// Creates a builder for this map type.
    /// The builder returns its mutable accumulation map; later additions modify that map.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}HashMap${Generic}> newMapBuilder() {
        return Mutable${Name}HashMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder sized for the expected number of entries.
    /// The builder returns its mutable accumulation map; later additions modify that map.
    ///
    /// @throws IllegalArgumentException if capacity is negative or exceeds 2^29
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}HashMap${Generic}> newMapBuilder(int capacity) {
        return Mutable${Name}HashMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder(capacity);
    }

    /// Creates an empty mutable map.
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} create() {
        return new Mutable${Name}HashMap${Diamond}();
    }

    /// Creates an empty mutable map.
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} of() {
        return create();
    }

    /// Creates a mutable map containing the given entry.
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} of(int key, ${Value} value) {
        var result = new Mutable${Name}HashMap${Generic}();
        result.set(key, value);
        return result;
    }

    /// Creates a mutable map of the entries; the second value wins for equal keys.
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} of(int key1, ${Value} value1, int key2, ${Value} value2) {
        var result = new Mutable${Name}HashMap${Generic}();
        result.set(key1, value1);
        result.set(key2, value2);
        return result;
    }

<#list 3..5 as arity>
    /// Creates a mutable map containing the supplied entries; the last value wins for duplicate keys.
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
        Mutable${Name}HashMap${Generic} result = new Mutable${Name}HashMap${Diamond}();
<#list 1..arity as index>
        result.set(key${index}, value${index});
</#list>
        return result;
    }

</#list>
    /// Copies parallel arrays; later values replace earlier values for duplicate keys.
    /// Subsequent array changes do not affect the map.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        Objects.requireNonNull(keys);
        Objects.requireNonNull(values);
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Mismatched array lengths");
        }
        var result = new Mutable${Name}HashMap${Generic}(keys.length);
        for (int i = 0; i < keys.length; i++) {
            result.set(keys[i], values[i]);
        }
        return result;
    }

    /// Copies mappings into an independent map; object values themselves are not copied.
    ///
    /// @throws NullPointerException if the source is null
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        Objects.requireNonNull(source);
        var result = new Mutable${Name}HashMap${Generic}(Math.max(0, source.knownSize()));
        result.putAll(source);
        return result;
    }

    /// Copies a Java map into an independent map.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Mutable${Name}HashMap${Generic} from(java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        var result = new Mutable${Name}HashMap${Generic}(source.size());
        result.putAll(source);
        return result;
    }

    //endregion

    /// Mixes the key bits before applying the table mask.
    private static int hash(int key) {
        int hash = key * 0x9E3779B9;
        return hash ^ (hash >>> 16);
    }

    /// Returns the key's occupied slot, or minus one if absent.
    private int find(int key) {
        int mask = keys.length - 1;
        int index = hash(key) & mask;
        while (states[index] != 0) {
            if (states[index] == 1 && keys[index] == key) {
                return index;
            }
            index = (index + 1) & mask;
        }
        return -1;
    }

    /// Returns a slot value, casting only values previously stored through this map's API.
<#if !IsPrimitiveValue>
    @SuppressWarnings("unchecked")
</#if>
    private ${Value} valueAt(int index) {
        return <#if !IsPrimitiveValue>(V) </#if>values[index];
    }

    /// Inserts a key known to be absent into a table with spare capacity.
    private void insert(int key, ${Value} value) {
        int mask = keys.length - 1;
        int index = hash(key) & mask;
        while (states[index] == 1) {
            index = (index + 1) & mask;
        }
        if (states[index] == 0) {
            used++;
        }
        states[index] = 1;
        keys[index] = key;
        values[index] = value;
        size++;
    }

    /// Rebuilds the table, removing deleted slots; allocation failure leaves the table intact.
    private void rehash(int capacity) {
        int[] newKeys = new int[capacity];
        ${ArrayValue}[] newValues = new ${ArrayValue}[capacity];
        byte[] newStates = new byte[capacity];
        int mask = capacity - 1;
        for (int i = 0; i < states.length; i++) {
            if (states[i] != 1) {
                continue;
            }
            int index = hash(keys[i]) & mask;
            while (newStates[index] != 0) {
                index = (index + 1) & mask;
            }
            newStates[index] = 1;
            newKeys[index] = keys[i];
            newValues[index] = values[i];
        }
        keys = newKeys;
        values = newValues;
        states = newStates;
        used = size;
    }

    /// Ensures that an insertion cannot fill the table, compacting tombstones when appropriate.
    private void prepareInsert() {
        if (used < keys.length / 2) {
            return;
        }
        if (size < keys.length / 4) {
            rehash(keys.length);
        } else {
            if (keys.length == (1 << 30)) {
                throw new IllegalStateException("Map capacity exhausted");
            }
            rehash(keys.length << 1);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(int key) {
        return find(key) >= 0;
    }

    @Override
    public ${Value} get(int key) {
        int index = find(key);
        if (index < 0) {
            throw new NoSuchElementException("Missing key: " + key);
        }
        return valueAt(index);
    }

    @Override
    public ${Value} getOrDefault(int key, ${Value} defaultValue) {
        int index = find(key);
        return index < 0 ? defaultValue : valueAt(index);
    }

    @Override
    public @NotNull ${Option} getOption(int key) {
        int index = find(key);
        return index < 0 ? ${OptionClass}.none() : ${OptionClass}.some(valueAt(index));
    }

    /// @throws IllegalStateException if inserting a new key requires growth beyond the maximum table capacity
    @Override
    public @NotNull ${Option} put(int key, ${Value} value) {
        int index = find(key);
        if (index >= 0) {
            var old = ${OptionClass}.some(valueAt(index));
            values[index] = value;
            version++;
            return old;
        }
        prepareInsert();
        insert(key, value);
        version++;
        return ${OptionClass}.none();
    }

    /// @throws IllegalStateException if inserting a new key requires growth beyond the maximum table capacity
    @Override
    public void set(int key, ${Value} value) {
        int index = find(key);
        if (index >= 0) {
            values[index] = value;
        } else {
            prepareInsert();
            insert(key, value);
        }
        version++;
    }

    @Override
    public @NotNull ${Option} remove(int key) {
        int index = find(key);
        if (index < 0) {
            return ${OptionClass}.none();
        }
        var old = ${OptionClass}.some(valueAt(index));
        states[index] = 2;
        values[index] = ${DefaultValue};
        size--;
        version++;
        if (size == 0) {
            Arrays.fill(states, (byte) 0);
            used = 0;
        }
        return old;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }
        Arrays.fill(states, (byte) 0);
        Arrays.fill(values, ${DefaultValue});
        size = used = 0;
        version++;
    }

    /// Returns a copy with independent table storage; object values themselves are not copied.
    @Override
    public @NotNull Mutable${Name}HashMap${Generic} clone() {
        return from(this);
    }

    @Override
    public @NotNull String className() {
        return "Mutable${Name}HashMap";
    }

    @Override
    public @NotNull ${Name}MapIterator${Generic} iterator() {
        return new EntryIterator();
    }

    /// Traverses occupied slots and detects changes to the enclosing map.
    private final class EntryIterator implements ${Name}MapIterator${Generic} {
        /// Modification counter captured at construction.
        private final int expectedVersion = version;
        /// Current entry slot, or minus one before the first advance.
        private int current = -1;
        /// First slot that has not yet been returned.
        private int next;

        /// Creates an iterator over the current table.
        private EntryIterator() {
        }

        /// Rejects use after a modification to the map.
        private void checkVersion() {
            if (expectedVersion != version) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkVersion();
            while (next < states.length && states[next] != 1) {
                next++;
            }
            return next < states.length;
        }

        @Override
        public int nextIntKey() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = next++;
            return keys[current];
        }

        @Override
        public ${Value} ${ValueGetter}() {
            checkVersion();
            if (current < 0) {
                throw new IllegalStateException("No current entry");
            }
            return valueAt(current);
        }
    }

    /// Builds maps from mutable hash-table accumulators.
<#if !IsPrimitiveValue>
    ///
    /// @param <V> the value type
</#if>
    private static final class Factory${Generic}
            implements MapFactory<Integer, ${BoxedValue}, Mutable${Name}HashMap${Generic}, Mutable${Name}HashMap${Generic}> {

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
        public Mutable${Name}HashMap${Generic} build(Mutable${Name}HashMap${Generic} builder) {
            return builder;
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
