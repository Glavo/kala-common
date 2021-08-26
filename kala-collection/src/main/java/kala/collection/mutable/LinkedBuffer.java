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
import java.util.stream.Collector;
import java.util.stream.Stream;

@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public final class LinkedBuffer<E> extends ImmutableLinkedSeq.Builder<E>
        implements MutableSeqOps<E, LinkedBuffer<?>, LinkedBuffer<E>>, MutableStack<E>, Serializable {

    private static final long serialVersionUID = 4403781063629141093L;

    private static final LinkedBuffer.Factory<?> FACTORY = new LinkedBuffer.Factory<>();

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, LinkedBuffer<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, LinkedBuffer<E>> collector() {
        return factory();
    }

    @Contract(" -> new")
    public static <E> @NotNull LinkedBuffer<E> of() {
        return new LinkedBuffer<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E value1) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        return buffer;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E value1, E value2) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        return buffer;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E value1, E value2, E value3) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        return buffer;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E value1, E value2, E value3, E value4) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        return buffer;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        buffer.append(value5);
        return buffer;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> from(E @NotNull [] values) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> from(@NotNull Iterable<? extends E> values) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> from(@NotNull Iterator<? extends E> it) {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        while (it.hasNext()) {
            res.append(it.next());
        }
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull LinkedBuffer<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull LinkedBuffer<E> fill(int n, E value) {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        while (n-- > 0) {
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull LinkedBuffer<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        while (n-- > 0) {
            res.append(supplier.get());
        }
        return res;
    }

    public static <E> @NotNull LinkedBuffer<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        for (int i = 0; i < n; i++) {
            res.append(init.apply(i));
        }
        return res;
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "LinkedBuffer";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, LinkedBuffer<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final @NotNull BufferEditor<E, LinkedBuffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull LinkedBuffer<E> clone() {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        for (E e : this) {
            res.append(e);
        }
        return res;
    }

    //endregion

    @Override
    public final void push(E value) {
        prepend(value);
    }

    @Override
    public final E pop() {
        return removeFirst();
    }

    @Override
    public final E peek() {
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

    private static final class Factory<E> extends AbstractBufferFactory<E, LinkedBuffer<E>> {
        @Override
        public final LinkedBuffer<E> newBuilder() {
            return new LinkedBuffer<>();
        }

        @Override
        public final LinkedBuffer<E> from(E @NotNull [] values) {
            return LinkedBuffer.from(values);
        }

        @Override
        public final LinkedBuffer<E> from(@NotNull Iterable<? extends E> values) {
            return LinkedBuffer.from(values);
        }

        @Override
        public final LinkedBuffer<E> from(@NotNull Iterator<? extends E> it) {
            return LinkedBuffer.from(it);
        }

        @Override
        public final LinkedBuffer<E> fill(int n, E value) {
            return LinkedBuffer.fill(n, value);
        }

        @Override
        public final LinkedBuffer<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return LinkedBuffer.fill(n, supplier);
        }

        @Override
        public final LinkedBuffer<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return LinkedBuffer.fill(n, init);
        }
    }
}