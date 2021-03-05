package org.glavo.kala.collection.internal.view;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.IndexedSeqView;
import org.glavo.kala.collection.SeqLike;
import org.glavo.kala.collection.SeqView;
import org.glavo.kala.collection.internal.view.SeqViews;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

public final class IndexedSeqViews {

    public static class Of<E, C extends IndexedSeq<E>> extends SeqViews.Of<E, C> implements IndexedSeqView<E> {
        public Of(@NotNull C source) {
            super(source);
        }
    }

    public static class Concat<E> extends SeqViews.Concat<E> implements IndexedSeqView<E> {
        public Concat(@NotNull SeqLike<? extends E> seq1, @NotNull SeqLike<? extends E> seq2) {
            super(seq1, seq2);
        }

        @Override
        public int knownSize() {
            return seq1.size() + seq2.size();
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
                return seq2.get(index + ss1);
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
    }

    public static class Drop<E> extends SeqViews.Drop<E> implements IndexedSeqView<E> {
        public Drop(@NotNull SeqView<E> source, int n) {
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
    }

    public static class Take<E> extends SeqViews.Take<E> implements IndexedSeqView<E> {
        public Take(@NotNull SeqView<E> source, int n) {
            super(source, n);
        }

        @Override
        public final E get(int index) {
            if (index >= 0 && index < size()) {
                return source.get(index);
            }
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index >= 0 && index < size()) {
                return source.getOption(index);
            }

            return Option.none();
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
}
