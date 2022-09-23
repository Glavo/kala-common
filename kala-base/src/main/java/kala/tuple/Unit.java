package kala.tuple;

import org.jetbrains.annotations.NotNull;

/**
 * A tuple of no elements.
 *
 * @author Glavo
 */
public final class Unit implements EmptyTuple {
    private static final long serialVersionUID = -3636451611428514248L;

    public static final Unit INSTANCE = new Unit();

    public static @NotNull Unit unit() {
        return Unit.INSTANCE;
    }

    private Unit() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Tuple.HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
