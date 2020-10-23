package asia.kala.control;

import asia.kala.iterator.IntIterator;
import asia.kala.traversable.IntTraversable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.IntConsumer;

public final class OptionInt extends OptionAny<Integer> implements IntTraversable {
    private static final long serialVersionUID = -8990024629462620023L;
    private static final int HASH_MAGIC = -818206074;

    public static final OptionInt None = new OptionInt();

    private static final OptionInt[] CACHE = new OptionInt[256];

    static {
        for (int i = 0; i < 256; i++) {
            CACHE[i] = new OptionInt(i - 128);
        }
    }

    private final int value;

    private OptionInt() {
        this.value = 0;
    }

    private OptionInt(int value) {
        this.value = value;
    }

    @NotNull
    public static OptionInt some(int value) {
        if (value >= -128 && value <= 127) {
            return CACHE[value + 128];
        }
        return new OptionInt(value);
    }

    @NotNull
    public static OptionInt none() {
        return None;
    }

    @NotNull
    public static OptionInt of(@Nullable Integer value) {
        return value == null ? None : some(value);
    }

    public final boolean isDefined() {
        return this != None;
    }

    public final boolean isEmpty() {
        return this == None;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OptionInt)) {
            return false;
        }
        if (this == None || o == None) {
            return false;
        }

        return value == ((OptionInt) o).value;
    }

    @Override
    public int hashCode() {
        return HASH_MAGIC + value;
    }

    @Override
    public final String toString() {
        return this == None ? "OptionInt.None" : "OptionInt[" + value + "]";
    }

    private Object writeReplace() {
        return new Data(this == None ? null : value);
    }

    @NotNull
    @Override
    public final IntIterator iterator() {
        return isDefined() ? IntIterator.of(value) : IntIterator.empty();
    }

    @Override
    public final void forEachPrimitive(@NotNull IntConsumer action) {
        if (isDefined()) {
            action.accept(value);
        }
    }

    private static final class Data implements Serializable {
        private static final long serialVersionUID = -2044232156734869349L;
        private final Integer value;

        Data(Integer value) {
            this.value = value;
        }

        final Object readResolve() {
            return value == null ? None : some(value);
        }
    }
}
