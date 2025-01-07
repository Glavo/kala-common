/*
 * Copyright 2025 Glavo
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
package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public final class MutableEnumSet<E extends Enum<E>> extends AbstractMutableSet<E> {

    @Contract("_ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> create(@NotNull Class<E> elementType) {
        return noneOf(elementType);
    }

    @Contract("_ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> noneOf(@NotNull Class<E> elementType) {
        return new MutableEnumSet<>(EnumSet.noneOf(elementType));
    }

    @Contract("_ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> allOf(@NotNull Class<E> elementType) {
        return new MutableEnumSet<>(EnumSet.allOf(elementType));
    }

    @Contract("_ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(@NotNull E value1) {
        return new MutableEnumSet<>(EnumSet.of(value1));
    }

    @Contract("_, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2
    ) {
        return new MutableEnumSet<>(EnumSet.of(value1, value2));
    }

    @Contract("_, _, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3
    ) {
        return new MutableEnumSet<>(EnumSet.of(value1, value2, value3));
    }

    @Contract("_, _, _, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4
    ) {
        return new MutableEnumSet<>(EnumSet.of(value1, value2, value3, value4));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4, @NotNull E value5
    ) {
        return new MutableEnumSet<>(EnumSet.of(value1, value2, value3, value4, value5));
    }

    @Contract("_ -> new")
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(@NotNull E... values) {
        if (values.length == 0) {
            Class<?> componentType = values.getClass().getComponentType();
            if (componentType.isEnum()) {
                return new MutableEnumSet<>(EnumSet.noneOf((Class<E>) componentType));
            } else {
                throw new IllegalArgumentException(values.getClass() + " is not enum array type");
            }
        }

        EnumSet<E> set = EnumSet.of(values[0]);
        for (int i = 1; i < values.length; i++) {
            set.add(values[i]);
        }
        return new MutableEnumSet<>(set);
    }

    @Contract("_, _ -> new")
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> of(@NotNull Class<E> elementType, @NotNull E... values) {
        return from(elementType, values);
    }

    @Contract("_, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> from(@NotNull Class<E> elementType, @NotNull E @NotNull [] values) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        Collections.addAll(set, values);
        return new MutableEnumSet<>(set);
    }

    @Contract("_, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> from(@NotNull Class<E> elementType, @NotNull Iterable<? extends E> values) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        for (E value : values) {
            set.add(value);
        }
        return new MutableEnumSet<>(set);
    }

    @Contract("_, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> from(@NotNull Class<E> elementType, @NotNull Iterator<? extends E> it) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        while (it.hasNext()) {
            set.add(it.next());
        }
        return new MutableEnumSet<>(set);
    }

    @Contract("_, _ -> new")
    public static <E extends Enum<E>> @NotNull MutableEnumSet<@NotNull E> from(@NotNull Class<E> elementType, @NotNull Stream<? extends E> stream) {
        Objects.requireNonNull(elementType);
        return new MutableEnumSet<>(stream.collect(() -> EnumSet.noneOf(elementType), EnumSet::add, EnumSet::addAll));
    }

    private final EnumSet<E> enumSet;

    private MutableEnumSet(EnumSet<E> enumSet) {
        this.enumSet = enumSet;
    }

    @Override
    public @NotNull String className() {
        return "MutableEnumSet";
    }

    @Override
    public boolean isEmpty() {
        return enumSet.isEmpty();
    }

    @Override
    public int size() {
        return enumSet.size();
    }

    @Override
    public int knownSize() {
        return size();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return enumSet.iterator();
    }

    @Override
    public boolean add(E value) {
        return enumSet.add(value);
    }

    @Override
    public boolean remove(Object value) {
        return enumSet.remove(value);
    }

    @Override
    public void clear() {
        enumSet.clear();
    }
}
