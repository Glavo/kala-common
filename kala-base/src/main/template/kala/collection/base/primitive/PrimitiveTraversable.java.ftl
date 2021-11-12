package kala.collection.base.primitive;

import kala.control.primitive.${Type}Option;
import kala.function.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
<#if IsSpecialized>
import java.util.function.*;
</#if>

public interface ${Type}Traversable
        extends PrimitiveTraversable<${WrapperType}, ${Type}Traversable, ${Type}Iterator, ${PrimitiveType}[], ${Type}Option, ${Type}Consumer, ${Type}Predicate> {

    @Override
    @NotNull ${Type}Iterator iterator();

    default ${PrimitiveType} elementAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }

        final int knownSize = this.knownSize();
        if (knownSize >= 0 && index >= knownSize) {
            throw new IndexOutOfBoundsException();
        }

        final ${Type}Iterator it = this.iterator();
        for (int i = 0; i < index; i++) {
            if (it.hasNext()) {
                it.next${Type}();
            } else {
                throw new IndexOutOfBoundsException("index: " + index);
            }
        }
        if (it.hasNext()) {
            return it.next${Type}();
        } else {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }


    //region Element Conditions

    default boolean contains(${PrimitiveType} value) {
        return knownSize() != 0 && iterator().contains(value);
    }

    @Override
    default boolean containsAll(${PrimitiveType} @NotNull [] values) {
        return iterator().containsAll(values);
    }

    default boolean containsAll(@NotNull ${Type}Traversable values) {
<#if Type == 'Boolean'>
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

        if (containsTrue) {
            while (it2.hasNext()) {
                boolean value = it2.nextBoolean();
                if (!value) {
                    return false;
                }
            }
        } else {
            while (it2.hasNext()) {
                boolean value = it2.nextBoolean();
                if (value) {
                    return false;
                }
            }
        }
        return true;
<#else>
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
</#if>
    }

    //endregion

    //region Aggregate Operations

    default ${PrimitiveType} max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    @Override
    default @Nullable ${WrapperType} maxOrNull() {
        return knownSize() == 0 ? null : iterator().maxOrNull();
    }

    @Override
    default @NotNull ${Type}Option maxOption() {
        return knownSize() == 0 ? ${Type}Option.none() : iterator().maxOption();
    }

    default ${PrimitiveType} min() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().min();
    }

    @Override
    default @Nullable ${WrapperType} minOrNull() {
        return knownSize() == 0 ? null : iterator().minOrNull();
    }

    @Override
    default @NotNull ${Type}Option minOption() {
        return knownSize() == 0 ? ${Type}Option.none() : iterator().minOption();
    }

    //endregion

    @Override
    default ${PrimitiveType} @NotNull [] toArray() {
        int s = knownSize();
        if (s == 0) {
            return ${Type}Arrays.EMPTY;
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
    default void forEach(@NotNull ${Type}Consumer action) {
        iterator().forEach(action);
    }

    default <Ex extends Throwable> void forEachChecked(@NotNull Checked${Type}Consumer<Ex> action) throws Ex {
        forEach(action);
    }

    default void forEachUnchecked(@NotNull Checked${Type}Consumer<?> action) {
        forEach(action);
    }

    //endregion
}
