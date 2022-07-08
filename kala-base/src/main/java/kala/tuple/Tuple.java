package kala.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntFunction;

import kala.collection.base.GenericArrays;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The base class of all tuples.
 *
 * @author Glavo
 * @see HList
 */
public abstract class Tuple implements AnyTuple, Serializable {
    Tuple() {
    }

    /**
     * Returns the number of elements of this {@code Tuple}.
     *
     * @return the number of elements of this {@code Tuple}
     */
    @Contract(pure = true)
    public abstract int arity();

    /**
     * Returns the element at the specified position in this {@code Tuple}.
     *
     * @return the element at the specified position in this {@code Tuple}
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >=
     *                                   arity()})
     */
    @Contract(pure = true)
    @Flow(sourceIsContainer = true)
    public abstract <U> U elementAt(int index);

    /**
     * Return a new tuple by prepending the head to `this` tuple.
     *
     * @return a new tuple by prepending the head to `this` tuple
     */
    @Contract(pure = true)
    public abstract <H> @NotNull HList<H, ? extends Tuple> cons(H head);

    /**
     * Returns an array containing all the elements in this tuple.
     *
     * @return an array containing all the elements in this tuple
     */
    @Contract(value = "-> new", pure = true)
    public Object @NotNull [] toArray() {
        return toArray(Object[]::new);
    }

    public <U> U @NotNull [] toArray(@NotNull Class<U> type) {
        return toArray(GenericArrays.generator(type));
    }

    /**
     * Returns an array containing all the elements in this tuple, using the provided {@code
     * generator} function to allocate the returned array.
     *
     * @return an array containing all the elements in this tuple
     * @throws ArrayStoreException if any element of this tuple cannot be stored in the generated
     *                             array because the runtime type does not match
     */
    @Contract(pure = true)
    public abstract <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator);

    public static @NotNull Unit empty() {
        return Unit.INSTANCE;
    }

    public static @NotNull Unit of() {
        return Unit.INSTANCE;
    }

    /**
     * Creates a tuple of 1 element.
     *
     * @param <T1> type of the 1st element
     * @param t1   the 1st element
     * @return a tuple of 1 element
     */
    @Contract("_ -> new")
    public static <T1> @NotNull Tuple1<T1> of(T1 t1) {
        return new Tuple1<>(t1);
    }

    /**
     * Creates a tuple of 2 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @return a tuple of 2 elements
     */
    @Contract("_, _ -> new")
    public static <T1, T2> @NotNull Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    /**
     * Creates a tuple of 3 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @return a tuple of 3 elements
     */
    @Contract("_, _, _ -> new")
    public static <T1, T2, T3> @NotNull Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

    /**
     * Creates a tuple of 4 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @return a tuple of 4 elements
     */
    @Contract("_, _, _, _ -> new")
    public static <T1, T2, T3, T4> @NotNull Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    /**
     * Creates a tuple of 5 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @return a tuple of 5 elements
     */
    @Contract("_, _, _, _, _ -> new")
    public static <T1, T2, T3, T4, T5> @NotNull Tuple5<T1, T2, T3, T4, T5> of(
            T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return new Tuple5<>(t1, t2, t3, t4, t5);
    }

    /**
     * Creates a tuple of 6 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @return a tuple of 6 elements
     */
    @Contract("_, _, _, _, _, _ -> new")
    public static <T1, T2, T3, T4, T5, T6> @NotNull Tuple6<T1, T2, T3, T4, T5, T6> of(
            T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return new Tuple6<>(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Creates a tuple of 7 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @param t7   the 7th element
     * @return a tuple of 7 elements
     */
    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T1, T2, T3, T4, T5, T6, T7> @NotNull Tuple7<T1, T2, T3, T4, T5, T6, T7> of(
            T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return new Tuple7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Creates a tuple of 8 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @param t7   the 7th element
     * @param t8   the 8th element
     * @return a tuple of 8 elements
     */
    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T1, T2, T3, T4, T5, T6, T7, T8> @NotNull Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(
            T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return new Tuple8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    /**
     * Creates a tuple of 9 elements.
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
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @param t7   the 7th element
     * @param t8   the 8th element
     * @param t9   the 9th element
     * @return a tuple of 9 elements
     */
    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9>
    @NotNull Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> of(
            T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return new Tuple9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @SuppressWarnings("unchecked")
    @Contract("_ -> new")
    public static <T extends Tuple> @NotNull T of(Object... values) {
        switch (values.length) {
            case 0:
                return (T) Unit.INSTANCE;
            case 1:
                return (T) new Tuple1<>(values[0]);
            case 2:
                return (T) new Tuple2<>(values[0], values[1]);
            case 3:
                return (T) new Tuple3<>(values[0], values[1], values[2]);
            case 4:
                return (T) new Tuple4<>(values[0], values[1], values[2], values[3]);
            case 5:
                return (T) new Tuple5<>(values[0], values[1], values[2], values[3], values[4]);
            case 6:
                return (T)
                        new Tuple6<>(
                                values[0], values[1], values[2], values[3], values[4], values[5]);
            case 7:
                return (T)
                        new Tuple7<>(
                                values[0], values[1], values[2], values[3], values[4], values[5],
                                values[6]);
            case 8:
                return (T)
                        new Tuple8<>(
                                values[0], values[1], values[2], values[3], values[4], values[5],
                                values[6], values[7]);
            case 9:
                return (T)
                        new Tuple9<>(
                                values[0], values[1], values[2], values[3], values[4], values[5],
                                values[6], values[7], values[8]);
            default:
                return (T) new TupleXXL(values.clone());
        }
    }

    /**
     * Return the 1st element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 1st element of the {@code tuple}
     */
    public static <T> T component1(@NotNull HList<T, ?> tuple) {
        return tuple.elementAt(0);
    }

    /**
     * Return the 2nd element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 2nd element of the {@code tuple}
     */
    public static <T> T component2(@NotNull HList<?, ? extends HList<T, ?>> tuple) {
        return tuple.elementAt(1);
    }

    /**
     * Return the 3rd element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 3rd element of the {@code tuple}
     */
    public static <T> T component3(
            @NotNull HList<?, ? extends HList<?, ? extends HList<T, ?>>> tuple) {
        return tuple.elementAt(2);
    }

    /**
     * Return the 4th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 4th element of the {@code tuple}
     */
    public static <T> T component4(
            @NotNull
                    HList<?, ? extends HList<?, ? extends HList<?, ? extends HList<T, ?>>>> tuple) {
        return tuple.elementAt(3);
    }

    /**
     * Return the 5th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 5th element of the {@code tuple}
     */
    public static <T> T component5(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            T,
                                                                                            ?>>>>>
                    tuple) {
        return tuple.elementAt(4);
    }

    /**
     * Return the 6th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 6th element of the {@code tuple}
     */
    public static <T> T component6(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            T,
                                                                                                            ?>>>>>>
                    tuple) {
        return tuple.elementAt(5);
    }

    /**
     * Return the 7th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 7th element of the {@code tuple}
     */
    public static <T> T component7(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            T,
                                                                                                                            ?>>>>>>>
                    tuple) {
        return tuple.elementAt(6);
    }

    /**
     * Return the 8th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 8th element of the {@code tuple}
     */
    public static <T> T component8(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            T,
                                                                                                                                            ?>>>>>>>>
                    tuple) {
        return tuple.elementAt(7);
    }

    /**
     * Return the 9th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 9th element of the {@code tuple}
     */
    public static <T> T component9(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            T,
                                                                                                                                                            ?>>>>>>>>>
                    tuple) {
        return tuple.elementAt(8);
    }

    /**
     * Return the 10th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 10th element of the {@code tuple}
     */
    public static <T> T component10(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            T,
                                                                                                                                                                            ?>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(9);
    }

    /**
     * Return the 11th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 11th element of the {@code tuple}
     */
    public static <T> T component11(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            T,
                                                                                                                                                                                            ?>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(10);
    }

    /**
     * Return the 12th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 12th element of the {@code tuple}
     */
    public static <T> T component12(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            T,
                                                                                                                                                                                                            ?>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(11);
    }

    /**
     * Return the 13th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 13th element of the {@code tuple}
     */
    public static <T> T component13(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                            ?>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(12);
    }

    /**
     * Return the 14th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 14th element of the {@code tuple}
     */
    public static <T> T component14(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                                            ?>>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(13);
    }

    /**
     * Return the 15th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 15th element of the {@code tuple}
     */
    public static <T> T component15(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                                                            ?>>>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(14);
    }

    /**
     * Return the 16th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 16th element of the {@code tuple}
     */
    public static <T> T component16(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                                                                            ?>>>>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(15);
    }

    /**
     * Return the 17th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 17th element of the {@code tuple}
     */
    public static <T> T component17(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                                                                                            ?>>>>>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(16);
    }

    /**
     * Return the 18th element of the {@code tuple}.
     *
     * @param <T>   type of the element
     * @param tuple a {@code Tuple}
     * @return the 18th element of the {@code tuple}
     */
    public static <T> T component18(
            @NotNull
                    HList<
                            ?,
                            ? extends
                                    HList<
                                            ?,
                                            ? extends
                                                    HList<
                                                            ?,
                                                            ? extends
                                                                    HList<
                                                                            ?,
                                                                            ? extends
                                                                                    HList<
                                                                                            ?,
                                                                                            ? extends
                                                                                                    HList<
                                                                                                            ?,
                                                                                                            ? extends
                                                                                                                    HList<
                                                                                                                            ?,
                                                                                                                            ? extends
                                                                                                                                    HList<
                                                                                                                                            ?,
                                                                                                                                            ? extends
                                                                                                                                                    HList<
                                                                                                                                                            ?,
                                                                                                                                                            ? extends
                                                                                                                                                                    HList<
                                                                                                                                                                            ?,
                                                                                                                                                                            ? extends
                                                                                                                                                                                    HList<
                                                                                                                                                                                            ?,
                                                                                                                                                                                            ? extends
                                                                                                                                                                                                    HList<
                                                                                                                                                                                                            ?,
                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                                            ?,
                                                                                                                                                                                                                                                                                            ? extends
                                                                                                                                                                                                                                                                                                    HList<
                                                                                                                                                                                                                                                                                                            T,
                                                                                                                                                                                                                                                                                                            ?>>>>>>>>>>>>>>>>>>
                    tuple) {
        return tuple.elementAt(17);
    }

    public static @NotNull Comparator<Unit> comparator() {
        return (Comparator<Unit> & Serializable) (u1, u2) -> 0;
    }

    public static <T1> @NotNull Comparator<Tuple1<T1>> comparator(@NotNull Comparator<? super T1> c1) {
        return (Comparator<Tuple1<T1>> & Serializable) (t1, t2) -> c1.compare(t1.component1(), t2.component1());
    }

    public static <T1, T2> @NotNull Comparator<Tuple2<T1, T2>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2
    ) {
        return (Comparator<Tuple2<T1, T2>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            return c2.compare(t1._2, t2._2);
        };
    }

    public static <T1, T2, T3> @NotNull Comparator<Tuple3<T1, T2, T3>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3
    ) {
        return (Comparator<Tuple3<T1, T2, T3>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            return c3.compare(t1._3, t2._3);
        };
    }

    public static <T1, T2, T3, T4> @NotNull Comparator<Tuple4<T1, T2, T3, T4>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4
    ) {
        return (Comparator<Tuple4<T1, T2, T3, T4>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            return c4.compare(t1._4, t2._4);
        };
    }

    public static <T1, T2, T3, T4, T5> @NotNull Comparator<Tuple5<T1, T2, T3, T4, T5>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4,
            @NotNull Comparator<? super T5> c5
    ) {
        return (Comparator<Tuple5<T1, T2, T3, T4, T5>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            final int compare4 = c4.compare(t1._4, t2._4);
            if (compare4 != 0) {
                return compare4;
            }
            return c5.compare(t1._5, t2._5);
        };
    }

    public static <T1, T2, T3, T4, T5, T6> @NotNull Comparator<Tuple6<T1, T2, T3, T4, T5, T6>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4,
            @NotNull Comparator<? super T5> c5,
            @NotNull Comparator<? super T6> c6
    ) {
        return (Comparator<Tuple6<T1, T2, T3, T4, T5, T6>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            final int compare4 = c4.compare(t1._4, t2._4);
            if (compare4 != 0) {
                return compare4;
            }
            final int compare5 = c5.compare(t1._5, t2._5);
            if (compare5 != 0) {
                return compare5;
            }
            return c6.compare(t1._6, t2._6);
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7> @NotNull Comparator<Tuple7<T1, T2, T3, T4, T5, T6, T7>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4,
            @NotNull Comparator<? super T5> c5,
            @NotNull Comparator<? super T6> c6,
            @NotNull Comparator<? super T7> c7
    ) {
        return (Comparator<Tuple7<T1, T2, T3, T4, T5, T6, T7>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            final int compare4 = c4.compare(t1._4, t2._4);
            if (compare4 != 0) {
                return compare4;
            }
            final int compare5 = c5.compare(t1._5, t2._5);
            if (compare5 != 0) {
                return compare5;
            }
            final int compare6 = c6.compare(t1._6, t2._6);
            if (compare6 != 0) {
                return compare6;
            }
            return c7.compare(t1._7, t2._7);
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> @NotNull Comparator<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4,
            @NotNull Comparator<? super T5> c5,
            @NotNull Comparator<? super T6> c6,
            @NotNull Comparator<? super T7> c7,
            @NotNull Comparator<? super T8> c8
    ) {
        return (Comparator<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            final int compare4 = c4.compare(t1._4, t2._4);
            if (compare4 != 0) {
                return compare4;
            }
            final int compare5 = c5.compare(t1._5, t2._5);
            if (compare5 != 0) {
                return compare5;
            }
            final int compare6 = c6.compare(t1._6, t2._6);
            if (compare6 != 0) {
                return compare6;
            }
            final int compare7 = c7.compare(t1._7, t2._7);
            if (compare7 != 0) {
                return compare7;
            }
            return c8.compare(t1._8, t2._8);
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> @NotNull Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> comparator(
            @NotNull Comparator<? super T1> c1,
            @NotNull Comparator<? super T2> c2,
            @NotNull Comparator<? super T3> c3,
            @NotNull Comparator<? super T4> c4,
            @NotNull Comparator<? super T5> c5,
            @NotNull Comparator<? super T6> c6,
            @NotNull Comparator<? super T7> c7,
            @NotNull Comparator<? super T8> c8,
            @NotNull Comparator<? super T9> c9
    ) {
        return (Comparator<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> & Serializable) (t1, t2) -> {
            final int compare1 = c1.compare(t1._1, t2._1);
            if (compare1 != 0) {
                return compare1;
            }
            final int compare2 = c2.compare(t1._2, t2._2);
            if (compare2 != 0) {
                return compare2;
            }
            final int compare3 = c3.compare(t1._3, t2._3);
            if (compare3 != 0) {
                return compare3;
            }
            final int compare4 = c4.compare(t1._4, t2._4);
            if (compare4 != 0) {
                return compare4;
            }
            final int compare5 = c5.compare(t1._5, t2._5);
            if (compare5 != 0) {
                return compare5;
            }
            final int compare6 = c6.compare(t1._6, t2._6);
            if (compare6 != 0) {
                return compare6;
            }
            final int compare7 = c7.compare(t1._7, t2._7);
            if (compare7 != 0) {
                return compare7;
            }
            final int compare8 = c8.compare(t1._8, t2._8);
            if (compare8 != 0) {
                return compare8;
            }
            return c9.compare(t1._9, t2._9);
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tuple> @NotNull Comparator<T> comparator(@NotNull Comparator<?>... comparators) {
        final int n = comparators.length;
        switch (n) {
            case 0:
                return (Comparator<T>) comparator();
            case 1:
                return (Comparator<T>) comparator(comparators[0]);
            case 2:
                return (Comparator<T>) comparator(comparators[0], comparators[1]);
            case 3:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2]);
            case 4:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3]);
            case 5:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3], comparators[4]);
            case 6:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3], comparators[4], comparators[5]);
            case 7:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3], comparators[4], comparators[5], comparators[6]);
            case 8:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3], comparators[4], comparators[5], comparators[6], comparators[7]);
            case 9:
                return (Comparator<T>) comparator(comparators[0], comparators[1], comparators[2], comparators[3], comparators[4], comparators[5], comparators[6], comparators[7], comparators[8]);
            default:
                return (Comparator<T>) new ComparatorN(comparators.clone());
        }
    }

    private static final class ComparatorN implements Comparator<Tuple>, Serializable {
        private static final long serialVersionUID = 6666584328927326964L;
        private final @NotNull Comparator<?>[] comparators;

        private ComparatorN(@NotNull Comparator<?>[] comparators) {
            this.comparators = comparators;
        }

        @Override
        public int compare(Tuple o1, Tuple o2) {
            final Comparator<?>[] comparators = this.comparators;
            final int n = comparators.length;
            if (o1.arity() != n || o2.arity() != n) {
                throw new ClassCastException();
            }
            for (int i = 0; i < n; i++) {
                @SuppressWarnings("unchecked") final Comparator<Object> c = (Comparator<Object>) comparators[i];
                final int res = c.compare(o1.elementAt(i), o2.elementAt(i));
                if (res != 0) {
                    return res;
                }
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ComparatorN)) {
                return false;
            }
            ComparatorN cn = (ComparatorN) obj;
            return Arrays.equals(this.comparators, cn.comparators);
        }
    }
}
