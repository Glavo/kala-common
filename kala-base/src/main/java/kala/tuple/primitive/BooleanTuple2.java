package kala.tuple.primitive;

import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.annotations.DeprecatedReplaceWith;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DeprecatedIsStillUsed")
public final class BooleanTuple2 implements PrimitiveTuple {
    private static final long serialVersionUID = 3436771981901568081L;
    private static final int HASH_MAGIC = -1290448591;

    private static final BooleanTuple2 TT = new BooleanTuple2(true, true);
    private static final BooleanTuple2 TF = new BooleanTuple2(true, false);
    private static final BooleanTuple2 FT = new BooleanTuple2(false, true);
    private static final BooleanTuple2 FF = new BooleanTuple2(false, false);

    private static final String TT_S = "BooleanTuple2(true, true)";
    private static final String TF_S = "BooleanTuple2(true, false)";
    private static final String FT_S = "BooleanTuple2(false, true)";
    private static final String FF_S = "BooleanTuple2(false, false)";

    public final boolean _1;
    public final boolean _2;

    @Deprecated
    @DeprecatedReplaceWith("of(b1, b2)")
    public BooleanTuple2(boolean b1, boolean b2) {
        _1 = b1;
        _2 = b2;
    }

    public static @NotNull BooleanTuple2 of(boolean b1, boolean b2) {
        if (b1) {
            return b2 ? TT : TF;
        } else {
            return b2 ? FT : FF;
        }
    }

    @Override
    public final int arity() {
        return 2;
    }

    public final boolean component1() {
        return _1;
    }

    public final boolean component2() {
        return _2;
    }

    public final @NotNull Tuple2<@NotNull Boolean, @NotNull Boolean> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooleanTuple2)) {
            return false;
        }
        BooleanTuple2 that = (BooleanTuple2) o;
        return _1 == that._1 &&
                _2 == that._2;
    }

    @Override
    public final int hashCode() {
        return 31 * Boolean.hashCode(_1) + Boolean.hashCode(_2) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        if (_1) {
            return _2 ? TT_S : TF_S;
        } else {
            return _2 ? FT_S : FF_S;
        }
    }

    private Object readResolve() {
        if (_1) {
            return _2 ? TT : TF;
        } else {
            return _2 ? FT : FF;
        }
    }
}
