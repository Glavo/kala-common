package kala.collection.base.primitive;

import kala.control.primitive.BooleanOption;
import kala.function.BooleanConsumer;
import kala.function.BooleanPredicate;
import kala.function.CheckedBooleanConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public interface BooleanTraversable
        extends PrimitiveTraversable<Boolean, BooleanTraversable, BooleanIterator, boolean[], BooleanOption, BooleanConsumer, BooleanPredicate> {
    @Override
    @NotNull BooleanIterator iterator();

    default boolean elementAt(int index) {
        final BooleanIterator it = this.iterator();
        for (int i = 0; i < index; i++) {
            if (it.hasNext()) {
                it.nextBoolean();
            } else {
                throw new IndexOutOfBoundsException("index: " + index);
            }
        }
        if (it.hasNext()) {
            return it.nextBoolean();
        } else {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    //region Element Conditions

    default boolean contains(boolean value) {
        return knownSize() != 0 && iterator().contains(value);
    }

    @Override
    default boolean containsAll(boolean @NotNull [] values) {
        return iterator().containsAll(values);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
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

    @Override
    default boolean sameElements(@NotNull BooleanTraversable other) {
        return this.iterator().sameElements(other.iterator());
    }

    @Override
    default boolean anyMatch(@NotNull BooleanPredicate predicate) {
        return iterator().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(@NotNull BooleanPredicate predicate) {
        return iterator().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(@NotNull BooleanPredicate predicate) {
        return iterator().noneMatch(predicate);
    }

    //endregion

    @Override
    default @NotNull BooleanOption find(@NotNull BooleanPredicate predicate) {
        return knownSize() == 0 ? BooleanOption.None : iterator().find(predicate);
    }

    //region Aggregate Operations

    default boolean max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    @Override
    default @Nullable Boolean maxOrNull() {
        return knownSize() == 0 ? null : iterator().maxOrNull();
    }

    @Override
    default @NotNull BooleanOption maxOption() {
        return knownSize() == 0 ? BooleanOption.none() : iterator().maxOption();
    }

    default boolean min() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().min();
    }

    //endregion

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

    //region Traverse Operations

    @Override
    default void forEachPrimitive(@NotNull BooleanConsumer action) {
        iterator().forEach(action);
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

    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedBooleanConsumer<Ex> action) throws Ex {
        forEachPrimitive(action);
    }

    default void forEachUnchecked(@NotNull CheckedBooleanConsumer<?> action) {
        forEachPrimitive(action);
    }

    //endregion
}
