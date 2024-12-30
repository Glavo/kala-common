/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.mutable;

import kala.Conditions;
import kala.collection.base.Iterators;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import kala.collection.base.AbstractIterator;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public final class MutableLinkedList<E> extends AbstractMutableList<E> implements MutableStack<E>, MutableDeque<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 8463536184690478447L;

    private static final boolean enableAssertions = Boolean.getBoolean("kala.collection.mutable.MutableLinkedList.enableAssertions");

    private static final MutableListFactory<?, ?> FACTORY = (MutableListFactory<Object, MutableLinkedList<Object>> & Serializable) MutableLinkedList::new;

    private int len = 0;
    private Node<E> first = null;
    private Node<E> last = null;

    public MutableLinkedList() {
    }

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableLinkedList<E>> factory() {
        return MutableListFactory.cast(FACTORY);
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
        for (int i = 0; i < n; i++) {
            res.append(value);
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

    @Override
    public @NotNull String className() {
        return "MutableLinkedList";
    }

    @Override
    public @NotNull <U> CollectionFactory<U, ?, ? extends MutableList<U>> iterableFactory() {
        return MutableLinkedList.factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return first == null ? Iterators.empty() : new Itr<>(first);
    }

    public @NotNull Iterator<E> iterator(@NotNull Node<E> node) {
        assertInList(node);
        return new Itr<>(node);
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
        for (E e : this) {
            res.append(e);
        }
        return res;
    }

    private void assertInList(Node<E> node) {
        if (enableAssertions && getIndex(node) < 0) {
            throw new AssertionError(node + " not in the linked list");
        }
    }

    private void assertNotInList(Node<E> node) {
        if (enableAssertions && getIndex(node) >= 0) {
            throw new AssertionError(node + " is already in the linked list");
        }
    }

    Node<E> internalGetNode(int index) {
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

    void internalInsertBefore(@NotNull Node<E> node, E value) {
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

    void internalUnlink(@NotNull Node<E> node) {
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
        len--;
    }

    public Node<E> getNode(int index) {
        Objects.checkIndex(index, len);
        return internalGetNode(index);
    }

    public int getIndex(Node<E> node) {
        Node<E> n = first;
        for (int i = 0; i < len; i++) {
            if (node == n)
                return i;

            n = n.next;
        }
        return -1;
    }

    @Override
    public E get(@Index int index) {
        return internalGetNode(Indexes.checkElementIndex(index, len)).value;
    }

    @Override
    public void prepend(E value) {
        final Node<E> oldFirst = this.first;
        final Node<E> newNode = new Node<>(null, oldFirst, value);
        this.first = newNode;
        if (oldFirst == null) {
            this.last = newNode;
        } else {
            oldFirst.prev = newNode;
        }
        len++;
    }

    public void prepend(@NotNull Node<E> node) {
        assertNotInList(node);

        final Node<E> oldFirst = this.first;
        node.prev = null;
        node.next = oldFirst;
        this.first = node;
        if (oldFirst == null) {
            this.last = node;
        } else {
            oldFirst.prev = node;
        }
        len++;
    }

    public void prepend(@NotNull Node<E> node, E value) {
        node.setValue(value);
        prepend(node);
    }

    @Override
    public void append(E value) {
        final Node<E> oldLast = this.last;
        final Node<E> newNode = new Node<>(oldLast, null, value);
        this.last = newNode;
        if (oldLast == null) {
            first = newNode;
        } else {
            oldLast.next = newNode;
        }
        len++;
    }

    public void append(@NotNull Node<E> node) {
        assertNotInList(node);

        final Node<E> oldLast = this.last;
        node.prev = oldLast;
        node.next = null;
        this.last = node;
        if (oldLast == null) {
            this.first = node;
        } else {
            oldLast.next = node;
        }
        len++;
    }

    public void append(@NotNull Node<E> node, E value) {
        node.setValue(value);
        append(node);
    }

    public E removeFirst() {
        final Node<E> oldFirst = this.first;
        if (oldFirst == null) {
            throw new NoSuchElementException();
        }

        final Node<E> next = oldFirst.next;
        this.first = next;
        if (next == null) {
            this.last = null;
        } else {
            next.prev = null;
        }
        len--;
        return oldFirst.value;
    }

    public E removeLast() {
        final Node<E> oldLast = this.last;
        if (oldLast == null) {
            throw new NoSuchElementException();
        }

        final Node<E> prev = oldLast.prev;
        this.last = prev;
        if (prev == null) {
            this.first = null;
        } else {
            prev.next = null;
        }
        len--;
        return oldLast.value;
    }

    @Override
    public void insert(int index, E value) {
        Conditions.checkPositionIndex(index, len);

        if (index == len) {
            append(value);
        } else {
            internalInsertBefore(internalGetNode(index), value);
        }
    }

    public void insertBefore(@NotNull Node<E> node, E value) {
        assertInList(node);
        internalInsertBefore(node, value);
    }

    public void insertBefore(@NotNull Node<E> node, @NotNull Node<E> newNode) {
        assertInList(node);
        assertNotInList(newNode);

        final Node<E> prev = node.prev;
        newNode.prev = prev;
        newNode.next = node;
        node.prev = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
        len++;
    }

    public void insertBefore(@NotNull Node<E> node, @NotNull Node<E> newNode, E newValue) {
        newNode.setValue(newValue);
        insertBefore(node, newNode);
    }

    public void insertAfter(@NotNull Node<E> node, E value) {
        assertInList(node);

        final Node<E> next = node.next;
        final Node<E> newNode = new Node<>(node, next, value);
        node.next = newNode;
        if (next == null) {
            last = newNode;
        } else {
            next.prev = newNode;
        }
        len++;
    }

    public void insertAfter(@NotNull Node<E> node, @NotNull Node<E> newNode) {
        assertInList(node);
        assertNotInList(newNode);

        final Node<E> next = node.next;
        newNode.prev = node;
        newNode.next = next;
        node.next = newNode;
        if (next == null) {
            last = newNode;
        } else {
            next.prev = newNode;
        }
        len++;
    }

    public void insertAfter(@NotNull Node<E> node, @NotNull Node<E> newNode, E newValue) {
        newNode.setValue(newValue);
        insertAfter(node, newNode);
    }

    @Override
    public E removeAt(int index) {
        Objects.checkIndex(index, len);

        final Node<E> node = internalGetNode(index);
        final E value = node.value;
        internalUnlink(node);
        return value;
    }

    public void unlink(@NotNull Node<E> node) {
        assertInList(node);
        internalUnlink(node);
    }

    @Override
    public boolean remove(Object value) {
        Node<E> node = first;

        if (value == null) {
            while (node != null) {
                if (null == node.value) {
                    internalUnlink(node);
                    return true;
                }
                node = node.next;
            }
        } else {
            while (node != null) {
                if (value.equals(node.value)) {
                    internalUnlink(node);
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
        Objects.checkIndex(index, len);
        internalGetNode(index).value = newValue;
    }

    @Override
    public E getFirst() {
        final Node<E> first = this.first;
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    public Node<E> firstNode() {
        return first;
    }

    @Override
    public E getLast() {
        final Node<E> last = this.last;
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    public Node<E> lastNode() {
        return last;
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
        return this.getFirst();
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

    public @NotNull Iterator<E> reverseIterator(@NotNull Node<E> node) {
        assertInList(node);
        return new ReverseItr<>(node);
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        Objects.requireNonNull(action);
        Node<E> node = this.first;
        while (node != null) {
            action.accept(node.value);
            node = node.next;
        }
    }

    public void forEachNode(@NotNull Consumer<? super Node<E>> action) {
        Objects.requireNonNull(action);
        Node<E> node = this.first;
        while (node != null) {
            action.accept(node);
            node = node.next;
        }
    }

    public void forEachNodeIndexed(@NotNull IndexedConsumer<? super Node<E>> action) {
        Objects.requireNonNull(action);
        int idx = 0;
        Node<E> node = this.first;
        while (node != null) {
            action.accept(idx++, node);
            node = node.next;
        }
    }

    @Serial
    private Object writeReplace() {
        return new SerializationWrapper<>(factory(), this);
    }

    public static final class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, Node<E> next, E value) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
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
            next = (index == len) ? null : internalGetNode(index);
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
                internalInsertBefore(next, e);
            }
            cursor++;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            Node<E> lastReturnedNext = this.lastReturned;
            internalUnlink(lastReturned);
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
}
