package kala.collection.base.primitive;

import kala.collection.base.Growable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.control.primitive.${Type}Option;
import kala.function.*;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.NoSuchElementException;
import java.util.Objects;
<#if IsSpecialized>
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.${Type}Stream;
import java.util.stream.StreamSupport;
</#if>

public interface ${Type}Traversable extends PrimitiveTraversable<${WrapperType}> {

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

    default boolean sameElements(@NotNull ${Type}Traversable other) {
        return iterator().sameElements(other.iterator());
    }

    default boolean sameElements(@NotNull Iterable<?> other) {
        return iterator().sameElements(other.iterator());
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

    default int count(@NotNull ${Type}Predicate predicate) {
        return iterator().count(predicate);
    }

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

    default ${PrimitiveType} fold(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return foldLeft(zero, op);
    }

    default ${PrimitiveType} foldLeft(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return iterator().foldLeft(zero, op);
    }

    default <U> U foldLeftToObj(U zero, @NotNull Obj${Type}BiFunction<U, U> op) {
        return iterator().foldLeftToObj(zero, op);
    }

    default ${PrimitiveType} foldRight(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return iterator().foldRight(zero, op);
    }

    default <U> U foldRightToObj(U zero, @NotNull ${Type}ObjBiFunction<U, U> op) {
        return iterator().foldRightToObj(zero, op);
    }

    default ${PrimitiveType} reduce(@NotNull ${Type}BinaryOperator op) {
        return reduceLeft(op);
    }

    default @Nullable ${WrapperType} reduceOrNull(@NotNull ${Type}BinaryOperator op) {
        return reduceLeftOrNull(op);
    }

    default @NotNull ${Type}Option reduceOption(@NotNull ${Type}BinaryOperator op) {
        return reduceLeftOption(op);
    }

    default ${PrimitiveType} reduceLeft(@NotNull ${Type}BinaryOperator op) {
        return iterator().reduceLeft(op);
    }

    default @Nullable ${WrapperType} reduceLeftOrNull(@NotNull ${Type}BinaryOperator op) {
        return isNotEmpty() ? reduceLeft(op) : null;
    }

    default @NotNull ${Type}Option reduceLeftOption(@NotNull ${Type}BinaryOperator op) {
        return isNotEmpty() ? ${Type}Option.some(reduceLeft(op)) : ${Type}Option.none();
    }

    default ${PrimitiveType} reduceRight(@NotNull ${Type}BinaryOperator op) {
        return iterator().reduceRight(op);
    }

    default @Nullable ${WrapperType} reduceRightOrNull(@NotNull ${Type}BinaryOperator op) {
        return isNotEmpty() ? reduceRight(op) : null;
    }

    default @NotNull ${Type}Option reduceRightOption(@NotNull ${Type}BinaryOperator op) {
        return isNotEmpty() ? ${Type}Option.some(reduceRight(op)) : ${Type}Option.none();
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

    default <R, Builder> R collect(@NotNull ${Type}CollectionFactory<Builder, ? extends R> factory) {
        return ${Type}CollectionFactory.buildBy(factory, this::forEach);
    }

    default <G extends ${Type}Growable> @NotNull G collect(@NotNull G destination) {
        destination.plusAssign(this);
        return destination;
    }

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

    default void forEachBreakable(@NotNull ${Type}Predicate action) {
        Objects.requireNonNull(action);
        ${Type}Iterator it = this.iterator();
        while (it.hasNext()) {
            if (!action.test(it.next${Type}())) {
                break;
            }
        }
    }

    //endregion
}
