package kala.collection.internal.view;

import kala.collection.*;
import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.collection.base.ObjectArrays;
import kala.control.Option;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.primitive.IntObjTuple2;
import kala.tuple.primitive.PrimitiveTuple;
import kala.Conditions;
import kala.collection.mutable.DynamicArray;
import kala.comparator.Comparators;
import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public final class SeqViews {
    public static class Empty<E> extends Views.Empty<E> implements SeqView<E> {

        public static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public @NotNull SeqView<E> reversed() {
            return this;
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.empty();
        }

        @Override
        public E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            throw new IndexOutOfBoundsException("index: " + index);
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

    public static class Single<E> extends Views.Single<E> implements SeqView<E> {
        public Single(E value) {
            super(value);
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
        public @NotNull SeqView<E> updated(int index, E newValue) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index);
            }

            return new Single<>(newValue);
        }

        @Override
        public @NotNull SeqView<E> filterNotNull() {
            return value == null ? SeqView.empty() : this;
        }
    }

    public static class Of<E, C extends SeqLike<E>> extends Views.Of<E, C> implements SeqView<E> {
        public Of(@NotNull C source) {
            super(source);
        }

        @Override
        public @NotNull SeqIterator<E> seqIterator() {
            return source.seqIterator();
        }

        @Override
        public @NotNull SeqIterator<E> seqIterator(int index) {
            return source.seqIterator(index);
        }

        public E get(int index) {
            return source.get(index);
        }

        @Override
        public @Nullable E getOrNull(int index) {
            return source.getOrNull(index);
        }

        public @NotNull Option<E> getOption(int index) {
            return source.getOption(index);
        }

        public boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
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
        protected final @NotNull SeqView<E> source;
        protected final int beginIndex;
        protected final int endIndex;

        public Slice(@NotNull SeqView<E> source, int beginIndex, int endIndex) {
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

    public static class Updated<E> extends AbstractSeqView<E> {
        private final @NotNull SeqView<E> source;

        private final int index;

        private final E newValue;

        public Updated(@NotNull SeqView<E> source, int index, E newValue) {
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
        public final E get(int index) {
            if (index == this.index) {
                return newValue;
            }
            return source.get(index);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index == this.index) {
                return Option.some(newValue);
            }
            return source.getOption(index);
        }
    }

    public static class Drop<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqView<E> source;

        protected final int n;

        public Drop(@NotNull SeqView<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
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
            return source.drop(n + this.n);
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
        public final E get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("Index(" + index + ") < 0");
            }

            final SeqView<E> source = this.source;
            final int n = this.n;

            if (n <= 0) {
                return this.source.get(index);
            }

            final int size = Integer.max(source.size() - n, 0);
            Conditions.checkElementIndex(index, size);

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

    public static class DropWhile<E> extends AbstractSeqView<E> {

        private final @NotNull SeqView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public DropWhile(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.dropWhile(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().dropWhile(predicate);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().dropWhile(predicate);
        }
    }

    public static class Take<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqView<E> source;

        protected final @Range(from = 1, to = Integer.MAX_VALUE) int n;

        public Take(@NotNull SeqView<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
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
        @NotNull
        protected final SeqView<E> source;


        private final int n;
        private final int delta;

        public TakeLast(@NotNull SeqView<E> source, int n) {
            this.source = source;
            this.n = Integer.max(n, 0);
            this.delta = Integer.max(0, source.size() - Integer.max(0, n));
        }

        @Override
        public final E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
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

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            int k = source.knownSize();
            if (k == 0 || n <= 0) {
                return Iterators.empty();
            }
            if (n == Integer.MAX_VALUE) {
                return source.iterator();
            }
            if (k > 0) {
                return Iterators.drop(source.iterator(), Integer.max(k - n, 0));
            }
            return new AbstractIterator<E>() {
                Iterator<E> it = source.iterator();
                int len = -1;
                int pos = 0;
                DynamicArray<E> buf = null;

                private void init() {
                    if (buf != null) {
                        return;
                    }
                    buf = new DynamicArray<>(Integer.min(n, 256));
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
        private final @NotNull SeqView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public TakeWhile(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.takeWhile(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().takeWhile(predicate);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().takeWhile(predicate);
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

    public static class Prepended<E> extends AbstractSeqView<E> {

        private final @NotNull SeqView<E> source;

        private final E value;

        public Prepended(@NotNull SeqView<E> source, E value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.prepended(source.iterator(), value);
        }

        @Override
        public final E get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
            return index == 0 ? value : source.get(index - 1);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index < 0) {
                return Option.none();
            }
            return index == 0 ? Option.some(value) : source.getOption(index + 1);
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

    public static class Appended<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqView<E> source;

        protected final E value;

        public Appended(@NotNull SeqView<E> source, E value) {
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

    public static class Reversed<E> extends AbstractSeqView<E> {
        protected final @NotNull SeqView<E> source;

        public Reversed(@NotNull SeqView<E> source) {
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
        public final E get(int index) {
            return source.get(size() - 1 - index);
        }

        @Override
        public final @NotNull SeqView<E> reversed() {
            return source;
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return source.iterator();
        }
    }

    public static final class Filter<E> extends AbstractSeqView<E> {
        private final @NotNull SeqView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
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

    public static final class FilterNot<E> extends AbstractSeqView<E> {
        private final @NotNull SeqView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public FilterNot(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.filterNot(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().filter(predicate.negate());
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().filter(predicate.negate());
        }
    }

    public static final class FilterNotNull<E> extends AbstractSeqView<E> {
        private final @NotNull SeqView<E> source;

        public FilterNotNull(@NotNull SeqView<E> source) {
            this.source = source;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.filterNotNull(source.iterator());
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().filter(Predicates.isNotNull());
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().filter(Predicates.isNotNull());
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
        private final @NotNull SeqView<T> source;

        private final @NotNull Function<? super T, ? extends E> mapper;

        public Mapped(@NotNull SeqView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
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
        public final E get(int index) {
            return mapper.apply(source.get(index));
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return source.getOption(index).map(mapper);
        }
    }

    public static class MapIndexed<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<T> source;

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
        public final boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
        }

        @Override
        public final E get(int index) {
            return mapper.apply(index, source.get(index));
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return source.getOption(index).map(a -> mapper.apply(index, a));
        }
    }

    public static class MapNotNull<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<T> source;

        private final @NotNull Function<? super T, ? extends E> mapper;

        public MapNotNull(@NotNull SeqView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapNotNull(source.iterator(), mapper);
        }
    }

    public static class MapIndexedNotNull<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<T> source;
        private final @NotNull IndexedFunction<? super T, ? extends E> mapper;

        public MapIndexedNotNull(@NotNull SeqView<T> source, @NotNull IndexedFunction<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapIndexedNotNull(source.iterator(), mapper);
        }
    }

    public static final class MapMulti<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<T> source;
        private final @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper;

        public MapMulti(@NotNull SeqView<T> source, @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapMulti(source.iterator(), mapper);
        }
    }

    public static final class MapIndexedMulti<E, T> extends AbstractSeqView<E> {
        private final @NotNull SeqView<T> source;
        private final @NotNull IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper;

        public MapIndexedMulti(@NotNull SeqView<T> source, @NotNull IndexedBiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapIndexedMulti(source.iterator(), mapper);
        }
    }

    static abstract class WithIndexBase<E> extends AbstractSeqView<IntObjTuple2<E>> {
        protected final SeqLike<E> source;

        public WithIndexBase(SeqLike<E> source) {
            this.source = source;
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
        public final boolean isDefinedAt(int index) {
            return source.isDefinedAt(index);
        }
    }

    public static class WithIndex<E> extends WithIndexBase<E> {
        public WithIndex(SeqLike<E> source) {
            super(source);
        }

        @Override
        public @NotNull Iterator<IntObjTuple2<E>> iterator() {
            return Iterators.withIndex(source.iterator());
        }


        //region Positional Access Operations

        @Override
        public final IntObjTuple2<E> get(int index) {
            E e = source.get(index);
            return IntObjTuple2.of(index, e);
        }

        @Override
        public @Nullable IntObjTuple2<E> getOrNull(int index) {
            Option<E> opt = source.getOption(index);
            return opt.isEmpty() ? null : IntObjTuple2.of(index, opt.get());
        }

        @Override
        public @NotNull Option<IntObjTuple2<E>> getOption(int index) {
            Option<E> opt = source.getOption(index);
            return opt.isEmpty() ? Option.none() : Option.some(IntObjTuple2.of(index, opt.get()));
        }

        //endregion

        //region Reversal Operations

        @Override
        public @NotNull SeqView<IntObjTuple2<E>> reversed() {
            return new WithIndexReversed<>(source);
        }

        @Override
        public @NotNull Iterator<IntObjTuple2<E>> reverseIterator() {
            return reversed().iterator();
        }

        //endregion
    }

    public static class WithIndexReversed<E> extends WithIndexBase<E> {
        public WithIndexReversed(SeqLike<E> source) {
            super(source);
        }

        @Override
        public @NotNull Iterator<IntObjTuple2<E>> iterator() {
            final int sks = source.knownSize();
            if (sks == 0) {
                return Iterators.empty();
            } else if (sks > 0) {
                Iterator<E> it = source.reverseIterator();
                return new AbstractIterator<IntObjTuple2<E>>() {
                    private int idx = sks - 1;

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public IntObjTuple2<E> next() {
                        final E e = it.next();
                        return PrimitiveTuple.of(idx--, e);
                    }
                };

            } else {
                Iterator<E> it = source.iterator();
                if (!it.hasNext()) {
                    return Iterators.empty();
                }
                Object[] arr = ObjectArrays.from(it);

                return new AbstractIterator<IntObjTuple2<E>>() {
                    private int index = arr.length - 1;

                    @Override
                    public final boolean hasNext() {
                        return index >= 0;
                    }

                    @Override
                    public final IntObjTuple2<E> next() {
                        final int oldIndex = this.index;
                        if (oldIndex < 0) {
                            throw new NoSuchElementException();
                        }
                        IntObjTuple2<E> res = IntObjTuple2.of(oldIndex, (E) arr[oldIndex]);
                        this.index = oldIndex - 1;
                        return res;
                    }
                };
            }
        }

        //region Positional Access Operations

        @Override
        public final IntObjTuple2<E> get(int index) {
            final int size = source.size();
            Conditions.checkElementIndex(index, size);
            final int ridx = size - index - 1;
            return IntObjTuple2.of(ridx, source.get(ridx));
        }

        @Override
        public @Nullable IntObjTuple2<E> getOrNull(int index) {
            final int size = source.size();
            final int ridx = size - index - 1;
            return index >= 0 && index < size
                    ? null
                    : IntObjTuple2.of(ridx, source.get(ridx));
        }

        @Override
        public @NotNull Option<IntObjTuple2<E>> getOption(int index) {
            final int size = source.size();
            final int ridx = size - index - 1;
            return index >= 0 && index < size
                    ? Option.none()
                    : Option.some(IntObjTuple2.of(ridx, source.get(ridx)));
        }

        //endregion

        //region Reversal Operations

        @Override
        public @NotNull SeqView<IntObjTuple2<E>> reversed() {
            return new WithIndex<>(source);
        }

        @Override
        public @NotNull Iterator<IntObjTuple2<E>> reverseIterator() {
            return Iterators.withIndex(source.iterator());
        }

        //endregion
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
        public final E get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
            initSorted();
            try {
                return (E) sorted[index];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException(e.getMessage());
            }
        }

        @Override
        public final @Nullable E getOrNull(int index) {
            if (index < 0) {
                return null;
            }

            initSorted();
            Object[] sorted = this.sorted;
            if (index >= sorted.length) {
                return null;
            }
            return (E) sorted[index];
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index < 0) {
                return Option.none();
            }

            initSorted();
            Object[] sorted = this.sorted;
            return index >= sorted.length ? Option.none() : (Option<E>) Option.some(sorted[index]);
        }

        @Override
        public final @NotNull SeqView<E> sorted(Comparator<? super E> comparator) {
            if (comparator == Comparators.naturalOrder()) {
                comparator = null;
            }

            if (comparator == this.comparator) {
                return this;
            }
            if ((comparator == null && this.comparator == Comparators.reverseOrder())
                    || (comparator == Comparators.reverseOrder() && this.comparator == null)) {
                return reversed();
            }

            return new Sorted<>(source, comparator);
        }
    }

    public static class Zip<E, U> extends AbstractSeqView<Tuple2<E, U>> {
        private final @NotNull SeqLike<? extends E> source;
        private final @NotNull SeqLike<? extends U> other;

        public Zip(@NotNull SeqLike<? extends E> source, @NotNull SeqLike<? extends U> other) {
            this.source = source;
            this.other = other;
        }

        @Override
        public final @NotNull Iterator<Tuple2<E, U>> iterator() {
            return Iterators.zip(source.iterator(), other.iterator());
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
        public final @NotNull Tuple2<E, U> get(int index) {
            return Tuple.of(source.get(index), other.get(index));
        }

        @Override
        public final @NotNull Option<Tuple2<E, U>> getOption(int index) {
            final Option<? extends E> o1 = source.getOption(index);
            if (o1.isEmpty()) {
                return Option.none();
            }
            final Option<? extends U> o2 = other.getOption(index);
            if (o2.isEmpty()) {
                return Option.none();
            }
            return Option.some(Tuple.of(o1.get(), o2.get()));
        }

        @Override
        public final @NotNull SeqView<Tuple2<E, U>> reversed() {
            return (SeqView) source.view().reversed().zipView(other.view().reversed());
        }
    }
}
