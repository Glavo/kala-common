package kala.tuple;

import org.jetbrains.annotations.NotNull;

/**
 * A tuple of no elements.
 *
 * @author Glavo
 */
public final class Unit implements EmptyTuple {
    private static final long serialVersionUID = 0L;
    public static final int HASH_CODE = 427632945;

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
        return HASH_CODE;
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
