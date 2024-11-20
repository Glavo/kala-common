package kala.collection.primitive.internal.view;

import kala.Conditions;
import kala.collection.*;
import kala.collection.base.Iterators;
import kala.collection.base.primitive.*;
import kala.collection.primitive.*;
import kala.collection.mutable.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
<#if IsSpecialized>
import java.util.function.*;
import java.util.stream.${Type}Stream;
</#if>

@SuppressWarnings("ALL")
public final class ${Type}SeqViews {
    public static final ${Type}SeqView EMPTY = new Empty();

    public static class Empty extends ${Type}CollectionViews.Empty implements ${Type}SeqView {
        @Override
        public boolean supportsFastRandomAccess() {
            return true;
        }

        @Override
        public @NotNull ${Type}SeqView reversed() {
            return this;
        }

        @Override
        public @NotNull ${Type}Iterator reverseIterator() {
            return ${Type}Iterator.empty();
        }

        @Override
        public ${PrimitiveType} get(int index) {
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public @NotNull ${Type}SeqView concat(@NotNull ${Type}SeqLike other) {
            return (${Type}SeqView) other.view();
        }

        @Override
        public @NotNull ${Type}SeqView filter(@NotNull ${Type}Predicate predicate) {
            return this;
        }

        @Override
        public @NotNull ${Type}SeqView filterNot(@NotNull ${Type}Predicate predicate) {
            return this;
        }

        @Override
        public @NotNull ${Type}SeqView map(@NotNull ${Type}UnaryOperator mapper) {
            return this;
        }

        @Override
        public @NotNull <U> SeqView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
            return SeqView.empty();
        }

        @Override
        public @NotNull ${Type}SeqView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
            return this;
        }

        @Override
        public @NotNull ${Type}SeqView sorted() {
            return this;
        }
    }

    public static class Single extends ${Type}CollectionViews.Single implements ${Type}SeqView {

        public Single(${PrimitiveType} value) {
            super(value);
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return true;
        }

        @Override
        public @NotNull ${Type}SeqView reversed() {
            return this;
        }

        @Override
        public final @NotNull ${Type}Iterator reverseIterator() {
            return ${Type}Iterator.of(value);
        }

        @Override
        public ${PrimitiveType} get(int index) {
            if (index != 0) throw new IndexOutOfBoundsException("index: " + index);

            return value;
        }

        @Override
        public @NotNull ${Type}SeqView updated(int index, ${PrimitiveType} newValue) {
            if (index != 0) throw new IndexOutOfBoundsException("index: " + index);

            return new Single(newValue);
        }
    }

    public static class Of<C extends ${Type}SeqLike> extends ${Type}CollectionViews.Of<C> implements ${Type}SeqView {
        public Of(@NotNull C source) {
            super(source);
        }

        /*
        @Override
        public @NotNull ${Type}SeqIterator seqIterator() {
            return source.seqIterator().frozen();
        }

        @Override
        public @NotNull ${Type}SeqIterator seqIterator(int index) {
            return source.seqIterator(index).frozen();
        }
         */

        @Override
        public boolean supportsFastRandomAccess() {
            return source.supportsFastRandomAccess();
        }

        public ${PrimitiveType} get(int index) {
            return source.get(index);
        }

        public boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
        }

        public int indexOf(${PrimitiveType} value) {
            return source.indexOf(value);
        }

        public int indexOf(${PrimitiveType} value, int from) {
            return source.indexOf(value, from);
        }

        public int indexWhere(@NotNull ${Type}Predicate predicate) {
            return source.indexWhere(predicate);
        }

        public int indexWhere(@NotNull ${Type}Predicate predicate, int from) {
            return source.indexWhere(predicate, from);
        }

        public int lastIndexOf(${PrimitiveType} value) {
            return source.lastIndexOf(value);
        }

        public int lastIndexOf(${PrimitiveType} value, int end) {
            return source.lastIndexOf(value, end);
        }

        public int lastIndexWhere(@NotNull ${Type}Predicate predicate) {
            return source.lastIndexWhere(predicate);
        }

        public int lastIndexWhere(@NotNull ${Type}Predicate predicate, int end) {
            return source.lastIndexWhere(predicate, end);
        }

        @Override
        public @NotNull ${Type}Iterator reverseIterator() {
            return source.reverseIterator();
        }
    }

    public static class OfArraySlice implements ${Type}SeqView, Indexed${Type}SeqLike {
        protected final ${PrimitiveType}[] array;
        protected final int beginIndex;
        protected final int endIndex;

        public OfArraySlice(${PrimitiveType}[] array, int beginIndex, int endIndex) {
            this.array = array;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return true;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return ${Type}Arrays.iterator(array, beginIndex, endIndex);
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
        public final ${PrimitiveType} get(int index) {
            Objects.checkIndex(index, size());
            return array[index + beginIndex];
        }

        @Override
        public @NotNull ${Type}SeqView slice(int beginIndex, int endIndex) {
            Conditions.checkPositionIndices(beginIndex, endIndex, size());
            final int ns = endIndex - beginIndex;
            switch (ns) {
                case 0:
                    return EMPTY;
                case 1:
                    return new Single(array[this.beginIndex + beginIndex]);
            }
            return new OfArraySlice(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
        }

        @Override
        public @NotNull ${Type}SeqView drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return EMPTY;
            }

            return new OfArraySlice(array, beginIndex + n, endIndex);
        }

        @Override
        public @NotNull ${Type}SeqView dropLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return EMPTY;
            }

            return new OfArraySlice(array, beginIndex, endIndex - n);
        }

        @Override
        public @NotNull ${Type}SeqView take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return EMPTY;
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new Single(array[beginIndex]);
            }
            return new OfArraySlice(array, beginIndex, beginIndex + n);
        }

        @Override
        public @NotNull ${Type}SeqView takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return EMPTY;
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new Single(array[beginIndex]);
            }
            return new OfArraySlice(array, endIndex - n, endIndex);
        }
    }

    public static class WithCachedSize<C extends ${Type}Seq> extends Of<C> {
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

    public static class Slice extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;
        protected final int beginIndex;
        protected final int endIndex;

        public Slice(@NotNull ${Type}SeqView source, int beginIndex, int endIndex) {
            this.source = source;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
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

        private final class Itr extends Abstract${Type}Iterator {
            private int idx;
            private final ${Type}Iterator it;

            Itr() {
                ${Type}Iterator it = source.iterator();
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
            public final ${PrimitiveType} next${Type}() {
                if (idx >= endIndex) {
                    throw new NoSuchElementException();
                }
                try {
                    ${PrimitiveType} next = it.next();
                    ++idx;
                    return next;
                } catch (NoSuchElementException e) {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
    }

    public static class Updated extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;

        private final int index;

        private final ${PrimitiveType} newValue;

        public Updated(@NotNull ${Type}SeqView source, int index, ${PrimitiveType} newValue) {
            this.source = source;
            this.index = index;
            this.newValue = newValue;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return new Abstract${Type}Iterator() {
                private final ${Type}Iterator it = source.iterator();
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
                public final ${PrimitiveType} next${Type}() {
                    ${PrimitiveType} value = it.next();
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
        public boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
        }

        @Override
        public final ${PrimitiveType} get(int index) {
            if (index == this.index) {
                return newValue;
            }
            return source.get(index);
        }
    }

    public static class Drop extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        protected final int n;

        public Drop(@NotNull ${Type}SeqView source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return iterator().drop(n);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().skip(n);
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.stream().skip(n).parallel();
        }
</#if>

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
        public @NotNull ${Type}SeqView drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            return source.drop(n + this.n);
        }
    }

    public static class DropLast extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        private final @Range(from = 1, to = Integer.MAX_VALUE) int n;

        public DropLast(@NotNull ${Type}SeqView source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            final int ss = source.size();
            if (n >= ss) {
                return ${Type}Iterator.empty();
            }

            return source.iterator().take(ss - n);
        }

        @Override
        public final ${PrimitiveType} get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("Index(" + index + ") < 0");
            }

            final ${Type}SeqView source = this.source;
            final int n = this.n;

            if (n <= 0) {
                return this.source.get(index);
            }

            final int size = Integer.max(source.size() - n, 0);
            Objects.checkIndex(index, size);

            return this.source.get(index);
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

    public static class DropWhile extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;

        private final @NotNull ${Type}Predicate predicate;

        public DropWhile(@NotNull ${Type}SeqView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().dropWhile(predicate);
        }
    }

    public static class Take extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        protected final @Range(from = 1, to = Integer.MAX_VALUE) int n;

        public Take(@NotNull ${Type}SeqView source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().take(n);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().limit(n);
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.stream().limit(n).parallel();
        }
</#if>

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
        public @NotNull ${Type}SeqView take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return EMPTY;
            }
            if (this.n <= n) {
                return this;
            }
            return new Take(source, n);
        }
    }

    public static class TakeLast extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        private final int n;
        private final int delta;

        public TakeLast(@NotNull ${Type}SeqView source, int n) {
            this.source = source;
            this.n = Integer.max(n, 0);
            this.delta = Integer.max(0, source.size() - Integer.max(0, n));
        }

        @Override
        public final ${PrimitiveType} get(int index) {
            return source.get(index + delta);
        }

        @Override
        public final int size() {
            return source.size() - delta;
        }

        @Override
        public final int knownSize() {
            int kn = source.knownSize();
            return kn >= 0 ? Integer.min(kn, n) : -1;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            int k = source.knownSize();
            if (k == 0 || n <= 0) {
                return ${Type}Iterator.empty();
            }
            if (n == Integer.MAX_VALUE) {
                return source.iterator();
            }
            if (k > 0) {
                return source.iterator().drop(Integer.max(k - n, 0));
            }
            return new Abstract${Type}Iterator() {
                ${Type}Iterator it = source.iterator();
                int len = -1;
                int pos = 0;
                Mutable${Type}ArrayList buf = null;

                private void init() {
                    if (buf != null) {
                        return;
                    }
                    buf = new Mutable${Type}ArrayList();
                    len = 0;
                    while (it.hasNext()) {
                        ${PrimitiveType} next = it.next${Type}();
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
                public final ${PrimitiveType} next${Type}() {
                    init();
                    if (len == 0) {
                        throw new NoSuchElementException();
                    }

                    ${PrimitiveType} v = buf.get(pos);
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

    public static class TakeWhile extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;

        private final @NotNull ${Type}Predicate predicate;

        public TakeWhile(@NotNull ${Type}SeqView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().takeWhile(predicate);
        }
    }

    public static class Concat extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqLike seq1;
        protected final @NotNull ${Type}SeqLike seq2;

        public Concat(@NotNull ${Type}SeqLike seq1, @NotNull ${Type}SeqLike seq2) {
            this.seq1 = seq1;
            this.seq2 = seq2;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return ${Type}Iterator.concat(seq1.iterator(), seq2.iterator());
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

    public static class Prepended extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;

        private final ${PrimitiveType} value;

        public Prepended(@NotNull ${Type}SeqView source, ${PrimitiveType} value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().prepended(value);
        }

        @Override
        public final ${PrimitiveType} get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
            return index == 0 ? value : source.get(index - 1);
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
    }

    public static class Appended extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        protected final ${PrimitiveType} value;

        public Appended(@NotNull ${Type}SeqView source, ${PrimitiveType} value) {
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
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().appended(value);
        }
    }

    public static class Reversed extends Abstract${Type}SeqView {
        protected final @NotNull ${Type}SeqView source;

        public Reversed(@NotNull ${Type}SeqView source) {
            this.source = source;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
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
        public final ${PrimitiveType} get(int index) {
            return source.get(size() - 1 - index);
        }

        @Override
        public final @NotNull ${Type}SeqView reversed() {
            return source;
        }

        @Override
        public final @NotNull ${Type}Iterator reverseIterator() {
            return source.iterator();
        }
    }

    public static final class Filter extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;
        private final @NotNull ${Type}Predicate predicate;

        public Filter(@NotNull ${Type}SeqView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return source.iterator().filter(predicate);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().filter(predicate);
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.parallelStream().filter(predicate);
        }
</#if>
    }

    public static final class FilterNot extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;
        private final @NotNull ${Type}Predicate predicate;

        public FilterNot(@NotNull ${Type}SeqView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().filterNot(predicate);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().filter(predicate.negate());
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.parallelStream().filter(predicate.negate());
        }
</#if>
    }

    public static final class FlatMapped extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqLike source;

        private final @NotNull ${Type}Function<? extends ${Type}Traversable> mapper;

        public FlatMapped(
                @NotNull ${Type}SeqLike source,
                @NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return ${Type}Iterator.concat(source.mapToObj(it -> mapper.apply(it).iterator()));
        }
    }

    public static final class FlatMapToObj<T> extends AbstractSeqView<T> {
        private final @NotNull ${Type}SeqLike source;

        private final @NotNull ${Type}Function<? extends Iterable<? extends T>> mapper;

        public FlatMapToObj(
                @NotNull ${Type}SeqLike source,
                @NotNull ${Type}Function<? extends Iterable<? extends T>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<T> iterator() {
            return Iterators.concat(source.mapToObj(it -> mapper.apply(it).iterator()));
        }
    }

    public static class Mapped extends Abstract${Type}SeqView {
        private final @NotNull ${Type}SeqView source;

        private final @NotNull ${Type}UnaryOperator mapper;

        public Mapped(@NotNull ${Type}SeqView source, @NotNull ${Type}UnaryOperator mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator().map(mapper);
        }
<#if IsSpecialized>

        @Override
        public final @NotNull ${Type}Stream stream() {
            return source.stream().map(mapper);
        }

        @Override
        public final @NotNull ${Type}Stream parallelStream() {
            return source.parallelStream().map(mapper);
        }
</#if>

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        @Override
        public final ${PrimitiveType} get(int index) {
            return mapper.applyAs${Type}(source.get(index));
        }
    }

    public static class MapToObj<E> extends AbstractSeqView<E> {
        private final @NotNull ${Type}SeqView source;
        private final @NotNull ${Type}Function<? extends E> mapper;

        public MapToObj(@NotNull ${Type}SeqView source, @NotNull ${Type}Function<? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator().mapToObj(mapper);
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
        public boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
        }

        @Override
        public final E get(int index) {
            return mapper.apply(source.get(index));
        }
    }

    public static final class Sorted extends Abstract${Type}SeqView {
        private final ${Type}SeqView source;
        private ${PrimitiveType}[] sorted;

        @SuppressWarnings("unchecked")
        public Sorted(@NotNull ${Type}SeqView source) {
            this.source = source;
        }

        private void initSorted() {
            if (sorted == null) {
                ${PrimitiveType}[] arr = source.toArray();
                ${Type}Arrays.sort(arr);
                this.sorted = arr;
            }
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            initSorted();
            return ${Type}Arrays.iterator(sorted);
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
        public final ${PrimitiveType} get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
            initSorted();
            try {
                return sorted[index];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException(e.getMessage());
            }
        }

        @Override
        public final @NotNull ${Type}SeqView sorted() {
            return this;
        }
    }
}
