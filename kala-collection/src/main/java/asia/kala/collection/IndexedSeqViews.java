package asia.kala.collection;

import asia.kala.control.Option;
import org.jetbrains.annotations.NotNull;

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

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
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

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
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

        @NotNull
        @Override
        public final Option<E> getOption(int index) {
            if (index == source.size()) {
                return Option.some(value);
            }
            return source.getOption(index);
        }
    }
}
