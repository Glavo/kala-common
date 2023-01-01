package kala.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple of 9 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 * @param <T9> type of the 9th element
 * @author Glavo
 */
public final class Tuple9<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7, @Covariant T8, @Covariant T9>
        implements HList<T1, Tuple8<T2, T3, T4, T5, T6, T7, T8, T9>>, Serializable {
    private static final long serialVersionUID = 0L;

    /**
     * The 1st element of this tuple.
     */
    private final T1 component1;

    /**
     * The 2nd element of this tuple.
     */
    private final T2 component2;

    /**
     * The 3rd element of this tuple.
     */
    private final T3 component3;

    /**
     * The 4th element of this tuple.
     */
    private final T4 component4;

    /**
     * The 5th element of this tuple.
     */
    private final T5 component5;

    /**
     * The 6th element of this tuple.
     */
    private final T6 component6;

    /**
     * The 7th element of this tuple.
     */
    private final T7 component7;

    /**
     * The 8th element of this tuple.
     */
    private final T8 component8;

    /**
     * The 9th element of this tuple.
     */
    private final T9 component9;

    /**
     * Constructs a tuple of 9 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     */
    public Tuple9(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        this.component1 = t1;
        this.component2 = t2;
        this.component3 = t3;
        this.component4 = t4;
        this.component5 = t5;
        this.component6 = t6;
        this.component7 = t7;
        this.component8 = t8;
        this.component9 = t9;
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends HList<? extends T4, ? extends HList<? extends T5, ? extends HList<? extends T6, ? extends HList<? extends T7, ? extends HList<? extends T8, ? extends HList<? extends T9, ? extends Unit>>>>>>>>> tuple) {
        return (Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 9;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        switch (index) {
            case 0:
                return (U) component1;
            case 1:
                return (U) component2;
            case 2:
                return (U) component3;
            case 3:
                return (U) component4;
            case 4:
                return (U) component5;
            case 5:
                return (U) component6;
            case 6:
                return (U) component7;
            case 7:
                return (U) component8;
            case 8:
                return (U) component9;
            default:
                throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
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
        arr[7] = (U) this.component8;
        arr[8] = (U) this.component9;
        return arr;
    }

    /**
     * Returns the 1st element of this tuple.
     *
     * @return the 1st element of this tuple
     */
    public T1 component1() {
        return component1;
    }

    /**
     * Returns the 2nd element of this tuple.
     *
     * @return the 2nd element of this tuple
     */
    public T2 component2() {
        return component2;
    }

    /**
     * Returns the 3rd element of this tuple.
     *
     * @return the 3rd element of this tuple
     */
    public T3 component3() {
        return component3;
    }

    /**
     * Returns the 4th element of this tuple.
     *
     * @return the 4th element of this tuple
     */
    public T4 component4() {
        return component4;
    }

    /**
     * Returns the 5th element of this tuple.
     *
     * @return the 5th element of this tuple
     */
    public T5 component5() {
        return component5;
    }

    /**
     * Returns the 6th element of this tuple.
     *
     * @return the 6th element of this tuple
     */
    public T6 component6() {
        return component6;
    }

    /**
     * Returns the 7th element of this tuple.
     *
     * @return the 7th element of this tuple
     */
    public T7 component7() {
        return component7;
    }

    /**
     * Returns the 8th element of this tuple.
     *
     * @return the 8th element of this tuple
     */
    public T8 component8() {
        return component8;
    }

    /**
     * Returns the 9th element of this tuple.
     *
     * @return the 9th element of this tuple
     */
    public T9 component9() {
        return component9;
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
    public @NotNull Tuple8<T2, T3, T4, T5, T6, T7, T8, T9> tail() {
        return Tuple.of(component2, component3, component4, component5, component6, component7, component8, component9);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <H> @NotNull HList<H, Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> cons(H head) {
        Object[] arr = new Object[10];
        arr[0] = head;
        arr[1] = component1;
        arr[2] = component2;
        arr[3] = component3;
        arr[4] = component4;
        arr[5] = component5;
        arr[6] = component6;
        arr[7] = component7;
        arr[8] = component8;
        arr[9] = component9;
        return (HList) new TupleXXL(arr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple9) {
            Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> other = (Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this.component1, other.component1)
                    && Objects.equals(this.component2, other.component2)
                    && Objects.equals(this.component3, other.component3)
                    && Objects.equals(this.component4, other.component4)
                    && Objects.equals(this.component5, other.component5)
                    && Objects.equals(this.component6, other.component6)
                    && Objects.equals(this.component7, other.component7)
                    && Objects.equals(this.component8, other.component8)
                    && Objects.equals(this.component9, other.component9);
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 9
                    && Objects.equals(this.component1, other.elementAt(0))
                    && Objects.equals(this.component2, other.elementAt(1))
                    && Objects.equals(this.component3, other.elementAt(2))
                    && Objects.equals(this.component4, other.elementAt(3))
                    && Objects.equals(this.component5, other.elementAt(4))
                    && Objects.equals(this.component6, other.elementAt(5))
                    && Objects.equals(this.component7, other.elementAt(6))
                    && Objects.equals(this.component8, other.elementAt(7))
                    && Objects.equals(this.component9, other.elementAt(8));
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
        hash = 31 * hash + Objects.hashCode(component8);
        hash = 31 * hash + Objects.hashCode(component9);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ", " + component3 + ", " + component4 + ", " + component5 + ", " + component6 + ", " + component7 + ", " + component8 + ", " + component9 + ")" ;
    }

    private Object writeReplace() {
        return new SerializedTuple(toArray());
    }
}
