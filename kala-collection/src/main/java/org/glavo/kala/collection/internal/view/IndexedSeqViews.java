package org.glavo.kala.collection.internal.view;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.*;
import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.GenericArrays;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

@SuppressWarnings("ALL")
public final class IndexedSeqViews {
    public static class Empty<E> extends SeqViews.Empty<E> implements IndexedSeqView<E> {
        public static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public @NotNull SeqView<E> reversed() {
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> prepended(E value) {
            return new Single<>(value);
        }

        @Override
        public @NotNull IndexedSeqView<E> appended(E value) {
            return new Single<>(value);
        }

        @Override
        public @NotNull IndexedSeqView<E> slice(int beginIndex, int endIndex) {
            if (beginIndex != 0 || endIndex != 0) {
                Conditions.checkPositionIndices(beginIndex, endIndex, 0);
            }
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> sliceView(int beginIndex, int endIndex) {
            if (beginIndex != 0 || endIndex != 0) {
                Conditions.checkPositionIndices(beginIndex, endIndex, 0);
            }
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> drop(int n) {
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> take(int n) {
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> updated(int index, E newValue) {
            throw new IndexOutOfBoundsException("index :" + index);
        }

        @Override
        public @NotNull IndexedSeqView<IntObjTuple2<E>> withIndex() {
            return (IndexedSeqView<IntObjTuple2<E>>) this;
        }

        @Override
        public @NotNull <U> IndexedSeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return (IndexedSeqView<U>) this;
        }

        @Override
        public @NotNull <U> IndexedSeqView<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return (IndexedSeqView<U>) this;
        }
    }

    public static class Single<E> extends SeqViews.Single<E> implements IndexedSeqView<E> {
        public Single(E value) {
            super(value);
        }

        @Override
        public @NotNull IndexedSeqView<E> reversed() {
            return this;
        }

        @Override
        public @NotNull IndexedSeqView<E> updated(int index, E newValue) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index);
            }

            return new Single<>(newValue);
        }
    }

    public static class Of<E, C extends IndexedSeqLike<E>> extends SeqViews.Of<E, C> implements IndexedSeqView<E> {
        public Of(@NotNull C source) {
            super(source);
        }
    }

    public static class Concat<E> extends SeqViews.Concat<E> implements IndexedSeqView<E> {
        public Concat(@NotNull SeqLike<? extends E> seq1, @NotNull SeqLike<? extends E> seq2) {
            super(seq1, seq2);
        }

        @Override
        public final int knownSize() {
            return size();
        }

        @Override
        public E get(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }

            final SeqLike<? extends E> seq1 = this.seq1;
            final int ss1 = seq1.size();
            if (index < ss1) {
                return seq1.get(index);
            }

            final SeqLike<? extends E> seq2 = this.seq2;
            final int ss2 = seq2.size();
            if (index < ss1 + ss2) {
                return seq2.get(index - ss1);
            }

            throw new IndexOutOfBoundsException("index(" + index + ") > size(" + (ss1 + ss2) + ")");
        }

        @Override
        public @Nullable E getOrNull(int index) {
            if (index < 0) {
                return null;
            }

            final SeqLike<? extends E> seq1 = this.seq1;
            final int ss1 = seq1.size();
            if (index < ss1) {
                return seq1.get(index);
            }

            final SeqLike<? extends E> seq2 = this.seq2;
            final int ss2 = seq2.size();
            if (index < ss1 + ss2) {
                return seq2.get(index + ss1);
            }

            return null;
        }

        @Override
        public @NotNull Option<E> getOption(int index) {
            if (index < 0) {
                return Option.none();
            }

            final SeqLike<? extends E> seq1 = this.seq1;
            final int ss1 = seq1.size();
            if (index < ss1) {
                return Option.some(seq1.get(index));
            }

            final SeqLike<? extends E> seq2 = this.seq2;
            final int ss2 = seq2.size();
            if (index < ss1 + ss2) {
                return Option.some(seq2.get(index + ss1));
            }

            return Option.none();
        }
    }

    public static class Updated<E> extends SeqViews.Updated<E> implements IndexedSeqView<E> {
        public Updated(@NotNull SeqView<E> source, int index, E newValue) {
            super(source, index, newValue);
        }
    }

    public static class Mapped<E, T> extends SeqViews.Mapped<E, T> implements IndexedSeqView<E> {
        public Mapped(@NotNull SeqView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            super(source, mapper);
        }
    }

    public static class Slice<E> extends SeqViews.Slice<E> implements IndexedSeqView<E> {

        public Slice(@NotNull SeqView<E> source, int beginIndex, int endIndex) {
            super(source, beginIndex, endIndex);
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return this.new Itr();
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return this.new ReverseItr();
        }

        @Override
        public final E get(int index) {
            Conditions.checkElementIndex(index, size());
            return source.get(index + beginIndex);
        }

        @Override
        public final @Nullable E getOrNull(int index) {
            return index < 0 || index >= size()
                    ? null
                    : source.get(index + beginIndex);
        }

        @Override
        public @NotNull Option<E> getOption(int index) {
            return index < 0 || index >= size()
                    ? Option.none()
                    : Option.some(source.get(index + beginIndex));
        }

        private final class Itr extends AbstractIterator<E> {
            private int idx = beginIndex;

            @Override
            public final boolean hasNext() {
                return idx < endIndex;
            }

            @Override
            public final E next() {
                if (idx >= endIndex) {
                    throw new NoSuchElementException();
                }
                return get(idx++);
            }
        }

        private final class ReverseItr extends AbstractIterator<E> {
            private int idx = endIndex - 1;

            @Override
            public final boolean hasNext() {
                return idx >= beginIndex;
            }

            @Override
            public E next() {
                return get(idx--);
            }
        }
    }

    public static class Drop<E> extends SeqViews.Drop<E> implements IndexedSeqView<E> {
        public Drop(@NotNull SeqView<E> source, @Range(from = 1, to = Integer.MAX_VALUE) int n) {
            super(source, n);
        }

        @Override
        public final E get(int index) {
            if (n <= 0) {
                return source.get(index);
            }
            int s = source.size();
            if (n >= s) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            return source.get(index + n);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (n <= 0) {
                return source.getOption(index);
            }

            return source.getOption(index + n);
        }

        @Override
        public @NotNull IndexedSeqView<E> drop(int n) {
            if (n <= 0) {
                return this;
            }

            final int nn = this.n + n;
            final int ss = source.size();
            if (nn >= ss) {
                return IndexedSeqView.empty();
            }
            return new Drop<>(source, nn);
        }
    }

    public static class Take<E> extends SeqViews.Take<E> implements IndexedSeqView<E> {
        public Take(@NotNull SeqView<E> source, int n) {
            super(source, n);
        }

        @Override
        public final int size() {
            return n;
        }

        @Override
        public final int knownSize() {
            return n;
        }

        @Override
        public final E get(int index) {
            if (index < 0 || index >= n) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            return source.get(index);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return index >= 0 && index < size()
                    ? source.getOption(index)
                    : Option.none();
        }

        @Override
        public @NotNull IndexedSeqView<E> take(int n) {
            if (n <= 0) {
                return IndexedSeqView.empty();
            }
            if (n >= this.n) {
                return this;
            }
            return new Take<>(source, n);
        }
    }

    public static class Prepended<E> extends SeqViews.Prepended<E> implements IndexedSeqView<E> {
        public Prepended(@NotNull SeqView<E> source, E value) {
            super(source, value);
        }
    }

    public static class Appended<E> extends SeqViews.Appended<E> implements IndexedSeqView<E> {
        public Appended(@NotNull SeqView<E> source, E value) {
            super(source, value);
        }

        @Override
        public final E get(int index) {
            if (index == source.size()) {
                return value;
            }
            return source.get(index);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index == source.size()) {
                return Option.some(value);
            }
            return source.getOption(index);
        }
    }

    public static class WithIndex<E> extends SeqViews.WithIndex<E> implements IndexedSeqView<IntObjTuple2<E>> {
        public WithIndex(SeqLike<E> source) {
            super(source);
        }
    }

    public static class WithIndexReversed<E> extends SeqViews.WithIndexReversed<E> implements IndexedSeqView<IntObjTuple2<E>> {
        public WithIndexReversed(SeqLike<E> source) {
            super(source);
        }
    }

    public static class Zip<E, U> extends SeqViews.Zip<E, U>
            implements IndexedSeqView<Tuple2<E, U>> {
        public Zip(@NotNull SeqLike<? extends E> source, @NotNull SeqLike<? extends U> other) {
            super(source, other);
        }
    }

    @SuppressWarnings("unchecked")
    public static class OfArraySlice<E> extends AbstractIndexedSeqView<E> {
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
        public final E get(int index) {
            Conditions.checkElementIndex(index, size());
            return (E) array[index + beginIndex];
        }

        @Override
        public final @Nullable E getOrNull(int index) {
            return index < 0 || index >= size()
                    ? null
                    : (E) array[index + beginIndex];
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return index < 0 || index >= size()
                    ? Option.none()
                    : Option.some((E) array[index + beginIndex]);
        }

        @Override
        public @NotNull IndexedSeqView<E> slice(int beginIndex, int endIndex) {
            Conditions.checkPositionIndices(beginIndex, endIndex, size());
            final int ns = endIndex - beginIndex;
            switch (ns) {
                case 0:
                    return IndexedSeqView.empty();
                case 1:
                    return new Single<>((E) array[this.beginIndex + beginIndex]);
            }
            return new OfArraySlice<>(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
        }

        @Override
        public @NotNull IndexedSeqView<E> sliceView(int beginIndex, int endIndex) {
            return slice(beginIndex, endIndex);
        }

        @Override
        public @NotNull IndexedSeqView<E> drop(int n) {
            if (n <= 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return IndexedSeqView.empty();
            }

            return new OfArraySlice<>(array, beginIndex + n, endIndex);
        }

        @Override
        public @NotNull IndexedSeqView<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }

            final int size = this.size();
            if (n >= size) {
                return IndexedSeqView.empty();
            }

            return new OfArraySlice<>(array, beginIndex, endIndex - n);
        }

        @Override
        public @NotNull IndexedSeqView<E> take(int n) {
            if (n <= 0) {
                return IndexedSeqView.empty();
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new Single<>((E) array[beginIndex]);
            }
            return new OfArraySlice<>(array, beginIndex, beginIndex + n);
        }

        @Override
        public @NotNull IndexedSeqView<E> takeLast(int n) {
            if (n <= 0) {
                return IndexedSeqView.empty();
            }
            final int size = this.size();
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new Single<>((E) array[beginIndex]);
            }
            return new OfArraySlice<>(array, endIndex - n, endIndex);
        }
    }
}
