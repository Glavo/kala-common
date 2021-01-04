package org.glavo.kala;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple of 2 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @author Glavo
 */
public final class Tuple2<@Covariant T1, @Covariant T2> extends HList<T1, Tuple1<T2>> implements Serializable, Map.Entry<T1, T2> {
    private static final long serialVersionUID = -1620386824894229867L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    public final T2 _2;

    /**
     * Constructs a tuple of 2 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     */
    public Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

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
    public final int arity() {
        return 2;
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
    public final @NotNull Tuple1<T2> tail() {
        return Tuple.of(_2);
    }

    /**
     * {@inheritDoc}
     */
    @Contract("_ -> new")
    @Override
    public final <H> @NotNull Tuple3<H, T1, T2> cons(H head) {
        return new Tuple3<>(head, _1, _2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T1 getKey() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T2 getValue() {
        return _2;
    }

    /**
     * Used to override {@link java.util.Map # setValue}.
     * Tuples are immutable, calling this method will always fail.
     *
     * @throws UnsupportedOperationException when calling this method
     */
    @Override
    @Deprecated
    @Contract("_ -> fail")
    public final T2 setValue(T2 value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Tuple2.setValue");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple2<?, ?>)) {
            return false;
        }
        Tuple2<?, ?> t = (Tuple2<?, ?>) o;
        return Objects.equals(_1, t._1) && Objects.equals(_2, t._2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(_1);
        hash = 31 * hash + Objects.hashCode(_2);
        return hash + Tuple.HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
}
