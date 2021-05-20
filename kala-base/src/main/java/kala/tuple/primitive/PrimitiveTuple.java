package kala.tuple.primitive;

import kala.tuple.AnyTuple;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveTuple extends AnyTuple {

    static @NotNull BooleanTuple2 of(boolean b1, boolean b2) {
        return BooleanTuple2.of(b1, b2);
    }

    static @NotNull IntTuple2 of(int i1, int i2) {
        return IntTuple2.of(i1, i2);
    }

    static <T> @NotNull IntObjTuple2<T> of(int i, T t) {
        return IntObjTuple2.of(i, t);
    }

    static @NotNull IntTuple3 of(int i1, int i2, int i3) {
        return IntTuple3.of(i1, i2, i3);
    }
}
