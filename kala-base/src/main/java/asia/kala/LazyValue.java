package asia.kala;

import asia.kala.annotations.Covariant;
import asia.kala.iterator.AbstractIterator;
import asia.kala.traversable.Mappable;
import asia.kala.traversable.Traversable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LazyValue<@Covariant T> implements Traversable<T>, Mappable<T>, Serializable {
    private static final long serialVersionUID = 7403692951772568981L;

    private transient volatile Supplier<? extends T> supplier;
    private T value;

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T> LazyValue<T> narrow(LazyValue<? extends T> value) {
        return (LazyValue<T>) value;
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> LazyValue<T> of(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return new LazyValue<>(supplier);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> LazyValue<T> ofValue(T value) {
        return new LazyValue<>(value);
    }

    private LazyValue(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    private LazyValue(T value) {
        this.value = value;
    }

    public final T get() {
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

    public final boolean isReady() {
        return supplier == null;
    }

    @NotNull
    @Override
    @Contract("_ -> new")
    public final <U> LazyValue<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return LazyValue.of(() -> mapper.apply(get()));
    }

    @NotNull
    @Override
    public final Iterator<T> iterator() {
        return new AbstractIterator<T>() {
            private boolean hasNext = true;

            @Override
            public final boolean hasNext() {
                return hasNext;
            }

            @Override
            public final T next() {
                if (hasNext) {
                    hasNext = false;
                    return get();
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Override
    public final String toString() {
        if (supplier == null) {
            return "LazyValue[<not computed>]";
        } else {
            return "LazyValue[" + value + "]";
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(get());
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        value = (T) in.readObject();
    }
}
