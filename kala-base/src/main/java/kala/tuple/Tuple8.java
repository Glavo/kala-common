package kala.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple of 8 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 * @author Glavo
 */
public final class Tuple8<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7, @Covariant T8>
        extends HList<T1, Tuple7<T2, T3, T4, T5, T6, T7, T8>> implements Serializable {
    private static final long serialVersionUID = -1787310025057386303L;

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
     * Constructs a tuple of 8 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     */
    public Tuple8(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends HList<? extends T4, ? extends HList<? extends T5, ? extends HList<? extends T6, ? extends HList<? extends T7, ? extends HList<? extends T8, ? extends Unit>>>>>>>> tuple) {
        return (Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
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
        arr[0] = (U) this._1;
        arr[1] = (U) this._2;
        arr[2] = (U) this._3;
        arr[3] = (U) this._4;
        arr[4] = (U) this._5;
        arr[5] = (U) this._6;
        arr[6] = (U) this._7;
        arr[7] = (U) this._8;
        return arr;
    }

    /**
     * Returns the 1st element of this tuple.
     *
     * @return the 1st element of this tuple
     */
    public T1 component1() {
        return _1;
    }

    /**
     * Returns the 2nd element of this tuple.
     *
     * @return the 2nd element of this tuple
     */
    public T2 component2() {
        return _2;
    }

    /**
     * Returns the 3rd element of this tuple.
     *
     * @return the 3rd element of this tuple
     */
    public T3 component3() {
        return _3;
    }

    /**
     * Returns the 4th element of this tuple.
     *
     * @return the 4th element of this tuple
     */
    public T4 component4() {
        return _4;
    }

    /**
     * Returns the 5th element of this tuple.
     *
     * @return the 5th element of this tuple
     */
    public T5 component5() {
        return _5;
    }

    /**
     * Returns the 6th element of this tuple.
     *
     * @return the 6th element of this tuple
     */
    public T6 component6() {
        return _6;
    }

    /**
     * Returns the 7th element of this tuple.
     *
     * @return the 7th element of this tuple
     */
    public T7 component7() {
        return _7;
    }

    /**
     * Returns the 8th element of this tuple.
     *
     * @return the 8th element of this tuple
     */
    public T8 component8() {
        return _8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T1 head() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tuple7<T2, T3, T4, T5, T6, T7, T8> tail() {
        return of(_2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    public <H> @NotNull Tuple9<H, T1, T2, T3, T4, T5, T6, T7, T8> cons(H head) {
        return new Tuple9<>(head, _1, _2, _3, _4, _5, _6, _7, _8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple8) {
            Tuple8<?, ?, ?, ?, ?, ?, ?, ?> other = (Tuple8<?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this._1, other._1)
                    && Objects.equals(this._2, other._2)
                    && Objects.equals(this._3, other._3)
                    && Objects.equals(this._4, other._4)
                    && Objects.equals(this._5, other._5)
                    && Objects.equals(this._6, other._6)
                    && Objects.equals(this._7, other._7)
                    && Objects.equals(this._8, other._8);
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 8
                    && Objects.equals(this._1, other.elementAt(0))
                    && Objects.equals(this._2, other.elementAt(1))
                    && Objects.equals(this._3, other.elementAt(2))
                    && Objects.equals(this._4, other.elementAt(3))
                    && Objects.equals(this._5, other.elementAt(4))
                    && Objects.equals(this._6, other.elementAt(5))
                    && Objects.equals(this._7, other.elementAt(6))
                    && Objects.equals(this._8, other.elementAt(7));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(_1);
        hash = 31 * hash + Objects.hashCode(_2);
        hash = 31 * hash + Objects.hashCode(_3);
        hash = 31 * hash + Objects.hashCode(_4);
        hash = 31 * hash + Objects.hashCode(_5);
        hash = 31 * hash + Objects.hashCode(_6);
        hash = 31 * hash + Objects.hashCode(_7);
        hash = 31 * hash + Objects.hashCode(_8);
        return hash + HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ")" ;
    }
}
