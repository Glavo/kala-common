package kala.collection.base.primitive;

import kala.annotations.ReplaceWith;
import kala.control.primitive.${Type}Option;
import kala.function.*;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.*;

public interface ${Type}Traversable
        extends PrimitiveTraversable<${WrapperType}, ${Type}Traversable, ${Type}Iterator, ${PrimitiveType}[], ${Type}Option, ${Type}Consumer, ${Type}Predicate> {

    @Override
    @NotNull ${Type}Iterator iterator();

    //region Element Conditions

    default boolean contains(${PrimitiveType} value) {
        return knownSize() != 0 && iterator().contains(value);
    }

    default boolean containsAll(@NotNull ${Type}Traversable values) {
        ${Type}Iterator it = values.iterator();
        if (knownSize() == 0) {
            return !it.hasNext();
        }
        while (it.hasNext()) {
            if (!contains(it.next${Type}())) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Aggregate Operations

    default ${PrimitiveType} max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    default ${PrimitiveType} min() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().min();
    }

    //endregion

    @Override
    default ${PrimitiveType} @NotNull [] toArray() {
        int s = knownSize();
        if (s == 0) {
            return new ${PrimitiveType}[0];
        } else if (s > 0) {
            ${PrimitiveType}[] arr = new ${PrimitiveType}[s];
            int i = 0;
            for (${Type}Iterator iterator = this.iterator(); iterator.hasNext(); ) {
                ${PrimitiveType} t = iterator.next${Type}();
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
    @ReplaceWith("forEachPrimitive(action::accept)")
    default void forEach(@NotNull Consumer<? super ${WrapperType}> action) {
        if (action instanceof ${Type}Consumer) {
            forEachPrimitive(((${Type}Consumer) action));
        } else {
            forEachPrimitive(action::accept);
        }
    }

    @Override
    default void forEachPrimitive(@NotNull ${Type}Consumer action) {
        iterator().forEach(action);
    }

    default <Ex extends Throwable> void forEachChecked(@NotNull Checked${Type}Consumer<Ex> action) throws Ex {
        forEachPrimitive(action);
    }

    default void forEachUnchecked(@NotNull Checked${Type}Consumer<?> action) {
        forEachPrimitive(action);
    }

    //endregion
}
