package kala.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import kala.annotations.Covariant;
import kala.annotations.ReplaceWith;
import kala.collection.base.Mappable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple of 1 element.
 *
 * @param <T1> type of the 1st element
 * @author Glavo
 */
public final class Tuple1<@Covariant T1> implements HList<T1, Unit>, Mappable<T1>, Serializable {
    private static final long serialVersionUID = -2553287320045901284L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * Constructs a tuple of 1 element.
     *
     * @param t1 the 1st element
     */
    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1> Tuple1<T1> narrow(HList<? extends T1, ? extends Unit> tuple) {
        return (Tuple1<T1>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        if (index == 0) {
            return (U) _1;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this._1;
        return arr;
    }

    /**
     * Returns the 1st element of this tuple.
     *
     * @return the 1st element of this tuple
     */
    public T1 _1() {
        return _1;
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
    public @NotNull Unit tail() {
        return Unit.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    public <H> @NotNull Tuple2<H, T1> cons(H head) {
        return new Tuple2<>(head, _1);
    }

    @Override
    public <U> @NotNull Tuple1<U> map(@NotNull Function<? super T1, ? extends U> mapper) {
        return new Tuple1<>(mapper.apply(_1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple1) {
            Tuple1<?> other = (Tuple1<?>) o;
            return Objects.equals(this._1, other._1);
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 1 && Objects.equals(this._1, other.elementAt(0));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HASH_MAGIC + Objects.hashCode(_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + _1 + ")" ;
    }
}
