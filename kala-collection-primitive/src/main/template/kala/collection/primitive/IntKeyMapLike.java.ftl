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

import kala.collection.base.primitive.${Name}MapIterator;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import kala.function.${ConsumerName};
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
<#if IsPrimitiveValue>
import java.util.function.IntSupplier;
<#else>
import java.util.function.Supplier;
</#if>

/// Provides read operations on mappings from int keys to <#if IsPrimitiveValue>int<#else>object</#if> values.
/// Each key occurs at most once. No iteration order is guaranteed.
<#if !IsPrimitiveValue>
/// Null values are permitted and are distinguished from absent keys by [#getOption(int)].
///
/// @param <V> the value type
</#if>
public interface ${Name}MapLike${Generic} extends PrimitiveMapLike<Integer, ${BoxedValue}> {
    /// Returns an iterator over all mappings; each key is yielded exactly once.
    @Override
    @NotNull ${Name}MapIterator${Generic} iterator();

    /// Returns the name used when formatting this map-like object.
    @Override
    default @NotNull String className() {
        return "${Name}MapLike";
    }

    /// Returns whether this map contains no mappings.
    @Override
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /// Returns the number of mappings.
    ///
    /// @implSpec The default implementation traverses the iterator.
    @Override
    default int size() {
        int size = 0;
        var it = iterator();
        while (it.hasNext()) {
            it.nextIntKey();
            size++;
        }
        return size;
    }

    /// Returns the number of mappings if cheaply available, or minus one otherwise.
    ///
    /// @implSpec The default implementation returns minus one.
    @Override
    default int knownSize() {
        return -1;
    }

    /// Returns the value associated with the key.
    ///
    /// @throws java.util.NoSuchElementException if the key is absent
    default ${Value} get(int key) {
        return getOption(key).get();
    }

    /// Returns the associated value, or an empty option if the key is absent.
<#if !IsPrimitiveValue>
    /// A present null value is represented by `Option.some(null)`.
</#if>
    default @NotNull ${Option} getOption(int key) {
        var it = iterator();
        while (it.hasNext()) {
            if (it.nextIntKey() == key) {
                return ${OptionClass}.some(it.${ValueGetter}());
            }
        }
        return ${OptionClass}.none();
    }

    /// Returns the associated value, or null if the key is absent.
    default @Nullable ${BoxedValue} getOrNull(int key) {
        return getOption(key).getOrNull();
    }

    /// Returns the associated value, or the default value if the key is absent.
    default ${Value} getOrDefault(int key, ${Value} defaultValue) {
        return getOption(key).getOrDefault(defaultValue);
    }

    /// Returns the associated value, invoking the supplier only if the key is absent.
    /// The supplied value is not inserted into this map.
    ///
    /// @throws NullPointerException if the supplier is null
    default ${Value} getOrElse(int key, @NotNull ${Supplier} supplier) {
        Objects.requireNonNull(supplier);
        return getOption(key).getOrElse(supplier);
    }

    /// Returns whether the key has an associated value.
    default boolean containsKey(int key) {
        return getOption(key).isDefined();
    }

    /// Returns whether the key is associated with the given value.
    default boolean contains(int key, <#if IsPrimitiveValue>int<#else>Object</#if> value) {
        var result = getOption(key);
        return result.isDefined() && <#if IsPrimitiveValue>result.get() == value<#else>Objects.equals(result.get(), value)</#if>;
    }

    /// Returns whether any key is associated with the given value.
    default boolean containsValue(<#if IsPrimitiveValue>int<#else>Object</#if> value) {
        var it = iterator();
        while (it.hasNext()) {
            it.nextIntKey();
            if (<#if IsPrimitiveValue>it.getIntValue() == value<#else>Objects.equals(it.getValue(), value)</#if>) {
                return true;
            }
        }
        return false;
    }

    /// Passes each key and value to the action without boxing primitive components.
    /// If the action throws, traversal stops and its exception is propagated.
    ///
    /// @throws NullPointerException if the action is null
    default void forEach(@NotNull ${Consumer} action) {
        iterator().forEachPrimitive(action);
    }

    /// Joins the entries as `key=value` pairs separated by a comma and a space.
    default @NotNull String joinToString() {
        return iterator().joinToString();
    }

    /// Joins the entries using the supplied separator, prefix, and postfix.
    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return iterator().joinToString(separator, prefix, postfix);
    }
}
