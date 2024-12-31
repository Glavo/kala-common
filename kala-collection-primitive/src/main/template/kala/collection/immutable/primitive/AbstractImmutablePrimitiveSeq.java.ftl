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

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.Abstract${Type}Seq;
import kala.collection.primitive.${Type}SeqLike;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutable${Type}Seq extends Abstract${Type}Seq implements Immutable${Type}Seq {
    static <T, Builder> T updated(
            @NotNull Immutable${Type}Seq seq,
            @Index int index, ${PrimitiveType} newValue,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        final int s = seq.size();
        index = Indexes.checkIndex(index, s);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, s);

        ${Type}Iterator it = seq.iterator();
        while (it.hasNext()) {
            ${PrimitiveType} e = it.next${Type}();

            if (index-- == 0) {
                factory.addToBuilder(builder, newValue);
            } else {
                factory.addToBuilder(builder, e);
            }
        }

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T slice(
            @NotNull Immutable${Type}Seq seq,
            @Index int beginIndex, @Index int endIndex,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        final int size = seq.size();
        beginIndex = Indexes.checkBeginIndex(beginIndex, size);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

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
            ${Type}Iterator it = seq.iterator().drop(beginIndex).take(ns);
            while (it.hasNext()) {
                factory.addToBuilder(builder, it.next${Type}());
            }
        }
        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T drop(
            @NotNull Immutable${Type}Seq seq,
            int n,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        Builder builder = factory.newBuilder();

        int s = seq.knownSize();
        if (s >= 0) {
            factory.sizeHint(builder, Integer.max(s - n, 0));
        }

        for (${Type}Iterator it = seq.iterator().drop(n); it.hasNext(); ) {
            ${PrimitiveType} e = it.next${Type}();
            factory.addToBuilder(builder, e);
        }
        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T dropLast(
            @NotNull Immutable${Type}Seq seq,
            int n,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
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
            ${Type}Iterator it = seq.iterator();
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, it.next${Type}());
            }
        }

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T dropWhile(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}Predicate predicate,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        factory.addAllToBuilder(builder, seq.iterator().dropWhile(predicate));

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T take(
            @NotNull Immutable${Type}Seq seq,
            int n,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
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

        ${Type}Iterator it = seq.iterator();
        while (it.hasNext()) {
            if (++count > n) break;

            factory.addToBuilder(builder, it.next${Type}());
        }

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T takeLast(
            @NotNull Immutable${Type}Seq seq,
            int n,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
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
            ${Type}Iterator it = seq.iterator();
            for (int i = 0; i < ss - n; i++) {
                it.next${Type}();
            }

            while (it.hasNext()) {
                factory.addToBuilder(builder, it.next${Type}());
            }
        }

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T takeWhile(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}Predicate predicate,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        seq.forEachBreakable(e -> {
            if (!predicate.test(e)) return false;

            factory.addToBuilder(builder, e);
            return true;
        });

        return factory.build(builder);
    }

    static <T extends Immutable${Type}Seq, Builder> T concat(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}SeqLike other,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, other);
        factory.addAllToBuilder(builder, other);

        return factory.build(builder);
    }

    static <T, Builder> T prepended(
            @NotNull Immutable${Type}Seq seq,
            ${PrimitiveType} element,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addToBuilder(builder, element);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <T, Builder> T prependedAll(
            @NotNull Immutable${Type}Seq seq,
            ${PrimitiveType} @NotNull [] values,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values.length);
        for (${PrimitiveType} e : values) {
            factory.addToBuilder(builder, e);
        }

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <T, Builder> T prependedAll(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}Traversable values,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <T, Builder> T appended(
            @NotNull Immutable${Type}Seq seq,
            ${PrimitiveType} element,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addAllToBuilder(builder, seq);
        factory.addToBuilder(builder, element);


        return factory.build(builder);
    }

    static <T, Builder> T appendedAll(
            @NotNull Immutable${Type}Seq seq,
            ${PrimitiveType} @NotNull [] values,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, values.length);
        factory.addAllToBuilder(builder, seq);

        for (${PrimitiveType} e : values) {
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <T, Builder> T appendedAll(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}Traversable values,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        return factory.build(builder);
    }

    static <T, Builder> T sorted(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        if (seq.isEmpty()) return factory.empty();

        ${PrimitiveType}[] arr = seq.toArray();
        ${Type}Arrays.sort(arr);

        return factory.from(arr);
    }

    static <T, Builder> T reversed(
            @NotNull Immutable${Type}Seq seq,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq.reverseIterator());
        return factory.build(builder);
    }
}
