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
package kala.collection.immutable;

import kala.annotations.Covariant;
import kala.collection.base.AbstractIterator;
import kala.collection.base.Iterators;
import kala.collection.mutable.AbstractMutableList;
import kala.collection.mutable.MutableSinglyLinkedList;
import kala.function.*;
import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableLinkedSeq<E> extends AbstractImmutableSeq<E> implements Serializable {
    private static final Factory<?> FACTORY = new Factory<>();

    private static final Node<?> NIL_NODE = new Node<>();
    private static <E> @NotNull Node<E> nilNode() {
        return (Node<E>) NIL_NODE;
    }

    public static final ImmutableLinkedSeq<?> EMPTY = new ImmutableLinkedSeq<>(NIL_NODE, 0);

    private final Node<E> node;
    private final int size;

    ImmutableLinkedSeq(Node<E> node, int size) {
        this.node = node;
        this.size = size;
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    public static <E> ImmutableLinkedSeq<E> narrow(ImmutableLinkedSeq<? extends E> seq) {
        return (ImmutableLinkedSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableLinkedSeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> ImmutableLinkedSeq<E> empty() {
        return (ImmutableLinkedSeq<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1) {
        return new ImmutableLinkedSeq<>(new Node<>(value1, nilNode()), 1);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2) {
        return new ImmutableLinkedSeq<>(new Node<>(value1, new Node<>(value2, nilNode())), 2);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableLinkedSeq<>(new Node<>(value1, new Node<>(value2, new Node<>(value3, nilNode()))), 3);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableLinkedSeq<>(new Node<>(value1, new Node<>(value2, new Node<>(value3, new Node<>(value4, nilNode())))), 4);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableLinkedSeq<>(new Node<>(value1, new Node<>(value2, new Node<>(value3, new Node<>(value4, new Node<>(value5, nilNode()))))), 5);
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableLinkedSeq<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(E @NotNull [] values) {
        final int size = values.length;
        if (size == 0) return empty();

        Node<E> node = nilNode();

        for (int i = values.length - 1; i >= 0; i--) {
            node = new Node<>(values[i], node);
        }

        return new ImmutableLinkedSeq<>(node, size);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull java.util.List<? extends E> values) {
        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableLinkedSeq<?>) return ((ImmutableLinkedSeq<E>) values);

        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) // implicit null check of it
            return empty();

        final Node<E> res = new Node<>(it.next());
        Node<E> t = res;
        int c = 1;
        while (it.hasNext()) {
            t = (t.tail = new Node<>(it.next()));
            c++;
        }
        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, c);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        Node<E> res = nilNode();
        for (int i = 0; i < n; i++) {
            res = new Node<>(value, res);
        }
        return new ImmutableLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        final Node<E> res = new Node<>(init.apply(0));
        Node<E> t = res;

        for (int i = 1; i < n; i++) {
            Node<E> nl = new Node<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableSinglyLinkedList<E> builder = new MutableSinglyLinkedList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            builder.append(value);
        }
        return builder.toImmutableLinkedSeq();
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableSinglyLinkedList<E> builder = new MutableSinglyLinkedList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            builder.append(value);
        }
        return builder.toImmutableLinkedSeq();
    }


    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableLinkedSeq";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableLinkedSeq<U>> iterableFactory() {
        return ImmutableLinkedSeq.factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        if (node == NIL_NODE) {
            return Iterators.empty();
        }
        if (node.tail == NIL_NODE) {
            return Iterators.of(node.head);
        }
        return new NodeItr<>(node);
    }

    public @NotNull Iterator<E> iterator(@Index int beginIndex) {
        beginIndex = Indexes.checkPositionIndex(beginIndex, size);

        if (beginIndex == 0) return iterator();
        if (beginIndex == size) return Iterators.empty();

        Node<E> node = this.node;
        for (int i = 0; i < beginIndex; i++) {
            node = node.tail;
        }
        return new NodeItr<>(node);
    }

    //region Size Info

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int knownSize() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public E get(@Index int index) {
        index = Indexes.checkIndex(index, size);

        Node<E> list = this.node;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return list.head;
    }

    //endregion

    //region Reversal Operations

    @Override
    public @NotNull ImmutableLinkedSeq<E> reversed() {
        if (size == 0 || size == 1) {
            return this;
        } else {
            Node<? extends E> node = this.node;
            Node<E> res = (Node<E>) NIL_NODE;
            while (node != NIL_NODE) {
                res = new Node<>(node.head, res);
                node = node.tail;
            }
            return new ImmutableLinkedSeq<>(res, size);
        }
    }

    @Override
    public @NotNull Iterator<E> reverseIterator() {
        return reversed().iterator();
    }

    //endregion

    //region Addition Operations

    public @NotNull ImmutableSeq<E> cons(E value) {
        return new ImmutableLinkedSeq<>(node.cons(value), size + 1);
    }

    @Override
    public @NotNull ImmutableSeq<E> prepended(E value) {
        return new ImmutableLinkedSeq<>(node.cons(value), size + 1);
    }

    @Override
    public @NotNull ImmutableSeq<E> appended(E value) {
        if (isEmpty()) return of(value);

        return super.appended(value);
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    public E getFirst() {
        return node.head();
    }

    @Override
    public E getLast() {
        if (node == NIL_NODE) {
            throw new NoSuchElementException();
        }
        Node<E> node = this.node;
        while (node.tail != NIL_NODE) {
            node = node.tail;
        }
        return node.head;
    }


    //endregion

    //region Misc Operations

    @Override
    public @NotNull ImmutableSeq<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableLinkedSeq.empty();
        }

        Node<E> list = this.node;
        for (int i = 0; i < n; i++) {
            list = list.tail;
        }
        return new ImmutableLinkedSeq<>(list, size - n);
    }

    @Override
    public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int c = 0;

        Node<E> list = this.node;
        while (list != NIL_NODE && predicate.test(list.head)) {
            list = list.tail();
            c++;
        }

        if (list == NIL_NODE) {
            return ImmutableLinkedSeq.empty();
        }
        if (c == 0) {
            return this;
        }

        return new ImmutableLinkedSeq<>(list, size - c);
    }

    @Override
    public @NotNull ImmutableSeq<E> takeLast(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n == 0) return ImmutableLinkedSeq.empty();
        if (n >= size) return this;

        return drop(size - n);
    }

    //endregion

    @Serial
    private Object writeReplace() {
        return new SerializationWrapper<>(factory(), this);
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableSinglyLinkedList<E>, ImmutableLinkedSeq<E>>, Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public MutableSinglyLinkedList<E> newBuilder() {
            return new MutableSinglyLinkedList<>();
        }

        @Override
        public ImmutableLinkedSeq<E> build(MutableSinglyLinkedList<E> builder) {
            return builder.toImmutableLinkedSeq();
        }

        @Override
        public void addToBuilder(@NotNull MutableSinglyLinkedList<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public MutableSinglyLinkedList<E> mergeBuilder(@NotNull MutableSinglyLinkedList<E> builder1, @NotNull MutableSinglyLinkedList<E> builder2) {
            return (MutableSinglyLinkedList<E>) Builder.merge(builder1, builder2);
        }

        @Serial
        private Object readResolve() {
            return factory();
        }
    }

    private static final class Node<@Covariant E> {
        E head;
        Node<E> tail;

        Node() {
        }

        Node(E head) {
            this.head = head;
        }

        Node(E head, @NotNull Node<E> tail) {
            this.head = head;
            this.tail = tail;
        }

        public E head() {
            if (this == NIL_NODE) {
                throw new NoSuchElementException("ImmutableList.Nil.head()");
            } else {
                return head;
            }
        }

        public @NotNull Node<E> tail() {
            if (this == NIL_NODE) {
                throw new NoSuchElementException("ImmutableList.Nil.tail()");
            } else {
                return tail;
            }
        }

        @Contract("_ -> new")
        public @NotNull Node<E> cons(E element) {
            return new Node<>(element, this);
        }
    }

    private static final class NodeItr<@Covariant E> extends AbstractIterator<E> {
        private @NotNull Node<? extends E> node;

        NodeItr(@NotNull Node<? extends E> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != NIL_NODE;
        }

        @Override
        public E next() {
            if (node == NIL_NODE) {
                throw new NoSuchElementException("ImmutableListIterator.next()");
            }

            E v = node.head();
            node = node.tail();
            return v;
        }
    }

    /**
     * Internal implementation of {@link MutableSinglyLinkedList}.
     *
     * @see MutableSinglyLinkedList
     */
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static abstract class Builder<E> extends AbstractMutableList<E> {
        Node<E> first = null;
        Node<E> last = null;

        int len = 0;

        private boolean aliased = false;

        static <E> Builder<E> merge(@NotNull Builder<E> b1, @NotNull Builder<E> b2) {
            final int b1s = b1.len;
            if (b1s == 0) {
                return b2;
            } else if (b1s == 1) {
                b2.prepend(b1.first.head);
                return b2;
            }

            final int b2s = b2.len;
            if (b2s == 0) {
                return b1;
            } else if (b2s == 1) {
                b1.append(b2.first.head);
                return b1;
            }

            b1.ensureUnaliased();
            b2.ensureUnaliased();

            final Node<E> b2f = b2.first;
            final Node<E> b2l = b2.last;
            b2.clear();

            b1.last.tail = b2f;
            b1.last = b2l;
            b1.len = b1s + b2s;
            return b1;
        }

        private void ensureUnaliased() {
            if (aliased) {
                Builder<E> buffer = new MutableSinglyLinkedList<>();
                buffer.appendAll(this);
                this.first = buffer.first;
                this.last = buffer.last;
                aliased = false;
            }
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            final Node<E> first = this.first;
            return first == null ? Iterators.empty() : new NodeItr<>(first);
        }

        @Override
        public @NotNull Iterator<E> iterator(@Index int beginIndex) {
            beginIndex = Indexes.checkPositionIndex(beginIndex, len);
            if (beginIndex == len) {
                return Iterators.empty();
            }

            Node<E> node = this.first;
            for (int i = 0; i < beginIndex; i++) {
                node = node.tail;
            }
            return new NodeItr<>(node);
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
        public final E get(@Index int index) {
            index = Indexes.checkIndex(index, len);

            if (index == len - 1)
                return last.head;

            Node<E> node = this.first;
            for (int i = 0; i < index; i++) {
                node = node.tail;
            }

            return node.head;
        }

        @Override
        public final void swap(@Index int index1, @Index int index2) {
            index1 = Indexes.checkIndex(index1, len);
            index2 = Indexes.checkIndex(index2, len);
            if (index1 == index2) {
                return;
            }
            ensureUnaliased();

            final int i1 = Integer.min(index1, index2);
            final int i2 = Integer.max(index1, index2);

            Node<E> node = this.first;
            int i = 0;
            while (i < i1) {
                node = node.tail;
                i++;
            }

            final Node<E> node1 = node;
            while (i < i2) {
                node = node.tail;
                i++;
            }
            final Node<E> node2 = node;

            final E tmp = node1.head;
            node1.head = node2.head;
            node2.head = tmp;
        }

        @Override
        public final E getFirst() {
            if (first == null) {
                throw new NoSuchElementException();
            } else {
                return first.head;
            }
        }

        @Override
        public final E getLast() {
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
        public final void prepend(E value) {
            if (len == 0) {
                first = last = new Node<>(value, nilNode());
            } else {
                first = first.cons(value);
            }
            len++;
        }

        @Override
        public final void append(E value) {
            Node<E> i = new Node<>(value, nilNode());
            if (len == 0) {
                first = i;
            } else {
                ensureUnaliased();
                last.tail = i;
            }
            last = i;
            len++;
        }

        @Override
        public void insert(int index, E value) {
            if (index < 0 || index > len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            if (index == 0) {
                prepend(value);
                return;
            }
            if (index == len) {
                append(value);
                return;
            }

            ensureUnaliased();
            Node<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail;
            }

            i.tail = i.tail.cons(value);
            ++len;
        }

        @Override
        public final E removeAt(int index) {
            Objects.checkIndex(index, len);

            if (index == 0) {
                E v = first.head;
                if (len == 1) {
                    first = last = null;
                    aliased = false;
                } else {
                    first = first.tail;
                }
                len--;
                return v;
            }

            ensureUnaliased();
            Node<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail();
            }
            E v = i.tail().head();
            i.tail = i.tail().tail();
            --len;
            return v;
        }

        public final void clear() {
            first = last = null;
            len = 0;
            aliased = false;
        }

        @Override
        public final @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
            final Node<E> first = this.first;
            if (first == null || first == NIL_NODE) {
                return ImmutableLinkedSeq.empty();
            }
            aliased = true;
            return new ImmutableLinkedSeq<>(first, len);
        }

        @Override
        public final void set(@Index int index, E newValue) {
            index = Indexes.checkIndex(index, len);

            ensureUnaliased();
            if (index == len - 1) {
                last.head = newValue;
                return;
            }

            Node<E> l = first;
            while (--index >= 0) {
                l = l.tail;
            }
            l.head = newValue;
        }

        @Override
        public final void sort(Comparator<? super E> comparator) {
            if (len == 0) {
                return;
            }
            Object[] values = toArray();
            Arrays.sort(values, (Comparator<? super Object>) comparator);

            if (aliased) {
                Node<E> newLast = new Node<>((E) values[values.length - 1], nilNode());
                Node<E> newFirst = newLast;

                for (int i = values.length - 2; i >= 0; i--) {
                    newFirst = new Node<>((E) values[i], newFirst);
                }

                first = newFirst;
                last = newLast;
                aliased = false;
            } else {
                Node<E> c = first;
                for (Object value : values) {
                    c.head = (E) value;
                    c = c.tail;
                }
            }
        }

        @Override
        public final void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
            Node<E> n = first;
            if (n == null || n == NIL_NODE) {
                return;
            }
            ensureUnaliased();
            while (n != NIL_NODE) {
                Node<E> c = n;
                c.head = operator.apply(c.head);
                n = c.tail;
            }
        }

        @Override
        public final void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
            Node<E> node = first;
            if (node == null || node == NIL_NODE) {
                return;
            }
            ensureUnaliased();
            int i = 0;
            while (node != NIL_NODE) {
                node.head = operator.apply(i++, node.head);
                node = node.tail;
            }
        }

        @Override
        public final boolean retainIf(@NotNull Predicate<? super E> predicate) {
            Node<E> prev = null;
            Node<E> cur = first;
            if (cur == null) {
                return false;
            }

            ensureUnaliased();
            final int oldLen = this.len;
            while (cur != NIL_NODE) {
                Node<E> follow = cur.tail;
                if (!predicate.test(cur.head)) {
                    if (prev == null) {
                        first = follow;
                    } else {
                        prev.tail = follow;
                    }
                    len--;
                } else {
                    prev = cur;
                }
                cur = follow;
            }
            last = prev;
            return len != oldLen;
        }

        @Override
        public final void reverse() {
            if (len <= 1) {
                return;
            }
            Builder<E> newBuilder = new MutableSinglyLinkedList<>();
            for (E e : this) {
                newBuilder.prepend(e);
            }
            this.first = newBuilder.first;
            this.last = newBuilder.last;
            this.aliased = false;
        }
    }
}
