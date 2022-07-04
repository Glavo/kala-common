package kala.collection.base.primitive;

import kala.collection.base.Growable;
import kala.control.primitive.${Type}Option;
import kala.function.*;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.NoSuchElementException;
<#if IsSpecialized>
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.${Type}Stream;
import java.util.stream.StreamSupport;
</#if>

public interface ${Type}Traversable extends PrimitiveTraversable<${WrapperType}> {

    //region Collection Operations

    @Override
    @NotNull ${Type}Iterator iterator();
<#if IsSpecialized>

    @Override
    default @NotNull Spliterator.Of${Type} spliterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Spliterators.empty${Type}Spliterator();
        } else if (ks > 0) {
            return Spliterators.spliterator(iterator(), ks, characteristics());
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), characteristics());
        }
    }

    @Override
    default @NotNull ${Type}Stream stream() {
        return StreamSupport.${PrimitiveType}Stream(spliterator(), false);
    }

    @Override
    default @NotNull ${Type}Stream parallelStream() {
        return StreamSupport.${PrimitiveType}Stream(spliterator(), true);
    }
</#if>

    //endregion

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


    //region Element Retrieval Operations

    default @NotNull ${Type}Option find(@NotNull ${Type}Predicate predicate) {
        return iterator().find(predicate);
    }

    //endregion

    //region Element Conditions

    default boolean contains(${PrimitiveType} value) {
        return knownSize() != 0 && iterator().contains(value);
    }

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

    /**
      * Tests whether any element of this {@code ${Type}Traversable} match the {@code predicate}.
      *
      * @return {@code true} if either any element of this {@code Traversable} match the {@code predicate},
      * otherwise {@code false}
      */
    default boolean anyMatch(@NotNull ${Type}Predicate predicate) {
        return iterator().anyMatch(predicate);
    }

    /**
     * Tests whether all elements of this {@code ${Type}Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either all elements of this {@code Traversable} match the {@code predicate} or
     * the {@code Traversable} is empty, otherwise {@code false}
     */
    default boolean allMatch(@NotNull ${Type}Predicate predicate) {
        return iterator().allMatch(predicate);
    }

    /**
     * Tests whether none elements of this {@code ${Type}Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either none elements of this {@code Traversable} match the {@code predicate} or
     * the {@code Traversable} is empty, otherwise {@code false}
     */
    default boolean noneMatch(@NotNull ${Type}Predicate predicate) {
        return iterator().noneMatch(predicate);
    }

    //endregion

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G filterTo(@NotNull G destination, @NotNull ${Type}Predicate predicate) {
        return iterator().filterTo(destination, predicate);
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G filterNotTo(@NotNull G destination, @NotNull ${Type}Predicate predicate) {
        return iterator().filterNotTo(destination, predicate);
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G mapTo(@NotNull G destination, @NotNull ${Type}UnaryOperator mapper) {
        return iterator().mapTo(destination, mapper);
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapToObjTo(@NotNull G destination, @NotNull ${Type}Function<? extends U> mapper) {
        return iterator().mapToObjTo(destination, mapper);
    }


    //region Aggregate Operations

    default ${PrimitiveType} max() {
        if (knownSize() == 0) {
            throw new NoSuchElementException();
        }
        return iterator().max();
    }

    @Override
    default @Nullable ${WrapperType} maxOrNull() {
        return isNotEmpty() ? max() : null;
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

    //region Copy Operations

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(${PrimitiveType} @NotNull [] dest) {
        return copyToArray(0, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(${PrimitiveType} @NotNull [] dest, int destPos) {
        return copyToArray(0, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(${PrimitiveType} @NotNull [] dest, int destPos, int limit) {
        return copyToArray(0, dest, destPos, limit);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, ${PrimitiveType} @NotNull [] dest) {
        return copyToArray(srcPos, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, ${PrimitiveType} @NotNull [] dest, int destPos) {
        return copyToArray(srcPos, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, ${PrimitiveType} @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + srcPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        if (limit <= 0) {
            return 0;
        }

        final int dl = dest.length; //implicit null check of dest
        if (destPos > dl) {
            return 0;
        }

        final int kn = this.knownSize();
        if (kn >= 0 && srcPos >= kn) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        int n = 0;
        ${Type}Iterator it = this.iterator();
        while (n++ < srcPos) {
            if (it.hasNext()) {
                it.next${Type}();
            } else {
                return 0;
            }
        }

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest[idx++] = it.next${Type}();
        }
        return idx - destPos;
    }

    //endregion

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
