package kala.collection.mutable;

import kala.collection.base.Iterators;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collector;

@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public final class DynamicDoubleLinkedSeq<E>
        extends AbstractDynamicSeq<E> implements DynamicSeqOps<E, DynamicDoubleLinkedSeq<?>, DynamicDoubleLinkedSeq<E>>, MutableStack<E>, MutableQueue<E> {

    private static final Factory<?> FACTORY = new Factory<>();

    private int len = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    public DynamicDoubleLinkedSeq() {
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, DynamicDoubleLinkedSeq<E>> factory() {
        return (DynamicDoubleLinkedSeq.Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> create() {
        return new DynamicDoubleLinkedSeq<>();
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of() {
        return new DynamicDoubleLinkedSeq<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E value1) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.append(value1);
        return res;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E value1, E value2) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.append(value1);
        res.append(value2);
        return res;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E value1, E value2, E value3) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        return res;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        return res;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        res.append(value5);
        return res;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> from(E @NotNull [] values) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicDoubleLinkedSeq<E> from(@NotNull Iterator<? extends E> iterator) {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        while (iterator.hasNext()) {
            res.append(iterator.next());
        }
        return res;
    }

    //endregion

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

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "DynamicDoubleLinkedSeq";
    }

    @Override
    public @NotNull <U> CollectionFactory<U, ?, ? extends DynamicSeq<U>> iterableFactory() {
        return DynamicDoubleLinkedSeq.factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        final Node<E> first = this.first;
        return first == null ? Iterators.empty() : new Itr<>(first);
    }

    @Override
    public @NotNull DynamicSeqEditor<E, DynamicDoubleLinkedSeq<E>> edit() {
        return new DynamicSeqEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull DynamicDoubleLinkedSeq<E> clone() {
        DynamicDoubleLinkedSeq<E> res = new DynamicDoubleLinkedSeq<>();
        if (len != 0) {
            for (E e : this) {
                res.append(e);
            }
        }
        return res;
    }

    //endregion

    @Override
    public E get(int index) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).value;
    }

    @Override
    public void append(E value) {
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
    public void prepend(E value) {
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
    public void enqueue(E value) {
        prepend(value);
    }

    @Override
    public E dequeue() {
        return removeLast();
    }

    public E removeFirst() {
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

    public E removeLast() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException();
        }

        final Node<E> prev = last.prev;
        this.last = prev;
        if (prev == null) {
            this.first = null;
        } else {
            prev.next = null;
        }
        --len;
        return last.value;
    }

    @Override
    public void insert(int index, E value) {
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
    public E removeAt(int index) {
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
    public void clear() {
        this.len = 0;
        this.first = null;
        this.last = null;
    }

    @Override
    public void set(int index, E newValue) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        getNode(index).value = newValue;
    }

    @Override
    public E first() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    @Override
    public E last() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    @Override
    public int size() {
        return len;
    }

    @Override
    public int knownSize() {
        return len;
    }

    @Override
    public void push(E value) {
        prepend(value);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return first();
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        Node<E> node = this.first;
        while (node != null) {
            node.value = operator.apply(node.value);
            node = node.next;
        }
    }

    @Override
    public void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        Node<E> node = this.first;
        int idx = 0;
        while (node != null) {
            node.value = operator.apply(idx++, node.value);
            node = node.next;
        }
    }

    @Override
    public void reverse() {
        final int size = this.len;
        if (size == 0) {
            return;
        }
        int limit = size / 2;
        Node<E> f = this.first;
        Node<E> l = this.last;
        for (int i = 0; i < limit; i++) {
            E tmp = f.value;
            f.value = l.value;
            l.value = tmp;

            f = f.next;
            l = l.prev;
        }
    }

    @Override
    public @NotNull Iterator<E> reverseIterator() {
        final Node<E> last = this.last;
        return last == null ? Iterators.empty() : new ReverseItr<>(last);
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
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
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
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            final Node<E> node = this.node;
            if (this.node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            this.node = node.prev;
            return v;
        }
    }

    private static final class Factory<E> extends AbstractDynamicSeqFactory<E, DynamicDoubleLinkedSeq<E>> {
        @Override
        public DynamicDoubleLinkedSeq<E> newBuilder() {
            return new DynamicDoubleLinkedSeq<>();
        }
    }
}
