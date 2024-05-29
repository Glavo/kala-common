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
package kala.collection.immutable;

import kala.collection.AbstractSeq;
import kala.collection.base.Iterators;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedFunction;
import kala.annotations.Covariant;
import kala.Conditions;
import kala.collection.SeqLike;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractImmutableSeq<@Covariant E> extends AbstractSeq<E> implements ImmutableSeq<E> {

    static <E, T, Builder> T updated(
            @NotNull ImmutableSeq<? extends E> seq,
            int index,
            E newValue,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int size = seq.size();

        Conditions.checkElementIndex(index, size);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, size);

        for (E e : seq) {
            if (index-- == 0) {
                factory.addToBuilder(builder, newValue);
            } else {
                factory.addToBuilder(builder, e);
            }
        }

        return factory.build(builder);
    }

    static <E, T, Builder> T inserted(
            @NotNull ImmutableSeq<? extends E> seq,
            int index,
            E newValue,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int size = seq.size();
        Conditions.checkPositionIndex(index, size);

        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, size - 1);

        Iterator<? extends E> iterator = seq.iterator();

        for (int i = 0; i < index; i++) {
            factory.addToBuilder(builder, iterator.next());
        }
        factory.addToBuilder(builder, newValue);
        factory.addAllToBuilder(builder, iterator);
        return factory.build(builder);
    }

    static <E, T, Builder> T removedAt(
            @NotNull ImmutableSeq<? extends E> seq,
            int index,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int size = seq.size();
        Conditions.checkElementIndex(index, size);

        if (size == 1) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, size - 1);

        Iterator<? extends E> iterator = seq.iterator();

        for (int i = 0; i < index; i++) {
            factory.addToBuilder(builder, iterator.next());
        }

        iterator.next();
        factory.addAllToBuilder(builder, iterator);
        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T slice(
            @NotNull ImmutableSeq<? extends E> seq,
            int beginIndex, int endIndex,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int size = seq.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int ns = endIndex - beginIndex;
        if (ns == 0) {
            return factory.empty();
        }
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, ns);

        if (seq.supportsFastRandomAccess()) {
            for (int i = beginIndex; i < endIndex; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = seq.iterator(beginIndex);
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, it.next());
            }
        }
        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T drop(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        Builder builder = factory.newBuilder();

        int s = seq.knownSize();
        if (s >= 0) {
            factory.sizeHint(builder, Integer.max(s - n, 0));
        }

        for (Iterator<? extends E> it = Iterators.drop(seq.iterator(), n); it.hasNext(); ) {
            E e = it.next();
            factory.addToBuilder(builder, e);
        }
        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T dropLast(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return (T) seq;
        }

        final int ss = seq.size();

        if (n >= ss) {
            return factory.empty();
        }
        final int ns = ss - n;
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, ns);

        if (seq.supportsFastRandomAccess()) {
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = seq.iterator();
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, it.next());
            }
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T dropWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        factory.addAllToBuilder(builder, Iterators.dropWhile(seq.iterator(), predicate));

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T take(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();

        int s = seq.knownSize();
        if (s != -1) {
            factory.sizeHint(builder, Integer.min(s, n));
        }

        int count = 0;
        for (E e : seq) {
            if (++count > n) {
                break;
            }
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T takeLast(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return factory.empty();
        }
        final int ss = seq.size();
        if (n >= ss) {
            return (T) seq;
        }

        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, n);

        if (seq.supportsFastRandomAccess()) {
            for (int i = ss - n; i < ss; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = seq.iterator();
            for (int i = 0; i < ss - n; i++) {
                it.next();
            }

            while (it.hasNext()) {
                factory.addToBuilder(builder, it.next());
            }
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T takeWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        for (E e : seq) {
            if (!predicate.test(e)) {
                break;
            }
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T concat(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull SeqLike<? extends E> other,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, other);
        factory.addAllToBuilder(builder, other);

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T concat(
            @NotNull ImmutableSeq<? extends E> seq,
            java.util.@NotNull List<? extends E> other,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, other);
        factory.addAllToBuilder(builder, other);

        return factory.build(builder);
    }

    static <E, T, Builder> T prepended(
            @NotNull ImmutableSeq<? extends E> seq,
            E element,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addToBuilder(builder, element);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values.length);
        for (E e : values) {
            factory.addToBuilder(builder, e);
        }

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T appended(
            @NotNull ImmutableSeq<? extends E> seq,
            E element,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addAllToBuilder(builder, seq);
        factory.addToBuilder(builder, element);


        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, values.length);
        factory.addAllToBuilder(builder, seq);

        for (E e : values) {
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        return factory.build(builder);
    }

    static <E, T, Builder> T sorted(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Comparator<? super E> comparator,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Object[] arr = seq.toArray();
        Arrays.sort(arr, ((Comparator<? super Object>) comparator));

        return factory.from((E[]) arr);
    }

    static <E, T, Builder> T reversed(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq.reverseIterator());
        return factory.build(builder);
    }

    static <E, U, T, Builder> T mapIndexed(
            @NotNull ImmutableSeq<? extends E> Seq,
            @NotNull IndexedFunction<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, Seq);

        int idx = 0;
        for (E e : Seq) {
            factory.addToBuilder(builder, mapper.apply(idx++, e));
        }
        return factory.build(builder);
    }


    static <E, U, T, Builder> T mapIndexedNotNull(
            @NotNull ImmutableSeq<? extends E> Seq,
            @NotNull IndexedFunction<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, Seq);

        int idx = 0;
        for (E e : Seq) {
            U u = mapper.apply(idx++, e);
            if (u != null) {
                factory.addToBuilder(builder, u
                );
            }
        }
        return factory.build(builder);
    }

    static <E, U, T, Builder> T mapIndexedMulti(
            @NotNull ImmutableSeq<? extends E> Seq,
            @NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();
        Consumer<U> consumer = u -> factory.addToBuilder(builder, u);

        int idx = 0;
        for (E e : Seq) {
            mapper.accept(idx++, e, consumer);
        }
        return factory.build(builder);
    }
}
