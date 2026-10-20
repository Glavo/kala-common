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
package kala.collection.base.primitive;

import kala.function.${ConsumerName};
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Objects;

/// Iterates over int keys and their associated values without boxing primitive components.
///
/// Calling [#nextIntKey()] selects an entry; its value remains available until
/// the next successful advance. Calling a value accessor before the first successful
/// advance throws [IllegalStateException]. Exhaustion does not discard the current entry.
/// Removal through the iterator is unsupported unless an implementation specifies otherwise.
<#if !IsPrimitiveValue>
///
/// @param <V> the value type; values may be null
</#if>
public interface ${Name}MapIterator${Generic} extends PrimitiveMapIterator<Integer, ${BoxedValue}> {
    /// Returns an iterator with no entries.
    static ${MethodGeneric}@NotNull ${Name}MapIterator${Generic} empty() {
        return new ${Name}MapIterator${Diamond}() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public int nextIntKey() {
                throw new NoSuchElementException();
            }

            @Override
            public ${Value} ${ValueGetter}() {
                throw new IllegalStateException("No current entry");
            }
        };
    }

    /// Advances to the next entry and returns its key.
    ///
    /// @throws NoSuchElementException if no entries remain
    int nextIntKey();

    /// Returns the value of the most recently selected entry without advancing.
    ///
    /// @throws IllegalStateException if no entry has been selected
<#if !IsPrimitiveValue>
    @Override
</#if>
    ${Value} ${ValueGetter}();

    /// Advances to the next entry and boxes its key.
    @Override
    default Integer nextKey() {
        return nextIntKey();
    }

<#if IsPrimitiveValue>
    /// Returns the current value, boxed as an Integer.
    @Override
    default Integer getValue() {
        return getIntValue();
    }

</#if>
    /// Consumes remaining entries without boxing primitive components,
    /// passing each key and value to the action.
    /// If the action throws, the entry passed to it has already been selected.
    ///
    /// @throws NullPointerException if the action is null
    default void forEachPrimitive(@NotNull ${Consumer} action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(nextIntKey(), ${ValueGetter}());
        }
    }

    /// Returns an iterator that consumes this iterator and yields its primitive keys.
    @Override
    default @NotNull IntIterator asKeysIterator() {
        return new IntIterator() {
            @Override
            public boolean hasNext() {
                return ${Name}MapIterator.this.hasNext();
            }

            @Override
            public int nextInt() {
                return nextIntKey();
            }
        };
    }

<#if IsPrimitiveValue>
    /// Returns an iterator that consumes this iterator and yields its primitive values.
    @Override
    default @NotNull IntIterator asValuesIterator() {
        return new IntIterator() {
            @Override
            public boolean hasNext() {
                return ${Name}MapIterator.this.hasNext();
            }

            @Override
            public int nextInt() {
                nextIntKey();
                return getIntValue();
            }
        };
    }

</#if>
    /// Consumes the remaining entries and returns the sum of their key/value hash XORs.
    @Override
    default int hash() {
        int hash = 0;
        while (hasNext()) {
            hash += nextIntKey() ^ <#if IsPrimitiveValue>getIntValue()<#else>Objects.hashCode(getValue())</#if>;
        }
        return hash;
    }
}
