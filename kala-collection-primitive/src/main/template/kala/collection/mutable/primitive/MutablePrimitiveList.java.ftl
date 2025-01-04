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
package kala.collection.mutable.primitive;

import kala.Conditions;
import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.*;
import kala.collection.primitive.internal.${Type}SeqIterators;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public interface Mutable${Type}List extends MutablePrimitiveList<${WrapperType}>, Mutable${Type}Seq, ${Type}Growable {
    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}List> factory() {
        return ${Type}CollectionFactory.narrow(Mutable${Type}ArrayList.factory());
    }

    @Contract("-> new")
    static @NotNull Mutable${Type}List create() {
        return Mutable${Type}ArrayList.create();
    }

    @Contract("-> new")
    static @NotNull Mutable${Type}List of() {
        return Mutable${Type}ArrayList.of();
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1) {
        return Mutable${Type}ArrayList.of(value1);
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Mutable${Type}ArrayList.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Mutable${Type}ArrayList.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Mutable${Type}ArrayList.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Mutable${Type}ArrayList.of(value1, value2, value3, value4, value5);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType}... values) {
        return Mutable${Type}ArrayList.of(values);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List from(${PrimitiveType} @NotNull [] values) {
        return Mutable${Type}ArrayList.from(values);
    }

    static @NotNull Mutable${Type}List from(@NotNull ${Type}Traversable values) {
        return Mutable${Type}ArrayList.from(values);
    }

    static @NotNull Mutable${Type}List from(@NotNull ${Type}Iterator it) {
        return Mutable${Type}ArrayList.from(it);
    }
<#if IsSpecialized>

    /*
    static @NotNull Mutable${Type}List from(@NotNull ${Type}Stream stream) {
        return Mutable${Type}ArrayList.from(stream);
    }
     */
</#if>

    //endregion

    @Override
    default @NotNull String className() {
        return "Mutable${Type}List";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}List> iterableFactory() {
        return Mutable${Type}List.factory();
    }

    @Override
    default @NotNull Mutable${Type}ListIterator seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull Mutable${Type}ListIterator seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new ${Type}SeqIterators.DefaultMutable${Type}ListIterator<>(this, index);
    }

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) ${PrimitiveType} value);

    @Contract(mutates = "this")
    default void appendAll(@Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        final int length = values.length;
        //noinspection StatementWithEmptyBody
        if (length == 0) {
        } else if (length == 1) {
            this.append(values[0]);
        } else {
            this.appendAll(${Type}ArraySeq.wrap(values));
        }
    }

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (${PrimitiveType} e : this.toArray()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                this.append(e);
            }
        } else {
            values.forEach(this::append);
        }
    }

    @Override
    @ReplaceWith("append(${PrimitiveType})")
    default void plusAssign(${PrimitiveType} value) {
        append(value);
    }

    @Override
    @ReplaceWith("appendAll(${PrimitiveType}[])")
    default void plusAssign(${PrimitiveType} @NotNull [] values) {
        appendAll(values);
    }

    @Override
    @ReplaceWith("appendAll(${Type}Traversable)")
    default void plusAssign(@NotNull ${Type}Traversable values) {
        appendAll(values);
    }

    @Contract(mutates = "this")
    void prepend(${PrimitiveType} value);

    @Contract(mutates = "this")
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        this.prependAll(${Type}ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            ${PrimitiveType}[] arr = values.toArray();
            for (int i = arr.length - 1; i >= 0; i--) {
                this.prepend(arr[i]);
            }
            return;
        }

        if (values instanceof ${Type}Seq && ((${Type}Seq) values).supportsFastRandomAccess()) {
            ${Type}Seq seq = (${Type}Seq) values;
            int s = seq.size();
            for (int i = s - 1; i >= 0; i--) {
                prepend(seq.get(i));
            }
            return;
        }

        ${PrimitiveType}[] cv = values.toArray();
        for (int i = cv.length - 1; i >= 0; i--) {
            prepend(cv[i]);
        }
    }

    @Contract(mutates = "this")
    void insert(@Index int index, @Flow(targetIsContainer = true) ${PrimitiveType} value);

    @Contract(mutates = "this")
    default void insertAll(
            @Index int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        insertAll(index, ${Type}ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void insertAll(
            @Index int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        index = Indexes.checkPositionIndex(index, size());

        ${PrimitiveType}[] valuesArray = values.toArray();
        for (${PrimitiveType} e : valuesArray) {
            insert(index++, e);
        }
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    ${PrimitiveType} removeAt(@Index int index);

    default void removeInRange(int beginIndex, int endIndex) {
        int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int rangeLength = endIndex - beginIndex;

        if (rangeLength == 0) {
            return;
        }

        if (rangeLength == size) {
            clear();
            return;
        }

        for (int i = 0; i < rangeLength; i++) {
            this.removeAt(beginIndex);
        }
    }

    @Contract(mutates = "this")
    default ${PrimitiveType} removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(0);
    }

    @Contract(mutates = "this")
    default @Nullable ${WrapperType} removeFirstOrNull() {
        return isEmpty() ? null : removeAt(0);
    }

    @Contract(mutates = "this")
    default @NotNull ${Type}Option removeFirstOption() {
        return isEmpty() ? ${Type}Option.none() : ${Type}Option.some(removeAt(0));
    }

    @Contract(mutates = "this")
    default ${PrimitiveType} removeLast() {
        final int size = this.size();
        if (size == 0) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @Nullable ${PrimitiveType} removeLastOrNull() {
        final int size = this.size();
        return size == 0 ? null : removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @NotNull ${Type}Option removeLastOption() {
        final int size = this.size();
        return size == 0 ? ${Type}Option.none() : ${Type}Option.some(removeAt(size - 1));
    }

    @Contract(mutates = "this")
    default boolean removeIf(@NotNull ${Type}Predicate predicate) {
        Mutable${Type}ListIterator it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
            ${PrimitiveType} value = it.next${Type}();
            if (predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Deprecated
    @ReplaceWith("removeIf(${Type}Predicate)")
    default boolean removeAll(@NotNull ${Type}Predicate predicate) {
        return removeIf(predicate);
    }

    @Contract(mutates = "this")
    default boolean retainIf(@NotNull ${Type}Predicate predicate) {
        Mutable${Type}ListIterator it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
        ${PrimitiveType} value = it.next${Type}();
            if (!predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Contract(mutates = "this")
    @Deprecated
    @ReplaceWith("retainIf<${Type}Predicate>")
    default boolean retainAll(@NotNull ${Type}Predicate predicate) {
        return retainIf(predicate);
    }

    @Contract(mutates = "this")
    void clear();

    // ---

    @Contract(mutates = "this")
    default void dropInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return;
        }
        Mutable${Type}ListIterator it = this.seqIterator();
        for (int i = 0; i < n; i++) {
            if (!it.hasNext()) {
                break;
            }
            it.next${Type}();
            it.remove();
        }
    }

    @Contract(mutates = "this")
    default void takeInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        if (n == 0) {
            clear();
            return;
        }

        final int knownSize = this.knownSize();
        if (knownSize >= 0 && n >= knownSize) {
            return;
        }
        Mutable${Type}ListIterator it = this.seqIterator(n);
        while (it.hasNext()) {
            it.next${Type}();
            it.remove();
        }
    }

    @Contract(mutates = "this")
    default void filterInPlace(@NotNull ${Type}Predicate predicate) {
        retainAll(predicate);
    }

    @Contract(mutates = "this")
    default void filterNotInPlace(@NotNull ${Type}Predicate predicate) {
        removeAll(predicate);
    }
}
