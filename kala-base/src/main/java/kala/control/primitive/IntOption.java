package kala.control.primitive;

import kala.collection.base.primitive.IntIterator;
import kala.collection.base.primitive.IntTraversable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.OptionalInt;
import java.util.function.IntConsumer;

public final class IntOption extends PrimitiveOption<Integer> implements IntTraversable {
    private static final long serialVersionUID = -8990024629462620023L;
    private static final int HASH_MAGIC = -818206074;

    public static final IntOption None = new IntOption();

    private static final IntOption[] CACHE = new IntOption[256];

    static {
        for (int i = 0; i < 256; i++) {
            CACHE[i] = new IntOption(i - 128);
        }
    }

    private final int value;

    private IntOption() {
        this.value = 0;
    }

    private IntOption(int value) {
        this.value = value;
    }

    public static @NotNull IntOption some(int value) {
        if (value >= -128 && value <= 127) {
            return CACHE[value + 128];
        }
        return new IntOption(value);
    }

    public static @NotNull IntOption none() {
        return None;
    }

    public static @NotNull IntOption of(int value) {
        return some(value);
    }

    public static @NotNull IntOption of(@Nullable Integer value) {
        return value == null ? None : some(value);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static @NotNull IntOption fromJava(@NotNull OptionalInt optional) {
        return optional.isPresent() ? some(optional.getAsInt()) : none();
    }

    public boolean isDefined() {
        return this != None;
    }

    public boolean isEmpty() {
        return this == None;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntOption)) {
            return false;
        }
        if (this == None || o == None) {
            return false;
        }

        return value == ((IntOption) o).value;
    }

    @Override
    public int hashCode() {
        return HASH_MAGIC + value;
    }

    @Override
    public String toString() {
        return this == None ? "OptionInt.None" : "OptionInt[" + value + "]";
    }

    @Override
    public @NotNull IntIterator iterator() {
        return isDefined() ? IntIterator.of(value) : IntIterator.empty();
    }

    @Override
    public void forEachPrimitive(@NotNull IntConsumer action) {
        if (isDefined()) {
            action.accept(value);
        }
    }

    private Object writeReplace() {
        return new Data(this == None ? null : value);
    }

    private static final class Data implements Serializable {
        private static final long serialVersionUID = -2044232156734869349L;
        private final Integer value;

        Data(Integer value) {
            this.value = value;
        }

        Object readResolve() {
            return value == null ? None : some(value);
        }
    }
}
