package kala.collection.base.primitive;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@StaticClass
public final class IntArrays {
    private IntArrays() {
    }

    public static final int[] EMPTY = new int[0];

    private static final IntFunction<int[]> GENERATOR = (IntFunction<int[]> & Serializable) int[]::new;

    //region Static Factories

    @Contract(pure = true)
    public static @NotNull IntFunction<int[]> generator() {
        return GENERATOR;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static int @NotNull [] of(int... values) {
        return values;
    }

    public static int @NotNull [] from(int @NotNull [] values) {
        return values.clone();
    }

    public static int @NotNull [] from(@NotNull IntTraversable values) {
        return values.toArray();
    }

    public static int @NotNull [] from(@NotNull IntIterator it) {
        return it.toArray();
    }

    public static int @NotNull [] from(@NotNull IntStream stream) {
        return stream.toArray();
    }

    //endregion

}
