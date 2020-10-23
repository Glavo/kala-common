package asia.kala;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

import asia.kala.annotations.Covariant;
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
public final class Tuple9<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7, @Covariant T8, @Covariant T9> extends HList<T1, Tuple8<T2, T3, T4, T5, T6, T7, T8, T9>> implements Serializable {
    private static final long serialVersionUID = 4767235624935269580L;

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
     * The 8th element of this tuple.
     */
    public final T8 _8;

    /**
     * The 9th element of this tuple.
     */
    public final T9 _9;

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
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
        this._9 = t9;
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
    public final int arity() {
        return 9;
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
            case 7:
                return (U) _8;
            case 8:
                return (U) _9;
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
        arr[7] = (U) this._8;
        arr[8] = (U) this._9;
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
     * Returns the 8th element of this tuple.
     *
     * @return the 8th element of this tuple
     */
    public final T8 component8() {
        return _8;
    }

    /**
     * Returns the 9th element of this tuple.
     *
     * @return the 9th element of this tuple
     */
    public final T9 component9() {
        return _9;
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
    public final Tuple8<T2, T3, T4, T5, T6, T7, T8, T9> tail() {
        return Tuple.of(_2, _3, _4, _5, _6, _7, _8, _9);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Contract("_ -> new")
    @Override
    public final <H> HList<H, Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> cons(H head) {
        Object[] arr = new Object[10];
        arr[0] = head;
        arr[1] = _1;
        arr[2] = _2;
        arr[3] = _3;
        arr[4] = _4;
        arr[5] = _5;
        arr[6] = _6;
        arr[7] = _7;
        arr[8] = _8;
        arr[9] = _9;
        return new TupleXXL(arr).cast();
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
        hash = 31 * hash + Objects.hashCode(_8);
        hash = 31 * hash + Objects.hashCode(_9);
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
        if (!(o instanceof Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>)) {
            return false;
        }
        Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> t = (Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(_1, t._1) && Objects.equals(_2, t._2) && Objects.equals(_3, t._3) && Objects.equals(_4, t._4) && Objects.equals(_5, t._5) && Objects.equals(_6, t._6) && Objects.equals(_7, t._7) && Objects.equals(_8, t._8) && Objects.equals(_9, t._9);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ")";
    }
}
