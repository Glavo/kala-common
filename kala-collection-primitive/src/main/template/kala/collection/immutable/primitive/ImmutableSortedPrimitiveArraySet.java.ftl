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
package kala.collection.immutable.primitive;

import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.mutable.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

public final class ImmutableSorted${Type}ArraySet extends AbstractImmutable${Type}Set implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final ImmutableSorted${Type}ArraySet.Factory FACTORY =
            new ImmutableSorted${Type}ArraySet.Factory();
    private static final ImmutableSorted${Type}ArraySet EMPTY = new ImmutableSorted${Type}ArraySet(${Type}Arrays.EMPTY);

    final ${PrimitiveType}[] elements;

    ImmutableSorted${Type}ArraySet(${PrimitiveType}[] elements) {
        this.elements = elements;
    }

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, ImmutableSorted${Type}ArraySet> factory() {
        return FACTORY;
    }

    @Contract
    public static @NotNull ImmutableSorted${Type}ArraySet empty() {
        return EMPTY;
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of() {
        return empty();
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(${PrimitiveType} value1) {
        return new ImmutableSorted${Type}ArraySet(new ${PrimitiveType}[]{value1});
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        int c = ${WrapperType}.compare(value1, value2);
        if (c < 0) {
            return new ImmutableSorted${Type}ArraySet(new ${PrimitiveType}[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableSorted${Type}ArraySet(new ${PrimitiveType}[]{value2, value1});
        }
        return new ImmutableSorted${Type}ArraySet(new ${PrimitiveType}[]{value1});
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return new ImmutableSorted${Type}ArraySet(s.toArray());
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return new ImmutableSorted${Type}ArraySet(s.toArray());
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(
            ${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5
    ) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return new ImmutableSorted${Type}ArraySet(s.toArray());
    }

    public static @NotNull ImmutableSorted${Type}ArraySet of(${PrimitiveType}... values) {
        return from(values);
    }

    public static @NotNull ImmutableSorted${Type}ArraySet from(${PrimitiveType} @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty();
        }
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.addAll(values);
        return new ImmutableSorted${Type}ArraySet(s.toArray());
    }

    public static @NotNull ImmutableSorted${Type}ArraySet from(@NotNull ${Type}Traversable values) {
        return from(values.iterator());
    }

    public static @NotNull ImmutableSorted${Type}ArraySet from(@NotNull ${Type}Iterator it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }

        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        while (it.hasNext()) {
            s.add(it.next${Type}());
        }
        return new ImmutableSorted${Type}ArraySet(s.toArray());
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableSorted${Type}ArraySet";
    }

    @Override
    public @NotNull ${Type}CollectionFactory<?, ImmutableSorted${Type}ArraySet> iterableFactory() {
        return FACTORY;
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return ${Type}Arrays.iterator(elements);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public int knownSize() {
        return size();
    }

    @Override
    public @NotNull ImmutableSorted${Type}ArraySet added(${PrimitiveType} value) {
        final int size = elements.length;

        if (size == 0) {
            return new ImmutableSorted${Type}ArraySet(new ${PrimitiveType}[]{value});
        }

        int idx = Arrays.binarySearch(elements, value);
        if (idx >= 0) {
            return this;
        }
        idx = -idx - 1;

        ${PrimitiveType}[] newElements = new ${PrimitiveType}[size + 1];

        if (idx == 0) {
            System.arraycopy(elements, 0, newElements, 1, size);
            newElements[0] = value;
            return new ImmutableSorted${Type}ArraySet(newElements);
        } else if (idx == size) {
            System.arraycopy(elements, 0, newElements, 0, size);
            newElements[size] = value;
            return new ImmutableSorted${Type}ArraySet(newElements);
        } else {
            System.arraycopy(elements, 0, newElements, 0, idx);
            System.arraycopy(elements, idx, newElements, idx + 1, size - idx);
            newElements[idx] = value;
            return new ImmutableSorted${Type}ArraySet(newElements);
        }
    }

    @Override
    public @NotNull ImmutableSorted${Type}ArraySet addedAll(@NotNull ${Type}Traversable values) {
        final ${Type}Iterator it = values.iterator();
        if (!it.hasNext()) {
            return this;
        }

        final ${PrimitiveType}[] elements = this.elements;

        if (elements.length == 0) {
            return from(values);
        }

        Mutable${Type}TreeSet builder = new Mutable${Type}TreeSet();

        builder.addAll(elements);
        builder.addAll(values);
        if (builder.size() == elements.length) {
            return this;
        }

        return new ImmutableSorted${Type}ArraySet(builder.toArray());
    }

    @Override
    public @NotNull ImmutableSorted${Type}ArraySet addedAll(${PrimitiveType} @NotNull [] values) {
        final int arrayLength = values.length;
        if (arrayLength == 0) {
            return this;
        }
        if (arrayLength == 1) {
            return added(values[0]);
        }

        final int size = elements.length;

        if (size == 0) {
            return from(values);
        }

        Mutable${Type}TreeSet builder = new Mutable${Type}TreeSet();
        builder.addAll(elements);
        builder.addAll(values);
        return new ImmutableSorted${Type}ArraySet(builder.toArray());
    }

    // @Override
    public ${PrimitiveType} getFirst() {
        if (elements.length == 0) {
            throw new NoSuchElementException();
        }
        return elements[0];
    }

    // @Override
    public ${PrimitiveType} getLast() {
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[size - 1];
    }

    @Override
    public <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return ${Type}Arrays.joinTo(elements, buffer, separator, prefix, postfix);
    }

    @Override
    public void forEach(@NotNull ${Type}Consumer action) {
        for (${PrimitiveType} e : elements) {
            action.accept(e);
        }
    }

    private static final class Factory implements ${Type}CollectionFactory<Mutable${Type}TreeSet, ImmutableSorted${Type}ArraySet> {
        @Override
        public ImmutableSorted${Type}ArraySet empty() {
            return ImmutableSorted${Type}ArraySet.empty();
        }

        @Override
        public Mutable${Type}TreeSet newBuilder() {
            return new Mutable${Type}TreeSet();
        }

        @Override
        public ImmutableSorted${Type}ArraySet build(@NotNull Mutable${Type}TreeSet builder) {
            return builder.isEmpty() ? ImmutableSorted${Type}ArraySet.empty() : new ImmutableSorted${Type}ArraySet(builder.toArray());
        }

        @Override
        public void addToBuilder(@NotNull Mutable${Type}TreeSet builder, ${PrimitiveType} value) {
            builder.add(value);
        }

        @Override
        public Mutable${Type}TreeSet mergeBuilder(@NotNull Mutable${Type}TreeSet builder1, @NotNull Mutable${Type}TreeSet builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
