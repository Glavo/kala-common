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

import kala.control.Try;
import kala.function.*;
import kala.internal.InternalIdentifyObject;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.annotations.StaticClass;
import kala.control.Option;
import kala.collection.factory.CollectionFactory;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

@StaticClass
@SuppressWarnings("unchecked")
public final class Iterators {
    private Iterators() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <E> Iterator<E> narrow(Iterator<? extends E> iterator) {
        return (Iterator<E>) iterator;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull Iterator<E> empty() {
        return ((Iterator<E>) EMPTY);
    }

    public static <E> @NotNull Iterator<E> of() {
        return empty();
    }

    public static <E> @NotNull Iterator<E> of(E value1) {
        return value1 == null ? new Iterators.OfNull<>() : new Iterators.OfNotNull<>(value1);
    }

    public static <E> @NotNull Iterator<E> of(E value1, E value2) {
        return new Itr2<>(value1, value2);
    }

    public static <E> @NotNull Iterator<E> of(E value1, E value2, E value3) {
        return new Itr3<>(value1, value2, value3);
    }

    public static <E> @NotNull Iterator<E> of(E value1, E value2, E value3, E value4) {
        return new Itr4<>(value1, value2, value3, value4);
    }

    public static <E> @NotNull Iterator<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new Itr5<>(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    public static <E> @NotNull Iterator<E> of(E... values) {
        return GenericArrays.iterator(values);
    }

    public static <E> @NotNull Iterator<E> ofEnumeration(@NotNull java.util.Enumeration<? extends E> enumeration) {
        if (!enumeration.hasMoreElements()) { // implicit null check of enumeration
            return Iterators.empty();
        }

        return new AbstractIterator<E>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public E next() {
                return enumeration.nextElement();
            }

            @Override
            public String toString() {
                return "Iterators.OfEnumeration[" + enumeration + "]";
            }
        };
    }

    public static <E> @NotNull Iterator<E> from(E @NotNull [] values) {
        return GenericArrays.iterator(values);
    }

    public static <E> @NotNull Iterator<E> from(@NotNull Iterable<? extends E> values) {
        return narrow(values.iterator());
    }

    public static <E> @NotNull Iterator<E> from(@NotNull Iterator<? extends E> it) {
        return narrow(Objects.requireNonNull(it));
    }

    public static <E> @NotNull Iterator<E> from(@NotNull Stream<? extends E> stream) {
        return narrow(stream.iterator());
    }

    public static <E> @NotNull Iterator<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        if (n == 1) {
            return of(value);
        }
        return new Copies<>(n, value);
    }

    public static <E> @NotNull Iterator<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        if (n == 1) {
            return of(init.apply(0));
        }
        Objects.requireNonNull(init);
        return new FillIntFunction<>(n, init);
    }

    public static <E> @NotNull Iterator<E> concat(@NotNull Iterator<? extends E> it1, @NotNull Iterator<? extends E> it2) {
        if (!it1.hasNext()) { //implicit null check of it1
            return (Iterator<E>) Objects.requireNonNull(it2);
        }
        if (!it2.hasNext()) {//implicit null check of it1
            return (Iterator<E>) it1;
        }
        return new Concat<>(it1, it2);
    }

    public static <E> @NotNull Iterator<E> concat(@NotNull Iterator<? extends E>... its) {
        return switch (its.length) { // implicit null check of its
            case 0 -> Iterators.empty();
            case 1 -> Objects.requireNonNull((Iterator<E>) its[0]);
            case 2 -> concat(its[0], its[1]);
            default -> new ConcatAll<>(GenericArrays.iterator(its));
        };
    }

    public static <E> @NotNull Iterator<E> concat(@NotNull Iterable<? extends Iterator<? extends E>> its) {
        return concat(its.iterator()); // implicit null check of its
    }

    public static <E> @NotNull Iterator<E> concat(@NotNull Iterator<? extends Iterator<? extends E>> its) {
        if (!its.hasNext()) { // implicit null check of its
            return Iterators.empty();
        }
        return new ConcatAll<>(its);
    }

    public static <E> @NotNull Iterator<E> freeze(@NotNull Iterator<E> it) {
        return new Frozen<>(it);
    }

    @Contract(mutates = "param1")
    public static int hash(@NotNull Iterator<?> it) {
        int res = 0;
        while (it.hasNext()) { // implicit null check of it
            res = res * 31 + Objects.hashCode(it.next());
        }
        return res;
    }

    public static int size(@NotNull Iterator<?> it) {
        int i = 0;
        while (it.hasNext()) { // implicit null check of it
            it.next();
            ++i;
        }
        return i;
    }

    public static <E> Iterator<E> reversed(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return (Iterator<E>) it;
        }
        ArrayList<E> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return (Iterator<E>) GenericArrays.reverseIterator(list.toArray());
    }

    public static boolean contains(Iterator<?> it, Object value) {
        if (value == null) {
            while (it.hasNext()) { // implicit null check of it
                if (null == it.next()) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) { // implicit null check of it
                if (value.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean sameElements(@NotNull Iterator<?> it1, @NotNull Iterator<?> it2) {
        while (it1.hasNext() && it2.hasNext()) { // implicit null check of it1 and it2
            if (!Objects.equals(it1.next(), it2.next())) {
                return false;
            }
        }
        return it1.hasNext() == it2.hasNext();
    }

    public static boolean sameElements(@NotNull Iterator<?> it1, @NotNull Iterator<?> it2, boolean identity) {
        if (!identity) {
            return sameElements(it1, it2);
        }

        while (it1.hasNext() && it2.hasNext()) {
            if (it1.next() != it2.next()) {
                return false;
            }
        }
        return it1.hasNext() == it2.hasNext();
    }

    public static <E> int count(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        int c = 0;
        while (it.hasNext()) { // implicit null check of it
            if (predicate.test(it.next())) { // implicit null check of predicate
                ++c;
            }
        }
        return c;
    }

    public static <E> boolean anyMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean allMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (!predicate.test(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean noneMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <E, T> boolean anyMatchWith(@NotNull Iterator<? extends E> it, @NotNull Iterator<? extends T> other, @NotNull BiPredicate<? super E, ? super T> predicate) {
        while (it.hasNext() && other.hasNext()) {
            if (predicate.test(it.next(), other.next())) {
                return true;
            }
        }
        return false;
    }

    public static <E, T> boolean allMatchWith(@NotNull Iterator<? extends E> it, @NotNull Iterator<? extends T> other, @NotNull BiPredicate<? super E, ? super T> predicate) {
        while (it.hasNext() && other.hasNext()) {
            if (!predicate.test(it.next(), other.next())) {
                return false;
            }
        }
        return true;
    }

    public static <E, T> boolean noneMatchWith(@NotNull Iterator<? extends E> it, @NotNull Iterator<? extends T> other, @NotNull BiPredicate<? super E, ? super T> predicate) {
        while (it.hasNext() && other.hasNext()) {
            if (predicate.test(it.next(), other.next())) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public static <E> E first(@NotNull Iterator<? extends E> it) {
        return it.next();
    }

    @Deprecated
    public static <E> E first(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) { // implicit null check of it
            E e = it.next();
            if (predicate.test(e)) { // implicit null check of predicate
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    @Deprecated
    public static <E> @Nullable E firstOrNull(@NotNull Iterator<? extends E> it) {
        return it.hasNext() ? it.next() : null;
    }

    @Deprecated
    public static <E> @Nullable E firstOrNull(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    @Deprecated
    public static <E> @NotNull Option<E> firstOption(@NotNull Iterator<? extends E> it) {
        return it.hasNext() ? Option.some(it.next()) : Option.none();
    }

    @Deprecated
    public static <E> @NotNull Option<E> firstOption(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    @Deprecated
    public static <E> E last(@NotNull Iterator<? extends E> it) {
        E res = it.next();
        while (it.hasNext()) {
            res = it.next();
        }
        return res;
    }

    @Deprecated
    public static <E> E last(@NotNull Iterator<? extends E> it, Predicate<? super E> predicate) {
        E res = null;
        boolean hasValue = false;

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                hasValue = true;
                res = e;
            }
        }

        if (!hasValue) {
            throw new NoSuchElementException();
        }
        return res;
    }

    @Deprecated
    public static <E> @Nullable E lastOrNull(@NotNull Iterator<? extends E> it) {
        E res = null;
        while (it.hasNext()) {
            res = it.next();
        }
        return res;
    }

    @Deprecated
    public static <E> @Nullable E lastOrNull(@NotNull Iterator<? extends E> it, Predicate<? super E> predicate) {
        E res = null;

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                res = e;
            }
        }

        return res;
    }

    @Deprecated
    public static <E> @NotNull Option<E> lastOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E res = null;
        while (it.hasNext()) {
            res = it.next();
        }
        return Option.some(res);
    }

    @Deprecated
    public static <E> @NotNull Option<E> lastOption(@NotNull Iterator<? extends E> it, Predicate<? super E> predicate) {
        E res = null;
        boolean hasValue = false;

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                hasValue = true;
                res = e;
            }
        }

        return hasValue ? Option.some(res) : Option.none();
    }

    public static <E> E getFirst(@NotNull Iterator<? extends E> it) {
        return it.next();
    }

    public static <E> @Nullable E getFirstOrNull(@NotNull Iterator<? extends E> it) {
        return it.hasNext() ? it.next() : null;
    }

    public static <E> @NotNull Option<E> getFirstOption(@NotNull Iterator<? extends E> it) {
        return it.hasNext() ? Option.some(it.next()) : Option.none();
    }

    public static <E> E getLast(@NotNull Iterator<? extends E> it) {
        E res = it.next();
        while (it.hasNext()) {
            res = it.next();
        }
        return res;
    }

    public static <E> @Nullable E getLastOrNull(@NotNull Iterator<? extends E> it) {
        E res = null;
        while (it.hasNext()) {
            res = it.next();
        }
        return res;
    }

    public static <E> @NotNull Option<E> getLastOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E res = null;
        while (it.hasNext()) {
            res = it.next();
        }
        return Option.some(res);
    }

    public static <E extends Comparable<E>> E max(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> E max(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) > 0) {
                    e = v;
                }
            }
        } else {
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) > 0) {
                    e = v;
                }
            }
        }
        return e;
    }

    public static <E extends Comparable<E>> @Nullable E maxOrNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> @Nullable E maxOrNull(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) > 0) {
                    e = v;
                }
            }
        } else {
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) > 0) {
                    e = v;
                }
            }
        }
        return e;
    }

    public static <E extends Comparable<E>> @NotNull Option<E> maxOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    public static <E> @NotNull Option<E> maxOption(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) > 0) {
                    e = v;
                }
            }
        } else {
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) > 0) {
                    e = v;
                }
            }
        }
        return Option.some(e);
    }

    public static <E extends Comparable<E>> E min(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> E min(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) < 0) {
                    e = v;
                }
            }
        } else
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) < 0) {
                    e = v;
                }
            }
        return e;
    }

    public static <E extends Comparable<E>> @Nullable E minOrNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> @Nullable E minOrNull(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) < 0) {
                    e = v;
                }
            }
        } else {
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) < 0) {
                    e = v;
                }
            }
        }
        return e;
    }

    public static <E extends Comparable<E>> @NotNull Option<E> minOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    public static <E> @NotNull Option<E> minOption(@NotNull Iterator<? extends E> it, Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        if (comparator == null) {
            while (it.hasNext()) {
                E v = it.next();
                if (((Comparable<E>) v).compareTo(e) < 0) {
                    e = v;
                }
            }
        } else {
            while (it.hasNext()) {
                E v = it.next();
                if (comparator.compare(v, e) < 0) {
                    e = v;
                }
            }
        }
        return Option.some(e);
    }

    @Contract(mutates = "param1")
    public static <E> @NotNull Iterator<E> drop(@NotNull Iterator<? extends E> it, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        while (n > 0 && it.hasNext()) { // implicit null check of it
            it.next();
            --n;
        }
        return ((Iterator<E>) it);
    }

    public static <E> @NotNull Iterator<E> dropWhile(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        if (!it.hasNext()) { // implicit null check of it
            return Iterators.empty();
        }

        while (it.hasNext()) {
            E value;
            if (!predicate.test(value = it.next())) {
                return it.hasNext() ? prepended(it, value) : Iterators.of(value);
            }
        }

        return Iterators.empty();
    }

    public static <E> @NotNull Iterator<E> take(@NotNull Iterator<? extends E> it, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0 || !it.hasNext()) { // implicit null check of it
            return empty();
        }

        return new Take<>(it, n);
    }

    public static <E> @NotNull Iterator<E> takeWhile(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new TakeWhile<>(it, predicate);
    }

    public static <E> @NotNull Iterator<E> updated(@NotNull Iterator<? extends E> it, int n, E newValue) {
        if (!it.hasNext() || n < 0) {
            return (Iterator<E>) it;
        }

        if (n == 0) {
            it.next();
            return prepended(it, newValue);
        }

        return new AbstractIterator<E>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                if (idx++ == n) {
                    it.next();
                    return newValue;
                } else {
                    return it.next();
                }
            }
        };
    }

    public static <E> @NotNull Iterator<E> appended(@NotNull Iterator<? extends E> it, E value) {
        if (!it.hasNext()) { // implicit null check of it
            return Iterators.of(value);
        }
        return value == null ? new AppendedNull<>(it) : new AppendedNotNull<>(it, value);
    }

    public static <E> @NotNull Iterator<E> prepended(@NotNull Iterator<? extends E> it, E value) {
        if (!it.hasNext()) { // implicit null check of it
            return of(value);
        }
        return value == null ? new PrependedNull<>(it) : new PrependedNotNull<>(it, value);
    }

    public static <E> @NotNull Iterator<E> inserted(@NotNull Iterator<? extends E> it, int index, E value) {
        return new AbstractIterator<>() {
            private int i;

            @Override
            public boolean hasNext() {
                return i == index || it.hasNext();
            }

            @Override
            public E next() {
                checkStatus();

                if (i++ == index) {
                    return value;
                } else {
                    return it.next();
                }
            }
        };
    }

    public static <E> @NotNull Iterator<E> removed(@NotNull Iterator<? extends E> it, int index) {
        return new AbstractIterator<>() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                if (count == index) {
                    try {
                        it.next();
                        count++;
                    } catch (NoSuchElementException ignored) {
                        return false;
                    }
                }

                return it.hasNext();
            }

            @Override
            public E next() {
                checkStatus();
                E res = it.next();
                count++;
                return res;
            }
        };
    }

    public static <E> @NotNull Iterator<E> filter(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new Filter<>(it, predicate);
    }

    public static <E> @NotNull Iterator<E> filterNot(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        if (!it.hasNext()) {
            return empty();
        }
        return new FilterNot<>(it, predicate);
    }

    public static <E> @NotNull Iterator<@NotNull E> filterNotNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new FilterNotNull<>(it);
    }

    public static <E, U> @NotNull Iterator<U> map(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends U> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) { // implicit null check of it
            return Iterators.empty();
        }
        return new AbstractIterator<U>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public U next() {
                return mapper.apply(it.next());
            }
        };
    }

    public static <E, U> @NotNull Iterator<U> mapIndexed(
            @NotNull Iterator<? extends E> it,
            @NotNull IndexedFunction<? super E, ? extends U> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) { // implicit null check of it
            return Iterators.empty();
        }
        return new AbstractIterator<U>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public U next() {
                final E nextValue = it.next();
                return mapper.apply(idx++, nextValue);
            }
        };
    }

    public static <E, U> @NotNull Iterator<U> mapNotNull(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends U> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) { // implicit null check of it
            return Iterators.empty();
        }
        return new Iterators.MapNotNull<>(it, mapper);
    }

    public static <E, U> @NotNull Iterator<U> mapIndexedNotNull(
            @NotNull Iterator<? extends E> it,
            @NotNull IndexedFunction<? super E, ? extends U> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return Iterators.empty();
        }
        return new MapIndexedNotNull<>(it, mapper);
    }

    public static <E, U> @NotNull Iterator<U> mapMulti(
            @NotNull Iterator<? extends E> it,
            @NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return Iterators.empty();
        }

        return new MapMulti<>(it, mapper);
    }

    public static <E, U> @NotNull Iterator<U> mapIndexedMulti(
            @NotNull Iterator<? extends E> it,
            @NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return Iterators.empty();
        }

        return new MapIndexedMulti<>(it, mapper);
    }

    public static <E, U> @NotNull Iterator<U> flatMap(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return empty();
        }

        return new ConcatAll<>(Iterators.map(Iterators.map(it, mapper), Iterable::iterator));
    }

    public static <E, U> @NotNull Iterator<@NotNull Tuple2<E, U>> zip(@NotNull Iterator<? extends E> it1, @NotNull Iterator<? extends U> it2) {
        if (!it1.hasNext() || !it2.hasNext()) { // implicit null check of it1 and it2
            return Iterators.empty();
        }
        return new Zip<>(it1, it2, Tuple::of);
    }

    public static <E, U, R> @NotNull Iterator<R> zip(@NotNull Iterator<? extends E> it1, @NotNull Iterator<? extends U> it2, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        if (!it1.hasNext() || !it2.hasNext()) { // implicit null check of it1 and it2
            return Iterators.empty();
        }
        return new Zip<>(it1, it2, mapper);
    }

    public static <E, U, V> @NotNull Iterator<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterator<? extends E> it1, Iterator<? extends U> it2, Iterator<? extends V> it3) {
        if (!it1.hasNext() || !it2.hasNext() || !it3.hasNext()) { // implicit null check of it1 and it2
            return Iterators.empty();
        }
        return new Zip3<>(it1, it2, it3);
    }

    public static <E> @NotNull Tuple2<Iterator<E>, Iterator<E>> span(
            @NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate
    ) {
        if (!it.hasNext()) {
            return Tuple.of(empty(), empty());
        }

        ArrayList<E> list = new ArrayList<>();

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                list.add(e);
            } else {
                it = prepended(it, e);
                break;
            }
        }

        return new Tuple2<>(list.iterator(), Iterators.narrow(it));
    }

    public static <E> E fold(
            @NotNull Iterator<? extends E> it,
            E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op
    ) {
        return foldLeft(it, zero, op);
    }

    public static <E, U> U foldLeft(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op
    ) {
        while (it.hasNext()) { // implicit null check of it
            zero = op.apply(zero, it.next());
        }
        return zero;
    }


    public static <E, U> U foldRight(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op
    ) {
        if (!it.hasNext()) { // implicit null check of it
            return zero;
        }
        ArrayList<E> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            zero = op.apply(list.get(i), zero);
        }
        return zero;
    }

    public static <E> E foldIndexed(
            @NotNull Iterator<? extends E> it,
            E zero, @NotNull IndexedBiFunction<? super E, ? super E, ? extends E> op
    ) {
        return foldLeftIndexed(it, zero, op);
    }

    public static <E, U> U foldLeftIndexed(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op
    ) {
        int idx = 0;
        while (it.hasNext()) { // implicit null check of it
            zero = op.apply(idx++, zero, it.next());
        }
        return zero;
    }

    public static <E, U> U foldRightIndexed(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op
    ) {
        if (!it.hasNext()) { // implicit null check of it
            return zero;
        }
        ArrayList<E> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            zero = op.apply(i, list.get(i), zero);
        }
        return zero;
    }

    public static <E> E reduce(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return reduceLeft(it, op);
    }

    public static <E> @Nullable E reduceOrNull(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return reduceLeftOrNull(it, op);
    }

    public static <E> @NotNull Option<E> reduceOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return reduceLeftOption(it, op);
    }

    public static <E> E reduceLeft(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        E e = it.next(); // implicit null check of it
        while (it.hasNext()) {
            e = op.apply(e, it.next());
        }
        return e;
    }

    public static <E> @Nullable E reduceLeftOrNull(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return it.hasNext() // implicit null check of it
                ? reduceLeft(it, op)
                : null;
    }

    public static <E> @NotNull Option<E> reduceLeftOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return it.hasNext() // implicit null check of it
                ? Option.some(reduceLeft(it, op))
                : Option.none();
    }

    public static <E> E reduceRight(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        ArrayList<E> list = new ArrayList<>();
        do {
            list.add(it.next());
        } while (it.hasNext());
        final int size = list.size();
        E e = list.get(size - 1);

        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(list.get(i), e);
        }
        return e;
    }

    public static <E> @Nullable E reduceRightOrNull(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return it.hasNext() // implicit null check of it
                ? reduceRight(it, op)
                : null;
    }

    public static <E> @NotNull Option<E> reduceRightOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return it.hasNext() // implicit null check of it
                ? Option.some(reduceRight(it, op))
                : Option.none();
    }

    public static Object @NotNull [] toArray(@NotNull Iterator<?> it) {
        if (!it.hasNext()) {
            return ObjectArrays.EMPTY;
        }
        ArrayList<Object> buffer = new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }
        return buffer.toArray();
    }

    public static <E> E @NotNull [] toArray(@NotNull Iterator<? extends E> it, @NotNull Class<E> type) {
        Objects.requireNonNull(type);
        final E[] emptyArray = (E[]) Array.newInstance(type, 0);

        if (!it.hasNext()) {
            return emptyArray;
        }
        ArrayList<E> buffer = new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }
        return buffer.toArray(emptyArray);
    }

    public static <E> E @NotNull [] toArray(@NotNull Iterator<? extends E> it, @NotNull IntFunction<E[]> generator) {
        Objects.requireNonNull(generator);
        if (!it.hasNext()) {
            return generator.apply(0);
        }
        ArrayList<E> buffer = new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }
        return buffer.toArray(generator.apply(buffer.size()));
    }

    public static <E, R, Builder> R collect(
            @NotNull Iterator<? extends E> it,
            @NotNull Collector<? super E, Builder, ? extends R> collector
    ) {
        Builder builder = collector.supplier().get();
        if (!it.hasNext()) { // implicit null check of it
            return collector.finisher().apply(builder);
        }

        final BiConsumer<Builder, ? super E> accumulator = collector.accumulator();
        while (it.hasNext()) {
            accumulator.accept(builder, it.next());
        }
        return collector.finisher().apply(builder);
    }

    static <E, R, Builder> R collect(
            @NotNull Iterator<? extends E> it,
            @NotNull CollectionFactory<? super E, Builder, ? extends R> factory
    ) {
        if (!it.hasNext()) {
            return factory.empty();
        }
        Builder builder = factory.newBuilder();
        while (it.hasNext()) {
            factory.addToBuilder(builder, it.next());
        }
        return factory.build(builder);
    }

    static <E, G extends Growable<? super E>> G collect(
            @NotNull Iterator<? extends E> it,
            @NotNull G destination
    ) {
        while (it.hasNext()) {
            destination.plusAssign(it.next());
        }
        return destination;
    }

    static <E, C extends java.util.Collection<? super E>> C collect(
            @NotNull Iterator<? extends E> it,
            @NotNull C collection
    ) {
        while (it.hasNext()) {
            collection.add(it.next());
        }
        return collection;
    }

    @Contract(value = "_, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer
    ) {
        return joinTo(it, buffer, ", ", "", "");
    }

    @Contract(value = "_, _, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return joinTo(it, buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        try {
            buffer.append(prefix);
            if (it.hasNext()) {
                buffer.append(Objects.toString(it.next()));
            }
            while (it.hasNext()) {
                buffer.append(separator).append(Objects.toString(it.next()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    @Contract(value = "_, _, _ -> param2", mutates = "param1, param2")
    public static <E, A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<? extends E> it,
            @NotNull A buffer,
            @NotNull Function<? super E, ? extends CharSequence> transform
    ) {
        return joinTo(it, buffer, ", ", "", "", transform);
    }

    @Contract(value = "_, _, _, _ -> param2", mutates = "param1, param2")
    public static <E, A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<? extends E> it,
            @NotNull A buffer,
            CharSequence separator,
            @NotNull Function<? super E, ? extends CharSequence> transform
    ) {
        return joinTo(it, buffer, separator, "", "", transform);
    }

    @Contract(value = "_, _, _, _, _, _ -> param2", mutates = "param1, param2")
    public static <E, A extends Appendable> @NotNull A joinTo(
            @NotNull Iterator<? extends E> it,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull Function<? super E, ? extends CharSequence> transform
    ) {
        try {
            buffer.append(prefix);
            if (it.hasNext()) {
                buffer.append(transform.apply(it.next()));
            }
            while (it.hasNext()) {
                buffer.append(separator).append(transform.apply(it.next()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    public static @NotNull String joinToString(@NotNull Iterator<?> it) {
        return joinTo(it, new StringBuilder()).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Iterator<?> it,
            CharSequence separator
    ) {
        return joinTo(it, new StringBuilder(), separator).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Iterator<?> it,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return joinTo(it, new StringBuilder(), separator, prefix, postfix).toString();
    }

    public static <E> @NotNull String joinToString(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends CharSequence> transform) {
        return joinTo(it, new StringBuilder(), transform).toString();
    }

    public static <E> @NotNull String joinToString(
            @NotNull Iterator<? extends E> it,
            CharSequence separator,
            @NotNull Function<? super E, ? extends CharSequence> transform) {
        return joinTo(it, new StringBuilder(), separator, transform).toString();
    }

    public static <E> @NotNull String joinToString(
            @NotNull Iterator<? extends E> it,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull Function<? super E, ? extends CharSequence> transform) {
        return joinTo(it, new StringBuilder(), separator, prefix, postfix, transform).toString();
    }


    public static <E> void forEach(@NotNull Iterator<? extends E> it, @NotNull Consumer<? super E> action) {
        while (it.hasNext()) {
            action.accept(it.next());
        }
    }

    public static <E, Ex extends Throwable> void forEachChecked(
            @NotNull Iterator<? extends E> it, @NotNull CheckedConsumer<? super E, ? extends Ex> action
    ) {
        try {
            while (it.hasNext()) {
                action.acceptChecked(it.next());
            }
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    public static <E> void forEachUnchecked(
            @NotNull Iterator<? extends E> it, @NotNull CheckedConsumer<? super E, ?> action
    ) {
        try {
            while (it.hasNext()) {
                action.acceptChecked(it.next());
            }
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    public static <E> void forEachIndexed(@NotNull Iterator<? extends E> it, @NotNull IndexedConsumer<? super E> action) {
        int idx = 0;
        while (it.hasNext()) {
            action.accept(idx++, it.next());
        }
    }

    public static <T, U> void forEachWith(
            @NotNull Iterator<? extends T> it1,
            @NotNull Iterator<? extends U> it2,
            @NotNull BiConsumer<? super T, ? super U> action) {
        Objects.requireNonNull(action);
        while (it1.hasNext() && it2.hasNext()) {
            action.accept(it1.next(), it2.next());
        }
    }

    public static <T, U, Ex extends Throwable> void forEachWithChecked(
            @NotNull Iterator<? extends T> it1,
            @NotNull Iterator<? extends U> it2,
            @NotNull CheckedBiConsumer<? super T, ? super U, ? extends Ex> action) throws Ex {
        forEachWith(it1, it2, action);
    }

    public static <T, U> void forEachWithUnchecked(
            @NotNull Iterator<? extends T> it1,
            @NotNull Iterator<? extends U> it2,
            @NotNull CheckedBiConsumer<? super T, ? super U, ?> action) {
        forEachWith(it1, it2, action);
    }

    private static final Iterator<?> EMPTY = new AbstractIterator<>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }

        @Override
        public String toString() {
            return "Iterator[]";
        }
    };

    static final Object TAG_VALUE = new InternalIdentifyObject();

    private static final class Frozen<E> extends AbstractIterator<E> {
        private final Iterator<E> source;

        private Frozen(Iterator<E> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public E next() {
            return source.next();
        }
    }

    private static final class OfNotNull<E> extends AbstractIterator<E> {
        private @Nullable E value;

        OfNotNull(@NotNull E value) {
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return value != null;
        }

        @Override
        public E next() {
            final E v = this.value;
            if (v == null) {
                throw new NoSuchElementException();
            }
            this.value = null;
            return v;
        }
    }

    private static final class OfNull<E> extends AbstractIterator<E> {
        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public E next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = false;
            return null;
        }
    }

    private static final class Itr2<E> extends AbstractIterator<E> {
        private int idx = 0;

        private E value1;
        private E value2;

        Itr2(E value1, E value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public boolean hasNext() {
            return idx < 2;
        }

        @Override
        public E next() {
            E res;
            switch (idx) {
                case 0:
                    res = value1;
                    value1 = null;
                    break;
                case 1:
                    res = value2;
                    value2 = null;
                    break;
                default:
                    throw new NoSuchElementException();
            }
            idx++;
            return res;
        }
    }

    private static final class Itr3<E> extends AbstractIterator<E> {
        private int idx = 0;

        private E value1;
        private E value2;
        private E value3;

        Itr3(E value1, E value2, E value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public boolean hasNext() {
            return idx < 3;
        }

        @Override
        public E next() {
            E res;
            switch (idx) {
                case 0:
                    res = value1;
                    value1 = null;
                    break;
                case 1:
                    res = value2;
                    value2 = null;
                    break;
                case 2:
                    res = value3;
                    value3 = null;
                    break;
                default:
                    throw new NoSuchElementException();
            }
            idx++;
            return res;
        }
    }

    private static final class Itr4<E> extends AbstractIterator<E> {
        private int idx = 0;

        private E value1;
        private E value2;
        private E value3;
        private E value4;

        Itr4(E value1, E value2, E value3, E value4) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
        }

        @Override
        public boolean hasNext() {
            return idx < 4;
        }

        @Override
        public E next() {
            E res;
            switch (idx) {
                case 0:
                    res = value1;
                    value1 = null;
                    break;
                case 1:
                    res = value2;
                    value2 = null;
                    break;
                case 2:
                    res = value3;
                    value3 = null;
                    break;
                case 3:
                    res = value4;
                    value4 = null;
                    break;
                default:
                    throw new NoSuchElementException();
            }
            idx++;
            return res;
        }
    }

    private static final class Itr5<E> extends AbstractIterator<E> {
        private int idx = 0;

        private E value1;
        private E value2;
        private E value3;
        private E value4;
        private E value5;

        Itr5(E value1, E value2, E value3, E value4, E value5) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
            this.value5 = value5;
        }

        @Override
        public boolean hasNext() {
            return idx < 5;
        }

        @Override
        public E next() {
            E res;
            switch (idx) {
                case 0:
                    res = value1;
                    value1 = null;
                    break;
                case 1:
                    res = value2;
                    value2 = null;
                    break;
                case 2:
                    res = value3;
                    value3 = null;
                    break;
                case 3:
                    res = value4;
                    value4 = null;
                    break;
                case 4:
                    res = value5;
                    value5 = null;
                    break;
                default:
                    throw new NoSuchElementException();
            }
            idx++;
            return res;
        }
    }

    private static final class Copies<E> extends AbstractIterator<E> {
        private int n;
        private E value;

        Copies(int n, E value) {
            this.n = n;
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return n > 0;
        }

        @Override
        public E next() {
            if (n <= 0) {
                throw new NoSuchElementException();
            }
            final E oldValue = this.value;
            if (--n == 0) {
                this.value = null;
            }
            return oldValue;
        }
    }

    private static final class FillSupplier<E> extends AbstractIterator<E> {
        private int n;
        private Supplier<? extends E> supplier;

        FillSupplier(int n, @NotNull Supplier<? extends E> supplier) {
            this.n = n;
            this.supplier = supplier;
        }

        @Override
        public boolean hasNext() {
            return n > 0;
        }

        @Override
        public E next() {
            if (n <= 0) {
                throw new NoSuchElementException();
            }
            final E res = supplier.get();
            if (--n == 0) {
                this.supplier = null;
            }
            return res;
        }
    }

    private static final class FillIntFunction<E> extends AbstractIterator<E> {
        private final int n;
        private int idx = 0;
        private IntFunction<? extends E> init;

        FillIntFunction(int n, @NotNull IntFunction<? extends E> init) {
            this.n = n;
            this.init = init;
        }

        @Override
        public boolean hasNext() {
            return idx < n;
        }

        @Override
        public E next() {
            if (idx >= n) {
                throw new NoSuchElementException();
            }
            final E res = init.apply(idx++);
            if (idx == n) {
                this.init = null;
            }
            return res;
        }
    }

    private static final class AppendedNull<E> extends AbstractIterator<E> {
        private final Iterator<? extends E> source;
        private boolean hasLast = true;

        AppendedNull(Iterator<? extends E> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return hasLast || source.hasNext();
        }

        @Override
        public E next() {
            if (source.hasNext()) {
                return source.next();
            }
            if (hasLast) {
                hasLast = false;
                return null;
            }
            throw new NoSuchElementException();
        }
    }

    private static final class AppendedNotNull<E> extends AbstractIterator<E> {
        private final Iterator<? extends E> source;
        private E last;

        AppendedNotNull(Iterator<? extends E> source, E last) {
            this.source = source;
            this.last = last;
        }

        @Override
        public boolean hasNext() {
            return last != null || source.hasNext();
        }

        @Override
        public E next() {
            if (source.hasNext()) {
                return source.next();
            }
            if (last == null) {
                throw new NoSuchElementException();
            }
            final E l = this.last;
            this.last = null;
            return l;
        }
    }

    private static final class PrependedNull<E> extends AbstractIterator<E> {
        private final Iterator<? extends E> source;
        private boolean hasHead = true;

        PrependedNull(Iterator<? extends E> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return hasHead || source.hasNext();
        }

        @Override
        public E next() {
            if (hasHead) {
                hasHead = false;
                return null;
            } else {
                return source.next();
            }
        }
    }

    private static final class PrependedNotNull<E> extends AbstractIterator<E> {
        private final Iterator<? extends E> source;
        private E head;

        PrependedNotNull(Iterator<? extends E> source, E head) {
            this.source = source;
            this.head = head;
        }

        @Override
        public boolean hasNext() {
            return head != null || source.hasNext();
        }

        @Override
        public E next() {
            if (head != null) {
                E h = this.head;
                this.head = null;
                return h;
            } else {
                return source.next();
            }
        }
    }

    private static final class Filter<E> extends AbstractIterator<E> {
        private final @NotNull Iterator<? extends E> source;
        private final @NotNull Predicate<? super E> predicate;

        private Object nextValue = TAG_VALUE;

        Filter(@NotNull Iterator<? extends E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != TAG_VALUE) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }
            E v = source.next();
            while (!predicate.test(v)) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.next();
            }

            this.nextValue = v;
            return true;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final E nv = (E) nextValue;
            nextValue = TAG_VALUE;
            return nv;
        }
    }

    private static final class FilterNot<E> extends AbstractIterator<E> {
        private final @NotNull Iterator<? extends E> source;
        private final @NotNull Predicate<? super E> predicate;

        private Object nextValue = TAG_VALUE;

        FilterNot(@NotNull Iterator<? extends E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != TAG_VALUE) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }
            E v = source.next();
            while (predicate.test(v)) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.next();
            }

            this.nextValue = v;
            return true;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final E nv = (E) nextValue;
            nextValue = TAG_VALUE;
            return nv;
        }
    }

    private static final class FilterNotNull<E> extends AbstractIterator<E> {
        private final @NotNull Iterator<? extends E> source;
        private E nextValue = null;

        FilterNotNull(@NotNull Iterator<? extends E> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != null) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }

            E v;
            do {
                v = source.next();
            } while (v == null && source.hasNext());

            if (v == null) {
                return false;
            }

            this.nextValue = v;
            return true;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final E v = this.nextValue;
            this.nextValue = null;
            return v;
        }
    }

    private static final class MapNotNull<E, U> extends AbstractIterator<U> {
        private final @NotNull Iterator<? extends E> source;
        private final @NotNull Function<? super E, ? extends U> mapper;

        private U nextValue = null;

        MapNotNull(@NotNull Iterator<? extends E> source, @NotNull Function<? super E, ? extends U> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != null) {
                return true;
            }
            U v;
            do {
                if (!source.hasNext()) {
                    return false;
                }
                v = mapper.apply(source.next());
            } while (v == null);

            this.nextValue = v;
            return true;
        }

        @Override
        public U next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final U nv = nextValue;
            nextValue = null;
            return nv;
        }
    }

    private static final class MapIndexedNotNull<E, U> extends AbstractIterator<U> {
        private final @NotNull Iterator<? extends E> source;
        private final @NotNull IndexedFunction<? super E, ? extends U> mapper;

        private int idx = 0;
        private Object nextValue = null;

        MapIndexedNotNull(@NotNull Iterator<? extends E> source, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != null) {
                return true;
            }
            U v;
            do {
                if (!source.hasNext()) {
                    return false;
                }
                v = mapper.apply(idx++, source.next());
            } while (v == null);

            this.nextValue = v;
            return true;
        }

        @Override
        public U next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            U nv = (U) nextValue;
            nextValue = null;
            return nv;
        }
    }

    private static final class MapMulti<E, T> extends AbstractIterator<E> {
        private Iterator<? extends T> source;
        private BiConsumer<? super T, ? super Consumer<? super E>> mapper;
        private ArrayDeque<E> tmp = new ArrayDeque<>();
        private Consumer<E> consumer = tmp::addLast;

        MapMulti(@NotNull Iterator<? extends T> source, @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            if (source == null) {
                return false;
            }
            while (tmp.isEmpty() && source.hasNext()) {
                mapper.accept(source.next(), consumer);
            }
            if (tmp.isEmpty()) {
                source = null;
                mapper = null;
                tmp = null;
                consumer = null;
                return false;
            } else {
                return true;
            }
        }

        @Override
        public E next() {
            if (hasNext()) {
                return tmp.removeFirst();
            }
            throw new NoSuchElementException();
        }
    }

    private static final class MapIndexedMulti<E, T> extends AbstractIterator<E> {
        private Iterator<? extends T> source;
        private IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper;
        private ArrayDeque<E> tmp = new ArrayDeque<>();
        private Consumer<E> consumer = tmp::addLast;
        private int idx = 0;

        MapIndexedMulti(@NotNull Iterator<? extends T> source, @NotNull IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            if (source == null) {
                return false;
            }
            while (tmp.isEmpty() && source.hasNext()) {
                mapper.accept(idx++, source.next(), consumer);
            }
            if (tmp.isEmpty()) {
                source = null;
                mapper = null;
                tmp = null;
                consumer = null;
                return false;
            } else {
                return true;
            }
        }

        @Override
        public E next() {
            if (hasNext()) {
                return tmp.removeFirst();
            }
            throw new NoSuchElementException();
        }
    }

    private static final class Take<E> extends AbstractIterator<E> {
        private final @NotNull Iterator<? extends E> source;
        private int c;

        Take(@NotNull Iterator<? extends E> source, int c) {
            this.source = source;
            this.c = c;
        }

        @Override
        public boolean hasNext() {
            return c > 0 && source.hasNext();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException(this + ".next()");
            }
            --c;
            return source.next();
        }
    }

    private static final class TakeWhile<E> extends AbstractIterator<E> {
        private @NotNull Iterator<? extends E> source;

        private final Predicate<? super E> predicate;

        private Object nextValue = TAG_VALUE;

        TakeWhile(@NotNull Iterator<? extends E> source, Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (nextValue != TAG_VALUE) {
                return true;
            }

            if (source.hasNext()) {
                E v = source.next();
                if (predicate.test(v)) {
                    nextValue = v;
                    return true;
                } else {
                    source = Iterators.empty();
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public E next() {
            if (hasNext()) {
                E nv = (E) nextValue;
                nextValue = TAG_VALUE;
                return nv;
            }
            throw new NoSuchElementException(this + ".next()");
        }
    }

    private static final class Concat<E> extends AbstractIterator<E> {
        private Iterator<? extends E> it1;
        private Iterator<? extends E> it2;

        Concat(Iterator<? extends E> it1, Iterator<? extends E> it2) {
            this.it1 = it1;
            this.it2 = it2;
        }

        @Override
        public boolean hasNext() {
            if (it1 != null) {
                if (it1.hasNext()) {
                    return true;
                } else {
                    it1 = null;
                }
            }
            if (it2 != null) {
                if (it2.hasNext()) {
                    return true;
                } else {
                    it2 = null;
                }
            }
            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (it1 != null) {
                return it1.next();
            }
            if (it2 != null) {
                return it2.next();
            }
            throw new AssertionError(); // never
        }
    }

    private static final class ConcatAll<E> extends AbstractIterator<E> {
        private final @NotNull Iterator<? extends Iterator<? extends E>> iterators;

        private Iterator<? extends E> current = null;

        ConcatAll(@NotNull Iterator<? extends Iterator<? extends E>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            while ((current == null || !current.hasNext()) && iterators.hasNext()) {
                current = iterators.next();
            }
            return current != null && current.hasNext();
        }

        @Override
        public E next() {
            if (hasNext()) {
                return current.next();
            }
            throw new NoSuchElementException(this + ".next()");
        }
    }

    private static final class Zip<E, U, R> extends AbstractIterator<R> {
        private Iterator<? extends E> it1;
        private Iterator<? extends U> it2;
        private BiFunction<? super E, ? super U, ? extends R> mapper;

        Zip(Iterator<? extends E> it1, Iterator<? extends U> it2, BiFunction<? super E, ? super U, ? extends R> mapper) {
            this.it1 = it1;
            this.it2 = it2;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            if (it1 != null && (it1.hasNext() && it2.hasNext())) {
                return true;
            } else {
                it1 = null;
                it2 = null;
                mapper = null;
                return false;
            }
        }

        @Override
        public R next() {
            if (hasNext()) {
                return mapper.apply(it1.next(), it2.next());
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private static final class Zip3<E, U, V> extends AbstractIterator<@NotNull Tuple3<E, U, V>> {
        private final Iterator<? extends E> it1;
        private final Iterator<? extends U> it2;
        private final Iterator<? extends V> it3;

        Zip3(Iterator<? extends E> it1, Iterator<? extends U> it2, Iterator<? extends V> it3) {
            this.it1 = it1;
            this.it2 = it2;
            this.it3 = it3;
        }

        @Override
        public boolean hasNext() {
            return it1.hasNext() && it2.hasNext() && it3.hasNext();
        }

        @Override
        public @NotNull Tuple3<E, U, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return Tuple.of(it1.next(), it2.next(), it3.next());
        }
    }
}
