package org.glavo.kala.collection.base.primitive;

import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.control.OptionInt;
import org.glavo.kala.function.CheckedIntConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntTraversable
        extends PrimitiveTraversable<Integer, IntTraversable, IntIterator, int[], OptionInt, IntConsumer, IntPredicate> {

    @Override
    @NotNull IntIterator iterator();

    //region Element Conditions

    default boolean contains(int value) {
        return knownSize() != 0 && iterator().contains(value);
    }

    default boolean containsAll(@NotNull IntTraversable values) {
        IntIterator it = values.iterator();
        if (knownSize() == 0) {
            return !it.hasNext();
        }
        while (it.hasNext()) {
            if (!contains(it.nextInt())) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Aggregate Operations

    default int max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    default int min() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().min();
    }

    //endregion

    @Override
    default int @NotNull [] toArray() {
        int s = knownSize();
        if (s == 0) {
            return new int[0];
        } else if (s > 0) {
            int[] arr = new int[s];
            int i = 0;
            for (IntIterator iterator = this.iterator(); iterator.hasNext(); ) {
                int t = iterator.nextInt();
                arr[i++] = t;
            }
            return arr;
        } else {
            return iterator().toArray();
        }
    }

    //region Traverse Operations

    @Override
    @Deprecated
    @DeprecatedReplaceWith("forEachPrimitive(action::accept)")
    default void forEach(@NotNull Consumer<? super Integer> action) {
        if (action instanceof IntConsumer) {
            forEachPrimitive(((IntConsumer) action));
        } else {
            forEachPrimitive(action::accept);
        }
    }

    @Override
    default void forEachPrimitive(@NotNull IntConsumer action) {
        iterator().forEach(action);
    }

    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedIntConsumer<Ex> action) throws Ex {
        forEachPrimitive(action);
    }

    default void forEachUnchecked(@NotNull CheckedIntConsumer<?> action) {
        forEachPrimitive(action);
    }

    //endregion
}
