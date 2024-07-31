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

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableLinkedSeq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public final class MutableSinglyLinkedList<E> extends ImmutableLinkedSeq.Builder<E>
        implements MutableStack<E>, FreezableMutableList<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 4403781063629141093L;

    private static final MutableListFactory<Object, MutableSinglyLinkedList<Object>> FACTORY = MutableSinglyLinkedList::new;

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableSinglyLinkedList<E>> factory() {
        return MutableListFactory.cast(FACTORY);
    }

    @Contract(" -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> create() {
        return new MutableSinglyLinkedList<>();
    }

    @Contract(" -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of() {
        return new MutableSinglyLinkedList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E value1) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.append(value1);
        return buffer;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E value1, E value2) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.append(value1);
        buffer.append(value2);
        return buffer;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E value1, E value2, E value3) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        return buffer;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E value1, E value2, E value3, E value4) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        return buffer;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        buffer.append(value5);
        return buffer;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> from(E @NotNull [] values) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> from(@NotNull Iterable<? extends E> values) {
        MutableSinglyLinkedList<E> buffer = new MutableSinglyLinkedList<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> from(@NotNull Iterator<? extends E> it) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        while (it.hasNext()) {
            res.append(it.next());
        }
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSinglyLinkedList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableSinglyLinkedList<E> fill(int n, E value) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        for (int i = 0; i < n; i++) {
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableSinglyLinkedList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        for (int i = 0; i < n; i++) {
            res.append(init.apply(i));
        }
        return res;
    }

    public static <E> @NotNull MutableSinglyLinkedList<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableSinglyLinkedList<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
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
        return "MutableSinglyLinkedList";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableSinglyLinkedList<U>> iterableFactory() {
        return factory();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableSinglyLinkedList<E> clone() {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        for (E e : this) {
            res.append(e);
        }
        return res;
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
    public @NotNull ImmutableLinkedSeq<E> freeze() {
        return toImmutableLinkedSeq();
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.append((E) in.readObject());
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size());
        for (E e : this) {
            out.writeObject(e);
        }
    }
}