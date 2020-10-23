package asia.kala.collection;

import asia.kala.LazyValue;
import asia.kala.collection.mutable.ArrayBuffer;
import asia.kala.control.Option;
import asia.kala.annotations.Covariant;
import asia.kala.function.IndexedConsumer;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.*;

final class SeqViews {
    static class Of<@Covariant E, C extends Seq<E>> extends Views.Of<E, C> implements SeqView<E> {
        Of(@NotNull C collection) {
            super(collection);
        }

        public E get(int index) {
            return collection.get(index);
        }

        @NotNull
        public Option<E> getOption(int index) {
            return collection.getOption(index);
        }

        public boolean isDefinedAt(int index) {
            return collection.isDefinedAt(index);
        }

        public int indexOf(Object value) {
            return collection.indexOf(value);
        }

        public int indexOf(Object value, int from) {
            return collection.indexOf(value, from);
        }

        public int indexWhere(@NotNull Predicate<? super E> predicate) {
            return collection.indexWhere(predicate);
        }

        public int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
            return collection.indexWhere(predicate, from);
        }

        public int lastIndexOf(Object value) {
            return collection.lastIndexOf(value);
        }

        public int lastIndexOf(Object value, int end) {
            return collection.lastIndexOf(value, end);
        }

        public int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
            return collection.lastIndexWhere(predicate);
        }

        public int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
            return collection.lastIndexWhere(predicate, end);
        }

        public void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            collection.forEachIndexed(action);
        }

        @NotNull
        @Override
        public Iterator<E> reverseIterator() {
            return collection.reverseIterator();
        }
    }

    static class Updated<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<E> source;

        private final int index;

        private final E newValue;

        Updated(@NotNull SeqView<E> source, int index, E newValue) {
            assert source != null;

            this.source = source;
            this.index = index;
            this.newValue = newValue;
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final E get(int index) {
            if (index == this.index) {
                return newValue;
            }
            return source.get(index);
        }

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
            if (index == this.index) {
                return Option.some(newValue);
            }
            return source.getOption(index);
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
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
    }

    static class Drop<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;

        protected final int n;

        Drop(@NotNull SeqView<E> source, int n) {
            assert source != null;

            this.source = source;
            this.n = n;
        }

        @Override
        public final int size() {
            int s = source.size();
            if (n <= 0) {
                return s;
            }
            if (n >= s) {
                return 0;
            }
            return s - n;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.drop(source.iterator(), n);
        }
    }

    static class DropLast<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;


        private final int n;
        private final int len;

        DropLast(@NotNull SeqView<E> source, int n) {
            this.source = source;
            this.n = Integer.max(n, 0);
            this.len = Integer.max(source.size() - this.n, 0);
        }

        @Override
        public final E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException();
            }
            return source.get(index);
        }

        @Override
        public final int size() {
            return len;
        }

        @Override
        public final int knownSize() {
            return len;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            if (n <= 0) {
                return source.iterator();
            }
            return Iterators.take(source.iterator(), len);
        }
    }

    static class DropWhile<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<E> source;

        @NotNull
        private final Predicate<? super E> predicate;

        DropWhile(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            assert source != null;
            assert predicate != null;

            this.source = source;
            this.predicate = predicate;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.dropWhile(source.iterator(), predicate);
        }
    }

    static class Take<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;

        private final int n;

        Take(@NotNull SeqView<E> source, int n) {
            this.source = source;
            this.n = n;
        }

        @Override
        public final int size() {
            if (n <= 0) {
                return 0;
            }

            return Integer.min(n, source.size());
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.take(source.iterator(), n);
        }
    }

    static class TakeLast<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;


        private final int n;
        private final int delta;

        TakeLast(@NotNull SeqView<E> source, int n) {
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
                ArrayBuffer<E> buf = null;

                private void init() {
                    if (buf != null) {
                        return;
                    }
                    buf = new ArrayBuffer<>(Integer.min(n, 256));
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

    static class TakeWhile<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<E> source;

        @NotNull
        private final Predicate<? super E> predicate;

        TakeWhile(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.takeWhile(source.iterator(), predicate);
        }
    }

    static class Concat<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final Seq<E> seq1;

        @NotNull
        private final Seq<E> seq2;

        Concat(@NotNull Seq<E> seq1, @NotNull Seq<E> seq2) {
            this.seq1 = seq1;
            this.seq2 = seq2;
        }

        @Override
        public final int size() {
            return seq1.size() + seq2.size();
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public final Iterator<E> iterator() {
            return Iterators.concat(seq1.iterator(), seq1.iterator());
        }
    }

    static class Prepended<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<E> source;

        private final E value;

        Prepended(@NotNull SeqView<E> source, E value) {
            this.source = source;
            this.value = value;
        }

        @Override
        public final E get(int index) {
            if (index == 0) {
                return value;
            }
            return source.get(index + 1);
        }

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
            if (index == 0) {
                return Option.some(value);
            }
            return source.getOption(index + 1);
        }

        @Override
        public final int size() {
            return source.size() + 1;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.prepended(source.iterator(), value);
        }
    }

    static class Appended<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;

        protected final E value;

        Appended(@NotNull SeqView<E> source, E value) {
            assert source != null;

            this.source = source;
            this.value = value;
        }

        @Override
        public final int size() {
            return source.size() + 1;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.appended(source.iterator(), value);
        }
    }

    static class Reversed<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        protected final SeqView<E> source;

        Reversed(@NotNull SeqView<E> source) {
            this.source = source;
        }

        @Override
        public final E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            return source.get(size() - 1 - index);
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        @Range(from = -1L, to = 2147483647L)
        public final int knownSize() {
            return source.knownSize();
        }

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @NotNull
        @Override
        public final SeqView<E> reversed() {
            return source;
        }

        @NotNull
        @Override
        public final Iterator<E> reverseIterator() {
            return source.iterator();
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return source.reverseIterator();
        }
    }

    static class Mapped<@Covariant E, T> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<T> source;

        @NotNull
        private final Function<? super T, ? extends E> mapper;

        public Mapped(@NotNull SeqView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final E get(int index) {
            return mapper.apply(source.get(index));
        }

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
            return source.getOption(index).map(mapper);
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.map(source.iterator(), mapper);
        }
    }

    static final class Filter<@Covariant E> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<E> source;

        @NotNull
        private final Predicate<? super E> predicate;

        public Filter(@NotNull SeqView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }
    }

    static final class FlatMapped<@Covariant E, T> extends AbstractSeqView<E> {
        @NotNull
        private final SeqView<? extends T> source;
        @NotNull
        private final Function<? super T, ? extends Iterable<? extends E>> mapper;

        public FlatMapped(
                @NotNull SeqView<? extends T> source,
                @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.concat(source.map(it -> mapper.apply(it).iterator()));
        }
    }

    static final class Sorted<@Covariant E> extends AbstractSeqView<E> {
        private final SeqView<E> source;
        private final LazyValue<Seq<E>> sortedSeq;

        @SuppressWarnings("unchecked")
        Sorted(@NotNull SeqView<E> source, @NotNull Comparator<? super E> comparator) {
            this.source = source;
            this.sortedSeq = LazyValue.of(() -> {
                Object[] arr = source.toArray();
                Arrays.sort(arr, (Comparator<? super Object>) comparator);
                return ArraySeq.wrap((E[]) arr);
            });
        }

        @Override
        public final E get(int index) {
            return sortedSeq.get().get(index);
        }

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
            return sortedSeq.get().getOption(index);
        }

        @Override
        public final int size() {
            if (sortedSeq.isReady()) {
                return sortedSeq.get().size();
            }
            return source.size();
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return sortedSeq.get().iterator();
        }
    }
}
