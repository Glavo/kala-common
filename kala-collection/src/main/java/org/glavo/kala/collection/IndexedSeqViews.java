package org.glavo.kala.collection;

import org.glavo.kala.Conditions;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

final class IndexedSeqViews {
    static class Of<E, C extends IndexedSeq<E>> extends SeqViews.Of<E, C> implements IndexedSeqView<E> {
        Of(@NotNull C collection) {
            super(collection);
        }
    }

    static class Updated<E> extends SeqViews.Updated<E> implements IndexedSeqView<E> {
        Updated(@NotNull SeqView<E> source, int index, E newValue) {
            super(source, index, newValue);
        }
    }

    static class Mapped<E, T> extends SeqViews.Mapped<E, T> implements IndexedSeqView<E> {
        Mapped(@NotNull SeqView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            super(source, mapper);
        }
    }

    static class Slice<E> extends SeqViews.Slice<E> implements IndexedSeqView<E> {
        private final int size;

        Slice(@NotNull SeqView<E> source, int beginIndex, int endIndex) {
            super(source, beginIndex, endIndex);
            this.size = endIndex - beginIndex;
        }

        @Override
        public final boolean isEmpty() {
            return size == 0;
        }

        @Override
        public final int size() {
            return size;
        }

        @Override
        public final @Range(from = -1, to = Integer.MAX_VALUE) int knownSize() {
            return size;
        }

        @Override
        public final E get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            Conditions.checkElementIndex(index, size);
            return source.get(index + beginIndex);
        }

        @Override
        public final @Nullable E getOrNull(int index) {
            return index < 0 || index >= size
                    ? null
                    : source.get(index + beginIndex);
        }

        @Override
        public @NotNull Option<E> getOption(int index) {
            return index < 0 || index >= size
                    ? Option.none()
                    : Option.some(source.get(index + beginIndex));
        }
    }

    static class Drop<E> extends SeqViews.Drop<E> implements IndexedSeqView<E> {
        Drop(@NotNull SeqView<E> source, int n) {
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

    static class Take<E> extends SeqViews.Take<E> implements IndexedSeqView<E> {
        Take(@NotNull SeqView<E> source, int n) {
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

    static class Prepended<E> extends SeqViews.Prepended<E> implements IndexedSeqView<E> {
        Prepended(@NotNull SeqView<E> source, E value) {
            super(source, value);
        }
    }

    static class Appended<E> extends SeqViews.Appended<E> implements IndexedSeqView<E> {
        Appended(@NotNull SeqView<E> source, E value) {
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
