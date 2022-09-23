package kala.tuple;

import org.jetbrains.annotations.NotNull;

public interface NonEmptyTuple extends Tuple {
    Object head();

    @NotNull Tuple tail();
}
