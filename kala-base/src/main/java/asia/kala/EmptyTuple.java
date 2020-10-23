package asia.kala;

import asia.kala.annotations.Sealed;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

@Sealed(subclasses = {Unit.class})
public abstract class EmptyTuple extends Tuple {
    EmptyTuple() {
    }

    @Override
    public final int arity() {
        return 0;
    }

    @Override
    public final <U> U elementAt(int index) {
        throw new IndexOutOfBoundsException("EmptyTuple.elementAt()");
    }

    @NotNull
    @Override
    public final <U> U[] toArray(@NotNull IntFunction<U[]> generator) {
        return generator.apply(0);
    }

    @NotNull
    @Override
    public final <H> Tuple1<H> cons(H head) {
        return new Tuple1<>(head);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return Tuple.HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "()";
    }
}
