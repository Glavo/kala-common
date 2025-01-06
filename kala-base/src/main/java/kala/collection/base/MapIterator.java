/*
 * Copyright 2024 Glavo
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
package kala.collection.base;

import kala.function.CheckedBiConsumer;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@SuppressWarnings("unchecked")
public interface MapIterator<K, V> extends Iterator<Tuple2<K, V>> {

    static <K, V> @NotNull MapIterator<K, V> empty() {
        return (MapIterator<K, V>) MapIterators.EMPTY;
    }

    static <K, V> @NotNull MapIterator<K, V> ofIterator(@NotNull Iterator<? extends java.util.Map.Entry<? extends K, ? extends V>> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        if (it instanceof MapIterator) {
            return (MapIterator<K, V>) it;
        }

        return new MapIterators.OfIterator<>(it);
    }

    boolean hasNext();

    K nextKey();

    V getValue();

    @Override
    default Tuple2<K, V> next() {
        return Tuple.of(nextKey(), getValue());
    }

    default boolean containsKey(K key) {
        if (key == null) {
            while (hasNext()) {
                if (null == nextKey()) {
                    return true;
                }
            }
        } else {
            while (hasNext()) {
                if (key.equals(nextKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean containsValue(Object value) {
        if (value == null) {
            while (hasNext()) {
                nextKey();
                if (null == getValue()) {
                    return true;
                }
            }
        } else {
            while (hasNext()) {
                nextKey();
                if (value.equals(getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (predicate.test(nextKey(), getValue())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (!predicate.test(nextKey(), getValue())) {
                return false;
            }
        }
        return true;
    }

    default boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (predicate.test(nextKey(), getValue())) {
                return false;
            }
        }
        return true;
    }

    default <U> @NotNull Iterator<U> map(@NotNull BiFunction<? super K, ? super V, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new MapIterators.Mapped<>(this, mapper);
    }

    default <NV> @NotNull MapIterator<K, NV> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends NV> mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return MapIterator.empty();
        }
        return new MapIterators.MapValues<>(this, mapper);
    }

    default int hash() {
        int hash = 0;
        while (hasNext()) {
            hash += Objects.hashCode(nextKey()) ^ Objects.hashCode(getValue());
        }
        return hash;
    }

    default @NotNull Iterator<K> asKeysIterator() {
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new MapIterators.KeysIterator<>(this);
    }

    default @NotNull Iterator<V> asValuesIterator() {
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new MapIterators.ValuesIterator<>(this);
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        while (hasNext()) {
            action.accept(nextKey(), getValue());
        }
    }

    default <Ex extends Throwable> void forEachChecked(
            @NotNull CheckedBiConsumer<? super K, ? super V, ? extends Ex> action) throws Ex {
        forEach(action);
    }

    default void forEachUnchecked(@NotNull CheckedBiConsumer<? super K, ? super V, ?> action) {
        forEach(action);
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        try {
            buffer.append(prefix);
            if (hasNext()) {
                buffer.append(Objects.toString(nextKey())).append("=").append(Objects.toString(getValue()));
            }
            while (hasNext()) {
                buffer.append(separator).append(Objects.toString(nextKey())).append("=").append(Objects.toString(getValue()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(buffer, ", ", transform);
    }

    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer, CharSequence separator,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(buffer, separator, "", "", transform);
    }

    @Contract(value = "_, _, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform
    ) {
        try {
            buffer.append(prefix);
            if (hasNext()) {
                buffer.append(transform.apply(nextKey(), getValue()));
            }
            while (hasNext()) {
                buffer.append(separator).append(transform.apply(nextKey(), getValue()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    default @NotNull String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    default @NotNull String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }

    default @NotNull String joinToString(@NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), transform).toString();
    }

    default @NotNull String joinToString(CharSequence separator, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, transform).toString();
    }

    default @NotNull String joinToString(
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, prefix, postfix, transform).toString();
    }
}
