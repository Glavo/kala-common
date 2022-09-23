package kala.tuple;

import kala.annotations.Sealed;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

@Sealed(subclasses = {Unit.class})
public interface EmptyTuple extends Tuple {
    @Override
    default int arity() {
        return 0;
    }

    @Override
    default <U> U elementAt(int index) {
        throw new IndexOutOfBoundsException("EmptyTuple.elementAt()");
    }

    @Override
    default <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        return generator.apply(0);
    }

    @Override
    default <H> @NotNull Tuple1<H> cons(H head) {
        return new Tuple1<>(head);
    }

}
