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

import kala.Conditions;
import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.collection.ArraySeq;
import kala.collection.Seq;
import kala.collection.base.Growable;
import kala.collection.base.Traversable;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.SeqIterators;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import kala.index.Index;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface MutableList<E> extends MutableSeq<E>, Growable<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableList<E>> factory() {
        return CollectionFactory.narrow(MutableArrayList.factory());
    }

    @Contract("-> new")
    static <E> @NotNull MutableList<E> create() {
        return new MutableArrayList<>();
    }

    @Contract("-> new")
    static <E> @NotNull MutableList<E> of() {
        return MutableArrayList.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> of(E value1) {
        return MutableArrayList.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2) {
        return MutableArrayList.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3) {
        return MutableArrayList.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3, E value4) {
        return MutableArrayList.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArrayList.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> from(E @NotNull [] values) {
        return MutableArrayList.from(values);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArrayList.from(values);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> from(@NotNull Iterator<? extends E> it) {
        return MutableArrayList.from(it);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> from(@NotNull Stream<? extends E> stream) {
        return MutableArrayList.from(stream);
    }

    static <E> @NotNull MutableList<E> fill(int n, E value) {
        return MutableArrayList.fill(n, value);
    }

    static <E> @NotNull MutableList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return MutableArrayList.fill(n, init);
    }

    static <E> @NotNull MutableList<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        return MutableArrayList.generateUntil(supplier, predicate);
    }

    static <E> @NotNull MutableList<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        return MutableArrayList.generateUntilNull(supplier);
    }

    static <E> @NotNull MutableList<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof AsJavaConvert.MutableListAsJava<?, ?>) {
            return ((AsJavaConvert.MutableListAsJava<E, MutableList<E>>) list).source;
        }
        return list instanceof RandomAccess
                ? new FromJavaConvert.MutableIndexedListFromJava<>(list)
                : new FromJavaConvert.MutableListFromJava<>(list);
    }

    static <E, C extends MutableList<E>> @NotNull MutableListEditor<E, C> edit(@NotNull C seq) {
        return new MutableListEditor<>(seq);
    }

    //endregion

    @Override
    default @NotNull
    String className() {
        return "MutableList";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableList<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull MutableListIterator<E> seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull MutableListIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultMutableListIterator<>(this, index);
    }

    @Override
    default @NotNull List<E> asJava() {
        return this.supportsFastRandomAccess()
                ? new AsJavaConvert.MutableIndexedListAsJava<>(this)
                : new AsJavaConvert.MutableListAsJava<>(this);
    }

    default @NotNull MutableStack<E> asMutableStack() {
        return this instanceof MutableStack ? (MutableStack<E>) this : new MutableListStackAdapter<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default @NotNull MutableList<E> clone() {
        return this.<E>iterableFactory().from(this);
    }

    @DelegateBy("appendAll(E[])")
    default void setAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        this.clear();
        this.appendAll(values);
    }

    @DelegateBy("appendAll(Iterable<E>)")
    default void setAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        if (this != values) {
            this.clear();
            this.appendAll(values);
        }
    }

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    @DelegateBy("appendAll(Iterable<E>)")
    default void appendAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        if (values.length != 0) { // implicit null check of values
            this.appendAll(ArraySeq.wrap(values));
        }
    }

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (E e : this.toImmutableSeq()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                this.append(e);
            }
        } else {
            for (E e : values) {
                this.append(e);
            }
        }
    }

    @Override
    @ReplaceWith("append(E)")
    default void plusAssign(E value) {
        append(value);
    }

    @Override
    @ReplaceWith("appendAll(E[])")
    default void plusAssign(E @NotNull [] values) {
        appendAll(values);
    }

    @Override
    @ReplaceWith("appendAll(Iterable<E>)")
    default void plusAssign(@NotNull Iterable<? extends E> values) {
        appendAll(values);
    }

    @Contract(mutates = "this")
    void prepend(E value);

    @Contract(mutates = "this")
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        this.prependAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            Iterator<?> iterator = ((Seq<?>) values).toImmutableArray().reverseIterator(); // avoid mutating under our own iterator
            while (iterator.hasNext()) {
                //noinspection ConstantConditions
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof Seq<?> seq) {
            Iterator<?> iterator = seq.reverseIterator();
            while (iterator.hasNext()) {
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof List<?> list && values instanceof RandomAccess) {
            int s = list.size();
            for (int i = s - 1; i >= 0; i--) {
                prepend((E) list.get(i));
            }
            return;
        }

        Object[] cv = CollectionHelper.asArray(values);

        for (int i = cv.length - 1; i >= 0; i--) {
            prepend((E) cv[i]);
        }
    }

    @Contract(mutates = "this")
    void insert(@Index int index, @Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        insertAll(index, ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Traversable<? extends E> values
    ) {
        insertAll(index, (Iterable<? extends E>) values);
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (isEmpty() && index != 0) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }

        Object[] valuesArray = CollectionHelper.asArray(values);
        for (Object e : valuesArray) {
            insert(index++, (E) e);
        }
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    E removeAt(int index);

    @Contract(mutates = "this")
    @DelegateBy("removeAt(int)")
    default boolean remove(Object value) {
        int idx = indexOf(value);
        if (idx >= 0) {
            removeAt(idx);
            return true;
        } else {
            return false;
        }
    }

    @Contract(mutates = "this")
    @DelegateBy("removeAt(int)")
    default void removeInRange(int beginIndex, int endIndex) {
        int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int rangeLength = endIndex - beginIndex;

        if (rangeLength == 0) {
            return;
        }

        if (rangeLength == size) {
            clear();
        } else {
            for (int i = 0; i < rangeLength; i++) {
                this.removeAt(beginIndex);
            }
        }
    }

    @Contract(mutates = "this")
    @DelegateBy("removeAt(int)")
    default E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(0);
    }

    @Contract(mutates = "this")
    @DelegateBy("removeFirst()")
    default @Nullable E removeFirstOrNull() {
        return isEmpty() ? null : removeFirst();
    }

    @Contract(mutates = "this")
    @DelegateBy("removeFirst()")
    default @NotNull Option<E> removeFirstOption() {
        return isEmpty() ? Option.none() : Option.some(removeFirst());
    }

    @Contract(mutates = "this")
    @DelegateBy("removeAt(int)")
    default E removeLast() {
        final int size = this.size();
        if (size == 0) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(size - 1);
    }

    @Contract(mutates = "this")
    @DelegateBy("removeLast()")
    default @Nullable E removeLastOrNull() {
        return isEmpty() ? null : removeLast();
    }

    @Contract(mutates = "this")
    @DelegateBy("removeLast()")
    default @NotNull Option<E> removeLastOption() {
        return isEmpty() ? Option.none() : Option.some(removeLast());
    }

    @Contract(mutates = "this")
    default boolean removeIf(@NotNull Predicate<? super E> predicate) {
        MutableListIterator<E> it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
            E value = it.next();
            if (predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Contract(mutates = "this")
    default boolean retainIf(@NotNull Predicate<? super E> predicate) {
        MutableListIterator<E> it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
            E value = it.next();
            if (!predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
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
        MutableListIterator<E> it = this.seqIterator();
        for (int i = 0; i < n; i++) {
            if (!it.hasNext()) {
                break;
            }
            it.next();
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
        MutableListIterator<E> it = this.seqIterator(n);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }
}
