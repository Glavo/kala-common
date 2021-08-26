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
public final class DynamicLinkedSeq<E> extends ImmutableLinkedSeq.Builder<E>
        implements MutableSeqOps<E, DynamicLinkedSeq<?>, DynamicLinkedSeq<E>>, MutableStack<E>, Serializable {

    private static final long serialVersionUID = 4403781063629141093L;

    private static final DynamicLinkedSeq.Factory<?> FACTORY = new DynamicLinkedSeq.Factory<>();

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, DynamicLinkedSeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, DynamicLinkedSeq<E>> collector() {
        return factory();
    }

    @Contract(" -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of() {
        return new DynamicLinkedSeq<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E value1) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.append(value1);
        return buffer;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E value1, E value2) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.append(value1);
        buffer.append(value2);
        return buffer;
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E value1, E value2, E value3) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        return buffer;
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        return buffer;
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        buffer.append(value5);
        return buffer;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> from(E @NotNull [] values) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
        buffer.appendAll(values);
        return buffer;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
        DynamicLinkedSeq<E> res = new DynamicLinkedSeq<>();
        while (it.hasNext()) {
            res.append(it.next());
        }
        return res;
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicLinkedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull DynamicLinkedSeq<E> fill(int n, E value) {
        DynamicLinkedSeq<E> res = new DynamicLinkedSeq<>();
        while (n-- > 0) {
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull DynamicLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        DynamicLinkedSeq<E> res = new DynamicLinkedSeq<>();
        while (n-- > 0) {
            res.append(supplier.get());
        }
        return res;
    }

    public static <E> @NotNull DynamicLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        DynamicLinkedSeq<E> res = new DynamicLinkedSeq<>();
        for (int i = 0; i < n; i++) {
            res.append(init.apply(i));
        }
        return res;
    }

    //endregion

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "DynamicLinkedSeq";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, DynamicLinkedSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull DynamicSeqEditor<E, DynamicLinkedSeq<E>> edit() {
        return new DynamicSeqEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull DynamicLinkedSeq<E> clone() {
        DynamicLinkedSeq<E> res = new DynamicLinkedSeq<>();
        for (E e : this) {
            res.append(e);
        }
        return res;
    }

    //endregion

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

    private static final class Factory<E> extends AbstractDynamicSeqFactory<E, DynamicLinkedSeq<E>> {
        @Override
        public DynamicLinkedSeq<E> newBuilder() {
            return new DynamicLinkedSeq<>();
        }

        @Override
        public DynamicLinkedSeq<E> from(E @NotNull [] values) {
            return DynamicLinkedSeq.from(values);
        }

        @Override
        public DynamicLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
            return DynamicLinkedSeq.from(values);
        }

        @Override
        public DynamicLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
            return DynamicLinkedSeq.from(it);
        }

        @Override
        public DynamicLinkedSeq<E> fill(int n, E value) {
            return DynamicLinkedSeq.fill(n, value);
        }

        @Override
        public DynamicLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return DynamicLinkedSeq.fill(n, supplier);
        }

        @Override
        public DynamicLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return DynamicLinkedSeq.fill(n, init);
        }
    }
}