package org.glavo.kala.control;

import org.glavo.kala.traversable.BooleanIterator;
import org.glavo.kala.traversable.BooleanTraversable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public final class OptionBoolean extends OptionAny<Boolean> implements BooleanTraversable {
    private static final long serialVersionUID = 3226319253753655469L;
    private static final int HASH_MAGIC = 773806848;

    public static final OptionBoolean True = new OptionBoolean(true);
    public static final OptionBoolean False = new OptionBoolean(false);
    public static final OptionBoolean None = new OptionBoolean();

    private final boolean isDefined;
    private final boolean value;
    private final transient int hashCode;
    private final transient String name;

    private OptionBoolean() {
        this.isDefined = false;
        this.value = false;
        this.name = "OptionBoolean.None";
        this.hashCode = HASH_MAGIC;
    }

    private OptionBoolean(boolean value) {
        this.isDefined = true;
        this.value = value;
        this.name = value ? "OptionBoolean.True" : "OptionBoolean.False";
        this.hashCode = HASH_MAGIC + Boolean.hashCode(value);
    }

    public static @NotNull OptionBoolean some(boolean value) {
        return value ? True : False;
    }

    public static @NotNull OptionBoolean none() {
        return None;
    }

    public static @NotNull OptionBoolean of(boolean value) {
        return value ? True : False;
    }

    public static @NotNull OptionBoolean of(@Nullable Boolean value) {
        if (value == null) {
            return None;
        }
        return value ? True : False;
    }

    public static @NotNull OptionBoolean fromOption(@NotNull Option<? extends Boolean> option) {
        if (option.isDefined()) {
            return of(option.get());
        } else {
            return None;
        }
    }

    public final boolean isDefined() {
        return isDefined;
    }

    public final boolean isEmpty() {
        return !isDefined;
    }

    /**
     * Returns the {@code Option}'s value.
     *
     * @return the {@code Option}'s value
     * @throws NoSuchElementException if the {@code Option} is empty
     */
    @Flow(sourceIsContainer = true)
    public final boolean get() {
        if (isDefined) {
            return value;
        } else {
            throw new NoSuchElementException("OptionBoolean.None.get()");
        }
    }

    @Override
    public final @NotNull BooleanIterator iterator() {
        return isDefined ? BooleanIterator.of(value) : BooleanIterator.empty();
    }

    @Override
    public final boolean equals(Object o) {
        return this == o;
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return name;
    }

    private Object readResolve() {
        if (isDefined) {
            return value ? True : False;
        } else {
            return None;
        }
    }
}

