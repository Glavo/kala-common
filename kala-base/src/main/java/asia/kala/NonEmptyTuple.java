package asia.kala;

import org.jetbrains.annotations.NotNull;

public abstract class NonEmptyTuple extends Tuple {
    NonEmptyTuple() {
    }

    public abstract Object head();

    @NotNull
    public abstract Tuple tail();
}
