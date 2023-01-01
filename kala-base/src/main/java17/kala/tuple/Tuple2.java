package kala.tuple;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A tuple of 2 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @author Glavo
 */
public record Tuple2<@Covariant T1, @Covariant T2>(T1 component1, T2 component2) implements HList<T1, Tuple1<T2>>, Serializable, Map.Entry<T1, T2> {
    private static final long serialVersionUID = 0L;

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1, T2> Tuple2<T1, T2> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends Unit>> tuple) {
        return (Tuple2<T1, T2>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) component1;
            case 1 -> (U) component2;
            default -> throw new IndexOutOfBoundsException("Index out of range: " + index);
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this.component1;
        arr[1] = (U) this.component2;
        return arr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T1 head() {
        return component1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tuple1<T2> tail() {
        return Tuple.of(component2);
    }

    /**
     * {@inheritDoc}
     */
    @Contract("_ -> new")
    @Override
    public <H> @NotNull Tuple3<H, T1, T2> cons(H head) {
        return new Tuple3<>(head, component1, component2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T1 getKey() {
        return component1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T2 getValue() {
        return component2;
    }

    /**
     * Used to override {@link Map # setValue}.
     * Tuples are immutable, calling this method will always fail.
     *
     * @throws UnsupportedOperationException when calling this method
     */
    @Override
    @Deprecated
    @Contract("_ -> fail")
    public T2 setValue(T2 value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Tuple2.setValue");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple2) {
            Tuple2<?, ?> other = (Tuple2<?, ?>) o;
            return Objects.equals(this.component1, other.component1) && Objects.equals(this.component2, other.component2);
        }

        if (o instanceof Map.Entry) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
            return Objects.equals(this.component1, other.getKey()) && Objects.equals(this.component2, other.getValue());
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 2
                    && Objects.equals(this.component1, other.elementAt(0))
                    && Objects.equals(this.component2, other.elementAt(1));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(component1) ^ Objects.hashCode(component2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ")" ;
    }
}
