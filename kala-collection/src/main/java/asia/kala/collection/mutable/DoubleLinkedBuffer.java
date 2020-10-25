package asia.kala.collection.mutable;

import asia.kala.collection.AbstractIterator;
import asia.kala.factory.CollectionFactory;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public final class DoubleLinkedBuffer<E>
        extends AbstractBuffer<E> implements BufferOps<E, DoubleLinkedBuffer<?>, DoubleLinkedBuffer<E>>, MutableStack<E> {

    private static final Factory<?> FACTORY = new Factory<>();

    private int len = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    public DoubleLinkedBuffer() {
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, DoubleLinkedBuffer<E>> factory() {
        return (DoubleLinkedBuffer.Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of() {
        return new DoubleLinkedBuffer<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E value1) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.append(value1);
        return res;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E value1, E value2) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.append(value1);
        res.append(value2);
        return res;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E value1, E value2, E value3) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        return res;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E value1, E value2, E value3, E value4) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        return res;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        res.append(value5);
        return res;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> from(E @NotNull [] values) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> from(@NotNull Iterable<? extends E> values) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DoubleLinkedBuffer<E> from(@NotNull Iterator<? extends E> iterator) {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        while (iterator.hasNext()) {
            res.append(iterator.next());
        }
        return res;
    }

    private Node<E> getNode(int index) {
        final int len = this.len;
        Node<E> x;
        if (index < (len >> 1)) {
            x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
        } else {
            x = last;
            for (int i = len - 1; i > index; i--) {
                x = x.prev;
            }
        }
        return x;
    }

    @Override
    public final E get(int index) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).value;
    }

    @Override
    public final void append(E value) {
        final Node<E> last = this.last;
        final Node<E> nn = new Node<>(last, null, value);
        this.last = nn;
        if (last == null) {
            first = nn;
        } else {
            last.next = nn;
        }
        len++;
    }

    @Override
    public final void prepend(E value) {
        final Node<E> first = this.first;
        final Node<E> newNode = new Node<>(null, first, value);
        this.first = newNode;
        if (first == null) {
            last = newNode;
        } else {
            first.prev = newNode;
        }
        ++len;
    }

    @Override
    public final void insert(int index, E value) {
        final int len = this.len;
        if (index < 0 || index > this.len) {
            throw new IndexOutOfBoundsException();
        }

        if (index == len) {
            append(value);
        } else {
            final Node<E> node = getNode(index);
            final Node<E> prev = node.prev;
            final Node<E> newNode = new Node<>(prev, node, value);
            if (prev == null) {
                first = newNode;
            } else {
                prev.next = newNode;
            }
            ++this.len;
        }
    }

    @Override
    public final E remove(int index) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        final Node<E> node = getNode(index);
        E res = node.value;
        final Node<E> next = node.next;
        final Node<E> prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        --len;
        return res;
    }

    @Override
    public final void clear() {
        this.len = 0;
        this.first = null;
        this.last = null;
    }

    @Override
    public final void set(int index, E newValue) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        getNode(index).value = newValue;
    }

    @Override
    public final E first() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    @Override
    public final E last() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
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
    public final void push(E value) {
        prepend(value);
    }

    @Override
    public final E pop() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException();
        }

        final Node<E> next = first.next;
        this.first = next;
        if (next == null) {
            this.last = null;
        } else {
            next.prev = null;
        }
        --len;
        return first.value;
    }

    @Override
    public final E peek() {
        return first();
    }

    @Override
    public final boolean isEmpty() {
        return len == 0;
    }

    @Override
    public final @NotNull BufferEditor<E, DoubleLinkedBuffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        final Node<E> first = this.first;
        return first == null ? Iterators.empty() : new Itr<>(first);
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        final Node<E> last = this.last;
        return last == null ? Iterators.empty() : new ReverseItr<>(last);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull DoubleLinkedBuffer<E> clone() {
        DoubleLinkedBuffer<E> res = new DoubleLinkedBuffer<>();
        if (len != 0) {
            for (E e : this) {
                res.append(e);
            }
        }
        return res;
    }

    @Override
    public final String className() {
        return "DoubleLinkedBuffer";
    }

    @Override
    public final @NotNull <U> CollectionFactory<U, ?, ? extends Buffer<U>> iterableFactory() {
        return DoubleLinkedBuffer.factory();
    }

    private static final class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, Node<E> next, E value) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }
    }

    private static final class Itr<E> extends AbstractIterator<E> {
        private Node<E> node;

        private Itr(Node<E> node) {
            this.node = node;
        }


        @Override
        public final boolean hasNext() {
            return node != null;
        }

        @Override
        public final E next() {
            final Node<E> node = this.node;
            if (this.node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            this.node = node.next;
            return v;
        }
    }

    private static final class ReverseItr<E> extends AbstractIterator<E> {
        private Node<E> node;

        private ReverseItr(Node<E> node) {
            this.node = node;
        }

        @Override
        public final boolean hasNext() {
            return node != null;
        }

        @Override
        public final E next() {
            final Node<E> node = this.node;
            if (this.node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            this.node = node.prev;
            return v;
        }
    }

    private static final class Factory<E> extends AbstractBufferFactory<E, DoubleLinkedBuffer<E>> {
        @Override
        public final DoubleLinkedBuffer<E> newBuilder() {
            return new DoubleLinkedBuffer<>();
        }
    }
}
