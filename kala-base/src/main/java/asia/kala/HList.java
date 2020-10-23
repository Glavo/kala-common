package asia.kala;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

/**
 * The base class of heterogeneous lists.
 * When the number of elements is 9 or less, use TupleN, otherwise use TupleXXL to store values in an array.
 * Elements are compactly stored in any case.
 *
 * <p>{@code TupleN<T1, T2, ..., Tn>} is equivalent to
 * {@code TList<T1, ? extends TList<T2, ? extends TList<..., , ? extends TList<Tn, Tuple0>>>>}.
 * The latter type notation can type-safely reference a tuple of unlimited length.
 *
 * @param <H> the type of the head element
 * @param <T>
 * @author Glavo
 */
public abstract class HList<@Covariant H, @Covariant T extends Tuple> extends NonEmptyTuple {
    HList() {
    }

    /**
     * Returns the head of this heterogeneous list.
     *
     * @return the head of this heterogeneous list
     */
    public abstract H head();

    /**
     * Returns the tail of this heterogeneous list.
     *
     * @return the tail of this heterogeneous list
     */
    @NotNull
    public abstract T tail();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <HH> @NotNull HList<HH, ? extends HList<H, T>> cons(HH head);
}
