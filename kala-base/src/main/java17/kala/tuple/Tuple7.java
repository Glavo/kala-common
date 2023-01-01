package kala.tuple;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A tuple of 7 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @author Glavo
 */
public record Tuple7<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7>(
        T1 component1, T2 component2, T3 component3,
        T4 component4, T5 component5, T6 component6,
        T7 component7
) implements HList<T1, Tuple6<T2, T3, T4, T5, T6, T7>>, Serializable {
    private static final long serialVersionUID = 0L;

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends HList<? extends T4, ? extends HList<? extends T5, ? extends HList<? extends T6, ? extends HList<? extends T7, ? extends Unit>>>>>>> tuple) {
        return (Tuple7<T1, T2, T3, T4, T5, T6, T7>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 7;
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
            case 2 -> (U) component3;
            case 3 -> (U) component4;
            case 4 -> (U) component5;
            case 5 -> (U) component6;
            case 6 -> (U) component7;
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
        arr[2] = (U) this.component3;
        arr[3] = (U) this.component4;
        arr[4] = (U) this.component5;
        arr[5] = (U) this.component6;
        arr[6] = (U) this.component7;
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
    public @NotNull Tuple6<T2, T3, T4, T5, T6, T7> tail() {
        return Tuple.of(component2, component3, component4, component5, component6, component7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    public <H> @NotNull Tuple8<H, T1, T2, T3, T4, T5, T6, T7> cons(H head) {
        return new Tuple8<>(head, component1, component2, component3, component4, component5, component6, component7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple7) {
            Tuple7<?, ?, ?, ?, ?, ?, ?> other = (Tuple7<?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this.component1, other.component1)
                    && Objects.equals(this.component2, other.component2)
                    && Objects.equals(this.component3, other.component3)
                    && Objects.equals(this.component4, other.component4)
                    && Objects.equals(this.component5, other.component5)
                    && Objects.equals(this.component6, other.component6)
                    && Objects.equals(this.component7, other.component7);
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 7
                    && Objects.equals(this.component1, other.elementAt(0))
                    && Objects.equals(this.component2, other.elementAt(1))
                    && Objects.equals(this.component3, other.elementAt(2))
                    && Objects.equals(this.component4, other.elementAt(3))
                    && Objects.equals(this.component5, other.elementAt(4))
                    && Objects.equals(this.component6, other.elementAt(5))
                    && Objects.equals(this.component7, other.elementAt(6));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(component1);
        hash = 31 * hash + Objects.hashCode(component2);
        hash = 31 * hash + Objects.hashCode(component3);
        hash = 31 * hash + Objects.hashCode(component4);
        hash = 31 * hash + Objects.hashCode(component5);
        hash = 31 * hash + Objects.hashCode(component6);
        hash = 31 * hash + Objects.hashCode(component7);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ", " + component3 + ", " + component4 + ", " + component5 + ", " + component6 + ", " + component7 + ")";
    }
}
