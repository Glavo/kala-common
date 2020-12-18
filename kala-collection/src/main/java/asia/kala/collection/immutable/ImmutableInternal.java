package asia.kala.collection.immutable;

import asia.kala.control.Option;
import asia.kala.collection.mutable.AbstractBuffer;
import asia.kala.collection.mutable.LinkedBuffer;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

@ApiStatus.Internal
public final class ImmutableInternal {

    /**
     * Internal implementation of {@link LinkedBuffer}.
     *
     * @see LinkedBuffer
     */
    @SuppressWarnings("unchecked")
    public static abstract class LinkedBufferImpl<E> extends AbstractBuffer<E> {
        ImmutableList.Cons<E> first = null;
        ImmutableList.Cons<E> last = null;

        int len = 0;

        private boolean aliased = false;

        @SuppressWarnings("SpellCheckingInspection")
        private void ensureUnaliased() {
            if (aliased) {
                LinkedBufferImpl<E> buffer = new LinkedBuffer<>();
                buffer.appendAll(this);
                this.first = buffer.first;
                this.last = buffer.last;
                aliased = false;
            }
        }

        @Override
        public final E get(int index) {
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            if (index == len - 1) {
                return last.head;
            }

            return first.get(index);
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index < 0 || index >= len) {
                return Option.none();
            }
            if (index == len - 1) {
                return Option.some(last.head);
            }

            return Option.some(first.get(index));
        }

        @Override
        public final E first() {
            ImmutableList.Cons<E> first = this.first;
            if (first == null) {
                throw new NoSuchElementException();
            } else {
                return first.head;
            }
        }

        @Override
        public final E last() {
            ImmutableList.Cons<E> last = this.last;
            if (last == null) {
                throw new NoSuchElementException();
            } else {
                return last.head;
            }
        }

        @Override
        public final boolean isEmpty() {
            return len == 0;
        }

        @Override
        public final void append(E value) {
            ImmutableList.Cons<E> i = new ImmutableList.Cons<>(value, ImmutableList.nil());
            if (len == 0) {
                first = i;
            } else {
                last.tail = i;
            }
            last = i;
            ++len;
        }

        @Override
        public final void prepend(E value) {
            ensureUnaliased();
            if (len == 0) {
                append(value);
                return;
            }
            first = new ImmutableList.Cons<>(value, first);
            ++len;
        }

        @Override
        public void insert(int index, E value) {
            ensureUnaliased();
            if (index < 0 || index > len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            if (index == len) {
                append(value);
                return;
            }

            if (index == 0) {
                prepend(value);
                return;
            }
            ensureUnaliased();
            ImmutableList<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail();
            }

            ((ImmutableList.Cons<E>) i).tail = new ImmutableList.Cons<>(value, i.tail());
            ++len;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final E removeAt(int index) {
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            if (index == 0) {
                E v = first.head;
                if (len == 1) {
                    first = last = null;
                    aliased = false;
                } else {
                    first = (ImmutableList.Cons<E>) first.tail;
                }
                --len;
                return v;
            }

            ensureUnaliased();
            ImmutableList<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail();
            }
            E v = i.tail().head();
            ((ImmutableList.Cons<E>) i).tail = i.tail().tail();
            --len;
            return v;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void removeAt(int index, int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count: " + count);
            }
            if (index < 0 || index + count > len) {
                throw new IndexOutOfBoundsException(String.format("%d to %d is out of bounds", index, index + count));
            }

            if (count == 0) {
                return;
            }
            if (count == 1) {
                removeAt(index);
                return;
            }
            if (count == len) {
                clear();
                return;
            }
            if (index == 0) {
                int c = count;
                while (c-- > 0) {
                    first = (ImmutableList.Cons<E>) first.tail;
                }
                len -= count;
                return;
            }

            ensureUnaliased();
            ImmutableList<E> i = first;
            int c = 1;
            while (c++ != index) {
                i = i.tail();
            }

            ImmutableList<E> t = i.tail();
            c = count;
            while (c-- > 0) {
                t = t.tail();
            }

            ((ImmutableList.Cons<E>) i).tail = t;
            len -= count;
        }

        public final E removeFirst() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }

            E v = first.head;
            if (len == 1) {
                first = last = null;
                aliased = false;
            } else {
                first = (ImmutableList.Cons<E>) first.tail;
            }
            --len;
            return v;
        }

        public final void clear() {
            first = last = null;
            len = 0;
            aliased = false;
        }

        @Override
        public final @NotNull ImmutableList<E> toImmutableList() {
            aliased = true;
            return first;
        }

        @Override
        public final void set(int index, E newValue) {
            final int len = this.len;
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            ensureUnaliased();
            if (index == len - 1) {
                last.head = newValue;
                return;
            }

            ImmutableList<E> l = first;
            while (--index >= 0) {
                l = l.tail();
            }
            ((ImmutableList.Cons<E>) l).head = newValue;
        }

        @Override
        public final void sort(@NotNull Comparator<? super E> comparator) {
            if (len == 0) {
                return;
            }
            Object[] values = toArray();
            Arrays.sort(values, (Comparator<? super Object>) comparator);

            ensureUnaliased();
            ImmutableList.Cons<E> c = first;
            for (Object value : values) {
                //noinspection ConstantConditions
                c.head = (E) value;
                c = c.tail instanceof ImmutableList.Cons<?>
                        ? (ImmutableList.Cons<E>) c.tail
                        : null;
            }
        }

        @Override
        public final void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
            ImmutableList<E> n = first;
            if (n == null || n == ImmutableList.nil()) {
                return;
            }
            ensureUnaliased();
            while (n instanceof ImmutableList.Cons<?>) {
                ImmutableList.Cons<E> c = (ImmutableList.Cons<E>) n;
                c.head = mapper.apply(c.head);
                n = (ImmutableList<E>) c.tail;
            }
        }

        @Override
        public final void reverse() {
            if (len == 0) {
                return;
            }
            LinkedBufferImpl<E> newBuffer = new LinkedBuffer<>();
            for (E e : this) {
                newBuffer.prepend(e);
            }
            this.first = newBuffer.first;
            this.last = newBuffer.last;
            this.aliased = false;
        }

        @Override
        public final int size() {
            return len;
        }

        @Override
        public final int knownSize() {
            return len;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            if (len == 0) {
                return Iterators.empty();
            }
            return first.iterator();
        }
    }
}
