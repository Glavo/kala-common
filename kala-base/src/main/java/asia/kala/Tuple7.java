package asia.kala;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
public final class Tuple7<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7>
        extends HList<T1, Tuple6<T2, T3, T4, T5, T6, T7>> implements Serializable {
    private static final long serialVersionUID = -7563939273863421454L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    public final T2 _2;

    /**
     * The 3rd element of this tuple.
     */
    public final T3 _3;

    /**
     * The 4th element of this tuple.
     */
    public final T4 _4;

    /**
     * The 5th element of this tuple.
     */
    public final T5 _5;

    /**
     * The 6th element of this tuple.
     */
    public final T6 _6;

    /**
     * The 7th element of this tuple.
     */
    public final T7 _7;

    /**
     * Constructs a tuple of 7 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     */
    public Tuple7(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
    }

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
    public final int arity() {
        return 7;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <U> U elementAt(int index) {
        switch (index) {
            case 0:
                return (U) _1;
            case 1:
                return (U) _2;
            case 2:
                return (U) _3;
            case 3:
                return (U) _4;
            case 4:
                return (U) _5;
            case 5:
                return (U) _6;
            case 6:
                return (U) _7;
            default:
                throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public final <U> U[] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this._1;
        arr[1] = (U) this._2;
        arr[2] = (U) this._3;
        arr[3] = (U) this._4;
        arr[4] = (U) this._5;
        arr[5] = (U) this._6;
        arr[6] = (U) this._7;
        return arr;
    }

    /**
     * Returns the 1st element of this tuple.
     *
     * @return the 1st element of this tuple
     */
    public final T1 component1() {
        return _1;
    }

    /**
     * Returns the 2nd element of this tuple.
     *
     * @return the 2nd element of this tuple
     */
    public final T2 component2() {
        return _2;
    }

    /**
     * Returns the 3rd element of this tuple.
     *
     * @return the 3rd element of this tuple
     */
    public final T3 component3() {
        return _3;
    }

    /**
     * Returns the 4th element of this tuple.
     *
     * @return the 4th element of this tuple
     */
    public final T4 component4() {
        return _4;
    }

    /**
     * Returns the 5th element of this tuple.
     *
     * @return the 5th element of this tuple
     */
    public final T5 component5() {
        return _5;
    }

    /**
     * Returns the 6th element of this tuple.
     *
     * @return the 6th element of this tuple
     */
    public final T6 component6() {
        return _6;
    }

    /**
     * Returns the 7th element of this tuple.
     *
     * @return the 7th element of this tuple
     */
    public final T7 component7() {
        return _7;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T1 head() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Tuple6<T2, T3, T4, T5, T6, T7> tail() {
        return Tuple.of(_2, _3, _4, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Contract("_ -> new")
    @Override
    public final <H> Tuple8<H, T1, T2, T3, T4, T5, T6, T7> cons(H head) {
        return new Tuple8<>(head, _1, _2, _3, _4, _5, _6, _7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(_1);
        hash = 31 * hash + Objects.hashCode(_2);
        hash = 31 * hash + Objects.hashCode(_3);
        hash = 31 * hash + Objects.hashCode(_4);
        hash = 31 * hash + Objects.hashCode(_5);
        hash = 31 * hash + Objects.hashCode(_6);
        hash = 31 * hash + Objects.hashCode(_7);
        return hash + Tuple.HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple7<?, ?, ?, ?, ?, ?, ?>)) {
            return false;
        }
        Tuple7<?, ?, ?, ?, ?, ?, ?> t = (Tuple7<?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(_1, t._1) && Objects.equals(_2, t._2) && Objects.equals(_3, t._3) && Objects.equals(_4, t._4) && Objects.equals(_5, t._5) && Objects.equals(_6, t._6) && Objects.equals(_7, t._7);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ")";
    }
}
