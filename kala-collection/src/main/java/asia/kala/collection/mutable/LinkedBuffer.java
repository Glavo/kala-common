package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import asia.kala.collection.immutable.ImmutableInternal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public final class LinkedBuffer<E> extends ImmutableInternal.LinkedBufferImpl<E>
        implements MutableSeqOps<E, LinkedBuffer<?>, LinkedBuffer<E>>, Serializable {

    private static final long serialVersionUID = 4403781063629141093L;

    private static final LinkedBuffer.Factory<?> FACTORY = new LinkedBuffer.Factory<>();

    //region Factory methods

    @SuppressWarnings("unchecked")
    public static <E> CollectionFactory<E, ?, LinkedBuffer<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @NotNull
    @Contract(" -> new")
    public static <E> LinkedBuffer<E> of() {
        return new LinkedBuffer<>();
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> LinkedBuffer<E> of(E value1) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        return buffer;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <E> LinkedBuffer<E> of(E value1, E value2) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        return buffer;
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static <E> LinkedBuffer<E> of(E value1, E value2, E value3) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        return buffer;
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static <E> LinkedBuffer<E> of(E value1, E value2, E value3, E value4) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        return buffer;
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static <E> LinkedBuffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.append(value1);
        buffer.append(value2);
        buffer.append(value3);
        buffer.append(value4);
        buffer.append(value5);
        return buffer;
    }

    @NotNull
    @SafeVarargs
    @Contract("_ -> new")
    public static <E> LinkedBuffer<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> LinkedBuffer<E> from(E @NotNull [] values) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.appendAll(values);
        return buffer;
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> LinkedBuffer<E> from(@NotNull Iterable<? extends E> values) {
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.appendAll(values);
        return buffer;
    }

    //endregion

    //region MutableCollection

    @Override
    public final String className() {
        return "LinkedBuffer";
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, LinkedBuffer<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    public final BufferEditor<E, LinkedBuffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @NotNull
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final LinkedBuffer<E> clone() {
        LinkedBuffer<E> res = new LinkedBuffer<>();
        for (E e : this) {
            res.append(e);
        }
        return res;
    }

    //endregion

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size());
        for (E e : this) {
            out.writeObject(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.append((E) in.readObject());
        }
    }

    //endregion

    private static final class Factory<E> extends AbstractBufferFactory<E, LinkedBuffer<E>> {
        @Override
        public final LinkedBuffer<E> newBuilder() {
            return new LinkedBuffer<>();
        }
    }
}