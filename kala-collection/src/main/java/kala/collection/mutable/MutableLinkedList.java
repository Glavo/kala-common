package kala.collection.mutable;

import kala.Conditions;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public final class MutableLinkedList<E> extends AbstractMutableList<E> implements MutableStack<E>, MutableDeque<E>, Serializable {
    private static final long serialVersionUID = 8463536184690478447L;

    private static final Factory<?> FACTORY = new Factory<>();

    private int len = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    public MutableLinkedList() {
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, MutableLinkedList<E>> factory() {
        return (MutableLinkedList.Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull MutableLinkedList<E> create() {
        return new MutableLinkedList<>();
    }

    @Contract("-> new")
    public static <E> @NotNull MutableLinkedList<E> of() {
        return new MutableLinkedList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E value1) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.append(value1);
        return res;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E value1, E value2) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.append(value1);
        res.append(value2);
        return res;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E value1, E value2, E value3) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        return res;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E value1, E value2, E value3, E value4) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        return res;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.append(value1);
        res.append(value2);
        res.append(value3);
        res.append(value4);
        res.append(value5);
        return res;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> from(E @NotNull [] values) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> from(@NotNull Iterable<? extends E> values) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        res.appendAll(values);
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> from(@NotNull Iterator<? extends E> iterator) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        while (iterator.hasNext()) {
            res.append(iterator.next());
        }
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableLinkedList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableLinkedList<E> fill(int n, E value) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        while (n-- > 0) {
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableLinkedList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        while (n-- > 0) {
            res.append(supplier.get());
        }
        return res;
    }

    public static <E> @NotNull MutableLinkedList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        for (int i = 0; i < n; i++) {
            res.append(init.apply(i));
        }
        return res;
    }

    public static <E> @NotNull MutableLinkedList<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableLinkedList<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            res.append(value);
        }
        return res;
    }

    //endregion

    Node<E> getNode(int index) {
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
    public @NotNull String className() {
        return "MutableLinkedList" ;
    }

    @Override
    public @NotNull <U> CollectionFactory<U, ?, ? extends MutableList<U>> iterableFactory() {
        return MutableLinkedList.factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        final Node<E> first = this.first;
        return first == null ? Iterators.empty() : new Itr<>(first);
    }

    @Override
    public @NotNull MutableListIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, len);
        return new SeqItr(index);
    }

    public @NotNull MutableStack<E> asMutableStack() {
        return this;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableLinkedList<E> clone() {
        MutableLinkedList<E> res = new MutableLinkedList<>();
        if (len != 0) {
            for (E e : this) {
                res.append(e);
            }
        }
        return res;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= len) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).value;
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
        len++;
    }

    @Override
    public void append(E value) {
        final Node<E> last = this.last;
        final Node<E> newNode = new Node<>(last, null, value);
        this.last = newNode;
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        len++;
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
        Conditions.checkPositionIndex(index, len);

        if (index == len) {
            append(value);
        } else {
            insertBefore(getNode(index), value);
        }
    }

    void insertBefore(Node<E> node, E value) {
        final Node<E> prev = node.prev;
        final Node<E> newNode = new Node<>(prev, node, value);
        node.prev = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
        len++;
    }

    @Override
    public E removeAt(int index) {
        Conditions.checkElementIndex(index, len);

        final Node<E> node = getNode(index);
        final E value = node.value;
        removeAt(node);
        return value;
    }

    void removeAt(@NotNull Node<E> node) {
        final Node<E> prev = node.prev;
        final Node<E> next = node.next;

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
        node.value = null;
        len--;
    }

    @Override
    public boolean remove(Object value) {
        Node<E> node = first;

        if (value == null) {
            while (node != null) {
                if (null == node.value) {
                    removeAt(node);
                    return true;
                }
                node = node.next;
            }
        } else {
            while (node != null) {
                if (value.equals(node.value)) {
                    removeAt(node);
                    return true;
                }
                node = node.next;
            }
        }

        return false;
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
        return last == null ? Iterators.empty() : new ReverseItr<>(last);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(len);
        Node<E> node = this.first;
        for (int i = 0; i < len; i++) {
            out.writeObject(node.value);
            node = node.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        clear();
        final int size = in.readInt();
        if (size == 0) {
            return;
        }

        final Node<E> first = new Node<>(null, null, (E) in.readObject());
        Node<E> last = first;

        for (int i = 1; i < size; i++) {
            Node<E> node = new Node<>(null, null, (E) in.readObject());
            last.next = node;
            last = node;
        }

        this.len = size;
        this.first = first;
        this.last = last;
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

        Itr(Node<E> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            final Node<E> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            this.node = node.next;
            return v;
        }
    }

    private static final class ReverseItr<E> extends AbstractIterator<E> {
        private Node<E> node;

        ReverseItr(Node<E> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            final Node<E> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            this.node = node.prev;
            return v;
        }
    }

    private final class SeqItr extends AbstractMutableListIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;

        SeqItr(int index) {
            super(index);
            next = (index == len) ? null : getNode(index);
        }

        @Override
        public boolean hasNext() {
            return cursor < len;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = next;
            next = next.next;
            cursor++;
            return lastReturned.value;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = next = (next == null) ? last : next.prev;
            cursor--;
            return lastReturned.value;
        }

        @Override
        public void add(E e) {
            lastReturned = null;
            if (next == null) {
                append(e);
            } else {
                insertBefore(next, e);
            }
            cursor++;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            Node<E> lastReturnedNext = this.lastReturned;
            removeAt(lastReturned);
            if (next == lastReturned) {
                next = lastReturnedNext;
            } else {
                cursor--;
            }
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.value = e;
        }
    }

    private static final class Factory<E> extends AbstractMutableListFactory<E, MutableLinkedList<E>> {
        @Override
        public MutableLinkedList<E> newBuilder() {
            return new MutableLinkedList<>();
        }
    }
}
