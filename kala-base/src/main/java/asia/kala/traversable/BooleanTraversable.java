package asia.kala.traversable;

import asia.kala.control.OptionBoolean;
import asia.kala.function.BooleanConsumer;
import asia.kala.function.BooleanPredicate;
import asia.kala.function.CheckedBooleanConsumer;
import asia.kala.iterator.BooleanIterator;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public interface BooleanTraversable
        extends PrimitiveTraversable<Boolean, BooleanTraversable, BooleanIterator, boolean[], OptionBoolean, BooleanConsumer, BooleanPredicate> {
    @NotNull
    @Override
    BooleanIterator iterator();

    default boolean contains(boolean value) {
        return knownSize() != 0 && iterator().contains(value);
    }

    @Override
    default boolean containsAll(@NotNull BooleanTraversable values) {
        BooleanIterator it1 = this.iterator();
        BooleanIterator it2 = values.iterator();
        if (!it2.hasNext()) {
            return true;
        }
        if (!it1.hasNext()) {
            return false;
        }

        boolean containsTrue = false;
        boolean containsFalse = false;

        while (it1.hasNext()) {
            boolean v = it1.nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                return true;
            }
        }

        if (containsTrue && containsFalse) {
            throw new AssertionError();
        } else if (containsTrue) {
            while (it2.hasNext()) {
                boolean value = it2.nextBoolean();
                if (!value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            while (it2.hasNext()) {
                boolean value = it2.nextBoolean();
                if (value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    @NotNull
    @Override
    default OptionBoolean find(@NotNull BooleanPredicate predicate) {
        return knownSize() == 0 ? OptionBoolean.None : iterator().find(predicate);
    }

    default boolean max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    default boolean min() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().min();
    }

    @Override
    default boolean @NotNull [] toArray() {
        int s = knownSize();
        if (s == 0) {
            return new boolean[0];
        } else if (s > 0) {
            boolean[] arr = new boolean[s];
            int i = 0;
            for (BooleanIterator iterator = this.iterator(); iterator.hasNext(); ) {
                boolean t = iterator.nextBoolean();
                arr[i++] = t;
            }
            return arr;
        } else {
            return iterator().toArray();
        }
    }

    @Override
    default void forEachPrimitive(@NotNull BooleanConsumer action) {
        iterator().forEach(action);
    }

    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedBooleanConsumer<Ex> action) throws Ex {
        forEachPrimitive(action);
    }

    default void forEachUnchecked(@NotNull CheckedBooleanConsumer<?> action) {
        forEachPrimitive(action);
    }

    @Override
    @Deprecated
    default void forEach(@NotNull Consumer<? super Boolean> action) {
        Objects.requireNonNull(action);
        if (action instanceof BooleanConsumer) {
            forEachPrimitive(((BooleanConsumer) action));
        } else {
            forEachPrimitive(action::accept);
        }
    }
}
