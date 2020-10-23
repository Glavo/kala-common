package asia.kala.traversable;

import asia.kala.control.OptionInt;
import asia.kala.function.BooleanConsumer;
import asia.kala.function.CheckedBooleanConsumer;
import asia.kala.function.CheckedIntConsumer;
import asia.kala.iterator.IntIterator;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntTraversable
        extends PrimitiveTraversable<Integer, IntTraversable, IntIterator, int[], OptionInt, IntConsumer, IntPredicate> {

    @NotNull
    @Override
    IntIterator iterator();

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

    @Override
    @Deprecated
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
}
