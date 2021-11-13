package kala.value;

import kala.collection.base.Traversable;
import kala.annotations.Covariant;
import kala.collection.base.AbstractIterator;
import kala.collection.base.Mappable;
import kala.function.Memoized;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LazyValue<@Covariant T> implements Traversable<T>, Mappable<T>, Value<T>, Supplier<T>, Memoized, Serializable {
    private static final long serialVersionUID = 7403692951772568981L;

    private transient volatile Supplier<? extends T> supplier;
    private transient T value;

    private LazyValue(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    private LazyValue(T value) {
        this.value = value;
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T> LazyValue<T> narrow(LazyValue<? extends T> value) {
        return (LazyValue<T>) value;
    }

    @Contract("_ -> new")
    public static <T> @NotNull LazyValue<T> of(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return new LazyValue<>(supplier);
    }

    @Contract("_ -> new")
    public static <T> @NotNull LazyValue<T> ofValue(T value) {
        return new LazyValue<>(value);
    }

    public T get() {
        if (supplier != null) {
            synchronized (this) {
                Supplier<? extends T> s = supplier;
                if (s != null) {
                    value = s.get();
                    supplier = null;
                }
            }
        }
        return value;
    }

    public boolean isReady() {
        return supplier == null;
    }

    @Override
    @Contract("_ -> new")
    public <U> @NotNull LazyValue<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return LazyValue.of(() -> mapper.apply(get()));
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new AbstractIterator<T>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return get();
            }
        };
    }

    @Override
    public String toString() {
        if (supplier == null) {
            return "LazyValue[" + value + "]";
        } else {
            return "LazyValue[<not computed>]";
        }
    }

    //region Serialization Operations

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        value = (T) in.readObject();
        supplier = null;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(get());
    }

    //endregion
}
