package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableLinkedSeq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public final class MutableSinglyLinkedList<E> extends ImmutableLinkedSeq.Builder<E> implements MutableStack<E>, Serializable {
    private static final long serialVersionUID = 4403781063629141093L;

    private static final MutableSinglyLinkedList.Factory<?> FACTORY = new MutableSinglyLinkedList.Factory<>();

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, MutableSinglyLinkedList<E>> factory() {
        return (Factory<E>) FACTORY;
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
        while (n-- > 0) {
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableSinglyLinkedList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        MutableSinglyLinkedList<E> res = new MutableSinglyLinkedList<>();
        while (n-- > 0) {
            res.append(supplier.get());
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
        return first();
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.append((E) in.readObject());
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size());
        for (E e : this) {
            out.writeObject(e);
        }
    }

    private static final class Factory<E> extends AbstractMutableListFactory<E, MutableSinglyLinkedList<E>> {
        @Override
        public MutableSinglyLinkedList<E> newBuilder() {
            return new MutableSinglyLinkedList<>();
        }

        @Override
        public MutableSinglyLinkedList<E> from(E @NotNull [] values) {
            return MutableSinglyLinkedList.from(values);
        }

        @Override
        public MutableSinglyLinkedList<E> from(@NotNull Iterable<? extends E> values) {
            return MutableSinglyLinkedList.from(values);
        }

        @Override
        public MutableSinglyLinkedList<E> from(@NotNull Iterator<? extends E> it) {
            return MutableSinglyLinkedList.from(it);
        }

        @Override
        public MutableSinglyLinkedList<E> fill(int n, E value) {
            return MutableSinglyLinkedList.fill(n, value);
        }

        @Override
        public MutableSinglyLinkedList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return MutableSinglyLinkedList.fill(n, supplier);
        }

        @Override
        public MutableSinglyLinkedList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return MutableSinglyLinkedList.fill(n, init);
        }
    }
}