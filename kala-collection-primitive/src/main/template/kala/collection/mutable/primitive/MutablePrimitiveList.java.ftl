package kala.collection.mutable.primitive;

import kala.Conditions;
import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.*;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public interface Mutable${Type}List extends MutablePrimitiveList<${WrapperType}>, Mutable${Type}Seq, ${Type}Growable {
    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}List> factory() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("-> new")
    static @NotNull Mutable${Type}List create() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("-> new")
    static @NotNull Mutable${Type}List of() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}List from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}List from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}List from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }
<#if IsSpecialized>

    /*
    static @NotNull Mutable${Type}List from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
    }
     */
</#if>

    //endregion

    @Override
    default @NotNull String className() {
        return "Mutable${Type}List";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}List> iterableFactory() {
        return Mutable${Type}List.factory();
    }

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) ${PrimitiveType} value);

    @Contract(mutates = "this")
    default void appendAll(@Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        final int length = values.length;
        //noinspection StatementWithEmptyBody
        if (length == 0) {
        } else if (length == 1) {
            this.append(values[0]);
        } else {
            throw new UnsupportedOperationException(); // TODO this.appendAll(ArraySeq.wrap(values));
        }
    }

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (${PrimitiveType} e : this.toArray()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                this.append(e);
            }
        } else {
            values.forEach(this::append);
        }
    }

    @Override
    @ReplaceWith("append(${PrimitiveType})")
    default void plusAssign(${PrimitiveType} value) {
        append(value);
    }

    @Override
    @ReplaceWith("appendAll(${PrimitiveType}[])")
    default void plusAssign(${PrimitiveType} @NotNull [] values) {
        appendAll(values);
    }

    @Override
    @ReplaceWith("appendAll(${Type}Traversable)")
    default void plusAssign(@NotNull ${Type}Traversable values) {
        appendAll(values);
    }

    @Contract(mutates = "this")
    void prepend(${PrimitiveType} value);

    @Contract(mutates = "this")
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO: this.prependAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            ${PrimitiveType}[] arr = values.toArray();
            for (int i = arr.length - 1; i >= 0; i--) {
                this.prepend(arr[i]);
            }
            return;
        }

        if (values instanceof ${Type}Seq && ((${Type}Seq) values).supportsFastRandomAccess()) {
            ${Type}Seq seq = (${Type}Seq) values;
            int s = seq.size();
            for (int i = s - 1; i >= 0; i--) {
                prepend(seq.get(i));
            }
            return;
        }

        ${PrimitiveType}[] cv = values.toArray();
        for (int i = cv.length - 1; i >= 0; i--) {
            prepend(cv[i]);
        }
    }

    @Contract(mutates = "this")
    void insert(int index, @Flow(targetIsContainer = true) ${PrimitiveType} value);

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO: insertAll(index, ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        Objects.requireNonNull(values);
        if (isEmpty() && index != 0) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }

        ${PrimitiveType}[] valuesArray = values.toArray();
        for (${PrimitiveType} e : valuesArray) {
            insert(index++, e);
        }
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    ${PrimitiveType} removeAt(int index);

    default void removeInRange(int beginIndex, int endIndex) {
        int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int rangeLength = endIndex - beginIndex;

        if (rangeLength == 0) {
            return;
        }

        if (rangeLength == size) {
            clear();
            return;
        }

        for (int i = 0; i < rangeLength; i++) {
            this.removeAt(beginIndex);
        }
    }

    @Contract(mutates = "this")
    default ${PrimitiveType} removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(0);
    }

    @Contract(mutates = "this")
    default @Nullable ${WrapperType} removeFirstOrNull() {
        return isEmpty() ? null : removeAt(0);
    }

    @Contract(mutates = "this")
    default @NotNull ${Type}Option removeFirstOption() {
        return isEmpty() ? ${Type}Option.none() : ${Type}Option.some(removeAt(0));
    }

    @Contract(mutates = "this")
    default ${PrimitiveType} removeLast() {
        final int size = this.size();
        if (size == 0) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @Nullable ${PrimitiveType} removeLastOrNull() {
        final int size = this.size();
        return size == 0 ? null : removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @NotNull ${Type}Option removeLastOption() {
        final int size = this.size();
        return size == 0 ? ${Type}Option.none() : ${Type}Option.some(removeAt(size - 1));
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(mutates = "this")
    default boolean retainAll(@NotNull ${Type}Predicate predicate) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(mutates = "this")
    void clear();

    // ---

    @Contract(mutates = "this")
    default void dropInPlace(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(mutates = "this")
    default void takeInPlace(int n) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract(mutates = "this")
    default void filterInPlace(@NotNull ${Type}Predicate predicate) {
        retainAll(predicate);
    }

    @Contract(mutates = "this")
    default void filterNotInPlace(@NotNull ${Type}Predicate predicate) {
        removeAll(predicate);
    }
}
