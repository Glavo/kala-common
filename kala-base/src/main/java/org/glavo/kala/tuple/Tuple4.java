package org.glavo.kala.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple of 4 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @author Glavo
 */
public final class Tuple4<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4> extends HList<T1, Tuple3<T2, T3, T4>> implements Serializable {
    private static final long serialVersionUID = 2111962546132489176L;

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
     * Constructs a tuple of 4 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     */
    public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends HList<? extends T4, ? extends Unit>>>> tuple) {
        return (Tuple4<T1, T2, T3, T4>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int arity() {
        return 4;
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
            default:
                throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this._1;
        arr[1] = (U) this._2;
        arr[2] = (U) this._3;
        arr[3] = (U) this._4;
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
     * {@inheritDoc}
     */
    @Override
    public final T1 head() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @NotNull Tuple3<T2, T3, T4> tail() {
        return Tuple.of(_2, _3, _4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    public final <H> @NotNull Tuple5<H, T1, T2, T3, T4> cons(H head) {
        return new Tuple5<>(head, _1, _2, _3, _4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple4<?, ?, ?, ?>)) {
            return false;
        }
        Tuple4<?, ?, ?, ?> t = (Tuple4<?, ?, ?, ?>) o;
        return Objects.equals(_1, t._1) && Objects.equals(_2, t._2) && Objects.equals(_3, t._3) && Objects.equals(_4, t._4);
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
        return hash + Tuple.HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ")";
    }
}
