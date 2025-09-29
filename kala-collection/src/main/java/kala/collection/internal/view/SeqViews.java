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
package kala.collection.internal.view;

import kala.collection.*;
import kala.collection.base.AbstractIterator;
import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.collection.mutable.MutableArrayList;
import kala.control.Option;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.index.Index;
import kala.index.Indexes;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("ALL")
public final class SeqViews {
    public static class Empty<E> extends CollectionViews.Empty<E> implements SeqView<E> {

        public static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public boolean supportsFastRandomAccess() {
            return true;
        }

        @Override
        public @NotNull SeqView<E> reversed() {
            return this;
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.empty();
        }

        @Override
        public E get(@Index int index) {
            throw new IndexOutOfBoundsException(index);
        }

        @Override
        public @NotNull SeqView<E> concat(@NotNull SeqLike<? extends E> other) {
            return (SeqView<E>) other.view();
        }

        @Override
        public @NotNull SeqView<E> filter(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull SeqView<E> filterNot(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull SeqView<@NotNull E> filterNotNull() {
            return this;
        }

        @Override
        public <U> @NotNull SeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return (SeqView<U>) this;
        }

        @Override
        public <U> @NotNull SeqView<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return SeqView.empty();
        }

        @Override
        public <U> @NotNull SeqView<U> mapNotNull(@NotNull Function<? super E, ? extends U> mapper) {
            return SeqView.empty();
        }

        @Override
        public <U> @NotNull SeqView<U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return SeqView.empty();
        }

        @Override
        public <U> @NotNull SeqView<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
            return (SeqView<U>) this;
        }

        @Override
        public @NotNull SeqView<E> sorted(Comparator<? super E> comparator) {
            return this;
        }
    }

    public static class Single<E> extends CollectionViews.Single<E> implements SeqView<E> {
        public Single(E value) {
            super(value);
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return true;
        }

        @Override
        public @NotNull SeqView<E> reversed() {
            return this;
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value);
        }

        @Override
        public E get(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index);
            }
            return value;
        }

        @Override
        public @NotNull SeqView<E> updated(@Index int index, E newValue) {
            if (index == 0 || index == ~1) {
                return new Single<>(value);
            }
            throw Indexes.outOfBounds(index, 1);
        }

        @Override
        public @NotNull SeqView<E> filterNotNull() {
            return value == null ? SeqView.empty() : this;
        }
    }

    public static class Of<E, C extends SeqLike<E>> extends CollectionViews.Of<E, C> implements SeqView<E> {
        public Of(@NotNull C source) {
            super(source);
        }

        @Override
        public @NotNull SeqIterator<E> seqIterator() {
            return source.seqIterator().freeze();
        }

        @Override
        public @NotNull SeqIterator<E> seqIterator(int index) {
            return source.seqIterator(index).freeze();
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return source.supportsFastRandomAccess();
        }

        public E get(@Index int index) {
            return source.get(index);
        }

        public int indexOf(Object value) {
            return source.indexOf(value);
        }

        public int indexOf(Object value, int from) {
            return source.indexOf(value, from);
        }

        public int indexWhere(@NotNull Predicate<? super E> predicate) {
            return source.indexWhere(predicate);
        }

        public int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
            return source.indexWhere(predicate, from);
        }

        public int lastIndexOf(Object value) {
            return source.lastIndexOf(value);
        }

        public int lastIndexOf(Object value, int end) {
            return source.lastIndexOf(value, end);
        }

        public int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
            return source.lastIndexWhere(predicate);
        }

        public int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
            return source.lastIndexWhere(predicate, end);
        }

        public void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            source.forEachIndexed(action);
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return source.reverseIterator();
        }
    }

    @SuppressWarnings("unchecked")
    public static class OfArraySlice<E> implements SeqView<E>, IndexedSeqLike<E> {
        protected final Object[] array;
        protected final int beginIndex;
        protected final int endIndex;

        public OfArraySlice(Object[] array, int beginIndex, int endIndex) {
            this.array = array;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return (Iterator<E>) GenericArrays.iterator(array, beginIndex, endIndex);
        }

        @Override
        public final int size() {
            return endIndex - beginIndex;
        }

        public final int beginIndex() {
            return beginIndex;
        }

        public final int endIndex() {
            return endIndex;
        }

        @Override
        public final E get(@Index int index) {
            return (E) array[Indexes.checkIndex(index, size()) + beginIndex];
        }

        @Override
        public @NotNull SeqView<E> slice(@Index int beginIndex, @Index int endIndex) {
            final int size = size();
            beginIndex = Indexes.checkBeginIndex(beginIndex, size);
            endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

            final int ns = endIndex - beginIndex;
            switch (ns) {
                case 0:
                    return SeqView.empty();
                case 1:
                    return SeqView.of((E) array[this.beginIndex + beginIndex]);
            }
            return new OfArraySlice<>(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
        }

        @Override
        public @NotNull SeqView<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return SeqView.empty();
            }

            return new OfArraySlice<>(array, beginIndex + n, endIndex);
        }

        @Override
        public @NotNull SeqView<E> dropLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return SeqView.empty();
            }

            return new OfArraySlice<>(array, beginIndex, endIndex - n);
        }

        @Override
        public @NotNull SeqView<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return SeqView.empty();
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return SeqView.of((E) array[beginIndex]);
            }
            return new OfArraySlice<>(array, beginIndex, beginIndex + n);
        }

        @Override
        public @NotNull SeqView<E> takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return SeqView.empty();
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return SeqView.of((E) array[beginIndex]);
            }
            return new OfArraySlice<>(array, endIndex - n, endIndex);
        }
    }

    public static class WithCachedSize<E, C extends Seq<E>> extends Of<E, C> {
        protected int cachedSize = -1;

        public WithCachedSize(@NotNull C source) {
            super(source);
        }

        public WithCachedSize(@NotNull C source, int size) {
            super(source);
            this.cachedSize = size;
        }

        @Override
        public boolean isEmpty() {
            final int cachedSize = this.cachedSize;
            if (cachedSize == 0) {
                return true;
            } else if (cachedSize > 0) {
                return false;
            }
            return source.isEmpty();
        }

        @Override
        public final int size() {
            if (cachedSize >= 0) {
                return cachedSize;
            }
            final int ss = source.size();
            this.cachedSize = ss;
            return ss;
        }

        @Override
        public final int knownSize() {
            if (cachedSize >= 0) {
                return cachedSize;
            }
            final int sks = source.knownSize();
            if (sks >= 0) {
                this.cachedSize = sks;
                return sks;
            }
            return -1;
        }
    }

    public static class Slice<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;
        protected final int beginIndex;
        protected final int endIndex;

        public Slice(@NotNull SeqLike<E> source, int beginIndex, int endIndex) {
            this.source = source;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return this.new Itr();
        }

        @Override
        public boolean isEmpty() {
            return beginIndex == endIndex;
        }

        @Override
        public int size() {
            return endIndex - beginIndex;
        }

        @Override
        public int knownSize() {
            return endIndex - beginIndex;
        }

        private final class Itr extends AbstractIterator<E> {
            private int idx;
            private final Iterator<E> it;

            Itr() {
                Iterator<E> it = source.iterator();
                int i = beginIndex;
                while (i > 0) {
                    try {
                        it.next();
                    } catch (NoSuchElementException e) {
                        throw new IndexOutOfBoundsException();
                    }
                    --i;
                }
                idx = beginIndex;
                this.it = it;
            }

            @Override
            public final boolean hasNext() {
                return idx < endIndex;
            }

            @Override
            public final E next() {
                if (idx >= endIndex) {
                    throw new NoSuchElementException();
                }
                try {
                    E next = it.next();
                    ++idx;
                    return next;
                } catch (NoSuchElementException e) {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
    }

    public static class Drop<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        protected final int n;

        public Drop(@NotNull SeqLike<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.drop(source.iterator(), n);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().skip(n);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.stream().skip(n).parallel();
        }

        @Override
        public final int size() {
            return Integer.max(0, source.size() - n);
        }

        @Override
        public int knownSize() {
            final int sks = source.knownSize();
            return sks < 0 ? -1 : Integer.max(0, sks - n);
        }

        @Override
        public @NotNull SeqView<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            return source.view().drop(n + this.n);
        }
    }

    public static class DropLast<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqView<E> source;

        private final @Range(from = 1, to = Integer.MAX_VALUE) int n;

        public DropLast(@NotNull SeqView<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            final int ss = source.size();
            if (n >= ss) {
                return Iterators.empty();
            }

            return Iterators.take(source.iterator(), ss - n);
        }

        @Override
        public final E get(@Index int index) {
            return this.source.get(Indexes.checkIndex(index, size()));
        }

        @Override
        public final int size() {
            return Integer.max(0, source.size() - n);
        }

        @Override
        public final int knownSize() {
            final int sks = source.knownSize();
            if (sks < 0) {
                return -1;
            }
            if (n >= sks) {
                return 0;
            }

            return sks - n;
        }
    }

    public static class DropWhile<E> extends AbstractSeqView<E> {

        private final @NotNull SeqLike<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public DropWhile(@NotNull SeqLike<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.dropWhile(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return StreamSupport.stream(spliterator(), true);
        }
    }

    public static class Take<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        protected final @Range(from = 1, to = Integer.MAX_VALUE) int n;

        public Take(@NotNull SeqLike<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.take(source.iterator(), n);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().limit(n);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.stream().limit(n).parallel();
        }

        @Override
        public int size() {
            if (n <= 0) {
                return 0;
            }

            return Integer.min(n, source.size());
        }

        @Override
        public int knownSize() {
            if (n <= 0) {
                return 0;
            }
            final int sks = source.knownSize();
            if (sks < 0) {
                return -1;
            }
            return Integer.min(sks, n);
        }

        @Override
        public @NotNull SeqView<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return SeqView.empty();
            }
            if (this.n <= n) {
                return this;
            }
            return new Take<>(source, n);
        }
    }

    public static class TakeLast<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        private final int n;

        public TakeLast(@NotNull SeqLike<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final E get(@Index int index) {
            if (index >= 0) {
                int sourceSize = source.size();
                if (sourceSize <= n) {
                    return source.get(index);
                } else {
                    Objects.checkIndex(index, n);
                    int delta = sourceSize - n;
                    return source.get(index + delta);
                }
            } else {
                if (index == ~0 || ~index > n) {
                    throw Indexes.outOfBounds(index);
                }

                return source.get(index);
            }
        }

        @Override
        public final int size() {
            return Integer.min(source.size(), n);
        }

        @Override
        public final int knownSize() {
            int ks = source.knownSize();
            return ks >= 0 ? Integer.min(ks, n) : -1;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            int ks = source.knownSize();
            if (ks == 0 || n <= 0) {
                return Iterators.empty();
            }
            if (n == Integer.MAX_VALUE) {
                return source.iterator();
            }
            if (ks > 0) {
                return Iterators.drop(source.iterator(), Integer.max(ks - n, 0));
            }
            return new AbstractIterator<E>() {
                Iterator<E> it = source.iterator();
                int len = -1;
                int pos = 0;
                MutableArrayList<E> buf = null;

                private void init() {
                    if (buf != null) {
                        return;
                    }
                    buf = new MutableArrayList<>(Integer.min(n, 256));
                    len = 0;
                    while (it.hasNext()) {
                        E next = it.next();
                        if (pos >= buf.size()) {
                            buf.append(next);
                        } else {
                            buf.set(pos, next);
                        }
                        if (++pos == n) {
                            pos = 0;
                        }
                        ++len;
                    }
                    it = null;
                    if (len > n) {
                        len = n;
                    }
                    pos = pos - len;
                    if (pos < 0) {
                        pos += n;
                    }
                }

                @Override
                public final boolean hasNext() {
                    init();
                    return len > 0;
                }

                @Override
                public final E next() {
                    init();
                    if (len == 0) {
                        throw new NoSuchElementException();
                    }
                    E v = buf.get(pos);
                    ++pos;
                    if (pos == n) {
                        pos = 0;
                    }
                    --len;
                    return v;
                }
            };
        }
    }

    public static class TakeWhile<E> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public TakeWhile(@NotNull SeqLike<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.takeWhile(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return StreamSupport.stream(spliterator(), true);
        }
    }

    public static class Concat<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<? extends E> seq1;
        protected final @NotNull SeqLike<? extends E> seq2;

        public Concat(@NotNull SeqLike<? extends E> seq1, @NotNull SeqLike<? extends E> seq2) {
            this.seq1 = seq1;
            this.seq2 = seq2;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.concat(seq1.iterator(), seq2.iterator());
        }

        @Override
        public final int size() {
            return seq1.size() + seq2.size();
        }

        @Override
        public int knownSize() {
            final int ks1 = seq1.knownSize();
            if (ks1 < 0) {
                return -1;
            }
            final int ks2 = seq2.knownSize();
            if (ks2 < 0) {
                return -1;
            }
            return ks1 + ks2;
        }
    }

    public static class Updated<E> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<E> source;

        private final int index;
        private final E newValue;

        public Updated(@NotNull SeqLike<E> source, int index, E newValue) {
            this.source = source;
            this.index = index;
            this.newValue = newValue;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return new Iterator<E>() {
                private final Iterator<E> it = source.iterator();
                private int i = 0;

                @Override
                public final boolean hasNext() {
                    if (it.hasNext()) {
                        return true;
                    }
                    if (index >= i) {
                        throw new IndexOutOfBoundsException();
                    }
                    return false;
                }

                @Override
                public final E next() {
                    E value = it.next();
                    if (i++ == index) {
                        value = newValue;
                    }

                    return value;
                }
            };
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }

        @Override
        public final E get(@Index int index) {
            index = Indexes.checkIndex(index, size());

            if (index == this.index) {
                return newValue;
            }
            return source.get(index);
        }
    }

    public static class Prepended<E> extends AbstractSeqView<E> {

        private final @NotNull SeqLike<E> source;
        private final E value;

        public Prepended(@NotNull SeqLike<E> source, E value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.prepended(source.iterator(), value);
        }

        @Override
        public final int size() {
            return source.size() + 1;
        }

        @Override
        public int knownSize() {
            final int sks = source.knownSize();
            if (sks < 0) {
                return -1;
            }
            return sks + 1;
        }

        @Override
        public final E get(@Index int index) {
            index = Indexes.checkIndex(index, size());
            return index == 0 ? value : source.get(index - 1);
        }
    }

    public static class Appended<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        protected final E value;

        public Appended(@NotNull SeqLike<E> source, E value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final int size() {
            return source.size() + 1;
        }

        @Override
        public int knownSize() {
            final int sks = source.knownSize();
            if (sks < 0) {
                return -1;
            }
            return sks + 1;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.appended(source.iterator(), value);
        }
    }

    public static class Inserted<E> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<E> source;

        private final int insertedIndex;
        private final E value;

        public Inserted(@NotNull SeqLike<E> source, int index, E value) {
            this.source = source;
            this.insertedIndex = index;
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.inserted(source.iterator(), insertedIndex, value);
        }

        @Override
        public final int size() {
            return source.size() + 1;
        }

        @Override
        public int knownSize() {
            int ks = source.knownSize();
            if (ks < 0) {
                return ks;
            } else {
                return ks + 1;
            }
        }

        @Override
        public final E get(@Index int index) {
            index = Indexes.checkIndex(index, size());

            if (index == insertedIndex) {
                return value;
            } else if (index > insertedIndex) {
                return source.get(index - 1);
            } else {
                return source.get(index);
            }
        }
    }

    public static class RemovedAt<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        protected final int removedIndex;

        public RemovedAt(@NotNull SeqLike<E> source, int index) {
            this.source = source;
            this.removedIndex = index;
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return source.supportsFastRandomAccess();
        }

        @Override
        public int size() {
            int size = source.size();
            return size > 0 ? size - 1 : size;
        }

        @Override
        public int knownSize() {
            int size = source.knownSize();
            return size > 0 ? size - 1 : size;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.removed(source.iterator(), removedIndex);
        }

        @Override
        public E get(@Index int index) {
            index = Indexes.checkIndex(index, size());
            return source.get(index < removedIndex ? index : index - 1);
        }
    }

    public static class Reversed<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqLike<E> source;

        public Reversed(@NotNull SeqLike<E> source) {
            this.source = source;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.reverseIterator();
        }

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        @Override
        public final E get(@Index int index) {
            if (index >= 0) {
                return source.get(size() - 1 - index);
            } else {
                if (index == ~0) {
                    throw Indexes.outOfBounds(index);
                }

                return source.get(~index - 1);
            }
        }

        @Override
        public final @NotNull SeqView<E> reversed() {
            return source.view();
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return source.iterator();
        }
    }

    public static final class Filter<E> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull SeqLike<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().filter(predicate);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().filter(predicate);
        }
    }

    public static final class FlatMapped<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<? extends T> source;

        private final @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper;

        public FlatMapped(
                @NotNull SeqView<? extends T> source,
                @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }


        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.concat(source.map(it -> mapper.apply(it).iterator()));
        }
    }

    public static class Mapped<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;
        private final @NotNull Function<? super T, ? extends E> mapper;

        public Mapped(@NotNull SeqLike<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.map(source.iterator(), mapper);
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return source.stream().map(mapper);
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return source.parallelStream().map(mapper);
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        @Override
        public final E get(@Index int index) {
            return mapper.apply(source.get(index));
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return source.getOption(index).map(mapper);
        }
    }

    public static class MapIndexed<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;

        private final @NotNull IndexedFunction<? super T, ? extends E> mapper;

        public MapIndexed(@NotNull SeqView<T> source, @NotNull IndexedFunction<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapIndexed(source.iterator(), mapper);
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        //endregion

        @Override
        public final E get(@Index int index) {
            index = Indexes.checkIndex(index, size());
            return mapper.apply(index, source.get(index));
        }
    }

    public static class MapNotNull<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;

        private final @NotNull Function<? super T, ? extends E> mapper;

        public MapNotNull(@NotNull SeqLike<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapNotNull(source.iterator(), mapper);
        }
    }

    public static class MapIndexedNotNull<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;
        private final @NotNull IndexedFunction<? super T, ? extends E> mapper;

        public MapIndexedNotNull(@NotNull SeqLike<T> source, @NotNull IndexedFunction<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapIndexedNotNull(source.iterator(), mapper);
        }
    }

    public static final class MapMulti<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;
        private final @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper;

        public MapMulti(@NotNull SeqLike<T> source, @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapMulti(source.iterator(), mapper);
        }
    }

    public static final class MapIndexedMulti<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqLike<T> source;
        private final @NotNull IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper;

        public MapIndexedMulti(@NotNull SeqLike<T> source, @NotNull IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapIndexedMulti(source.iterator(), mapper);
        }
    }

    public static final class Sorted<E> extends AbstractSeqView<E> {
        private final SeqView<E> source;
        private Comparator<? super E> comparator;
        private Object[] sorted;

        @SuppressWarnings("unchecked")
        public Sorted(@NotNull SeqView<E> source, @NotNull Comparator<? super E> comparator) {
            this.source = source;
            this.comparator = comparator;
        }

        private void initSorted() {
            if (sorted == null) {
                Object[] arr = source.toArray();
                Arrays.sort(arr, (Comparator<Object>) comparator);
                this.sorted = arr;
                this.comparator = null;
            }
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            initSorted();
            return (Iterator<E>) GenericArrays.iterator(sorted);
        }

        @Override
        public final int size() {
            return sorted == null ? source.size() : sorted.length;
        }

        @Override
        public final int knownSize() {
            return sorted == null ? source.knownSize() : sorted.length;
        }

        @Override
        public final E get(@Index int index) {
            initSorted();
            return (E) GenericArrays.get(sorted, index);
        }

        @Override
        public final @NotNull SeqView<E> sorted(Comparator<? super E> comparator) {
            if (comparator == Comparator.naturalOrder()) {
                comparator = null;
            }

            if (comparator == this.comparator) {
                return this;
            }
            if ((comparator == null && this.comparator == Comparator.reverseOrder())
                    || (comparator == Comparator.reverseOrder() && this.comparator == null)) {
                return reversed();
            }

            return new Sorted<>(source, comparator);
        }
    }

    public static class Zip<E, U, R> extends AbstractSeqView<R> {
        private final @NotNull SeqLike<? extends E> source;
        private final @NotNull SeqLike<? extends U> other;
        private final @NotNull BiFunction<? super E, ? super U, ? extends R> mapper;

        public Zip(@NotNull SeqLike<? extends E> source, @NotNull SeqLike<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
            this.source = source;
            this.other = other;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<R> iterator() {
            return Iterators.zip(source.iterator(), other.iterator(), mapper);
        }

        @Override
        public final boolean isEmpty() {
            return source.isEmpty() || other.isEmpty();
        }

        @Override
        public final int size() {
            final int ss = source.size();
            return ss == 0 ? 0 : Integer.min(ss, other.size());
        }

        @Override
        public int knownSize() {
            final int ks1 = source.knownSize();
            if (ks1 < 0) {
                return -1;
            }
            if (ks1 == 0) {
                return 0;
            }

            final int ks2 = other.knownSize();
            return Integer.min(ks1, ks2);
        }

        @Override
        public final @NotNull R get(@Index int index) {
            index = Indexes.checkIndex(index, size());
            return mapper.apply(source.get(index), other.get(index));
        }

        @Override
        public final @NotNull SeqView<R> reversed() {
            return source.view().reversed().zip(other.view().reversed(), mapper);
        }
    }

    public static final class Zip3<E, U, V> extends AbstractSeqView<Tuple3<E, U, V>> {
        private final @NotNull SeqLike<? extends E> source;
        private final @NotNull SeqLike<? extends U> other1;
        private final @NotNull SeqLike<? extends V> other2;

        public Zip3(@NotNull SeqLike<? extends E> source, @NotNull SeqLike<? extends U> other1, @NotNull SeqLike<? extends V> other2) {
            this.source = source;
            this.other1 = other1;
            this.other2 = other2;
        }

        @Override
        public @NotNull Iterator<Tuple3<E, U, V>> iterator() {
            return Iterators.zip3(source.iterator(), other1.iterator(), other2.iterator());
        }

        @Override
        public final @NotNull Tuple3<E, U, V> get(@Index int index) {
            index = Indexes.checkIndex(index, size());
            return Tuple.of(source.get(index), other1.get(index), other2.get(index));
        }

        @Override
        public final @NotNull SeqView<Tuple3<E, U, V>> reversed() {
            return (SeqView) source.view().reversed().zip3(other1.view().reversed(), other2.view().reversed());
        }
    }
}
