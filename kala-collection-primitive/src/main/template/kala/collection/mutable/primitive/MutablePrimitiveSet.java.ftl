package kala.collection.mutable.primitive;

import kala.annotations.*;
import kala.collection.primitive.*;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.*;
import kala.function.*;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface Mutable${Type}Set extends MutablePrimitiveSet<${WrapperType}>, ${Type}Set, Mutable${Type}Collection, ${Type}Growable {

    //region Static Factories

    @Contract(pure = true)
    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}Set> factory() {
<#if Type == "Boolean">
        return ${Type}CollectionFactory.narrow(DefaultMutable${Type}Set.factory());
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "-> new")
    static @NotNull Mutable${Type}Set create() {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.create();
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "-> new")
    static @NotNull Mutable${Type}Set of() {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of();
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType} value1) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(value1);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_, _ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(value1, value2);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_, _, _ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(value1, value2, value3);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_, _, _, _ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(value1, value2, value3, value4);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(value1, value2, value3, value4, value5);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_ -> new")
    static @NotNull Mutable${Type}Set of(${PrimitiveType}... values) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.of(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_ -> new")
    static @NotNull Mutable${Type}Set from(${PrimitiveType} @NotNull [] values) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.from(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    @Contract(value = "_ -> new")
    static @NotNull Mutable${Type}Set from(@NotNull ${Type}Traversable values) {
<#if Type == "Boolean">
        return DefaultMutable${Type}Set.from(values);
<#else>
        throw new UnsupportedOperationException(); // TODO
</#if>
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Mutable${Type}Set";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}Set> iterableFactory() {
        return factory();
    }

    @Contract(mutates = "this")
    boolean add(@Flow(targetIsContainer = true) ${PrimitiveType} value);

    @Contract(mutates = "this")
    default boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) ${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        return addAll(${Type}ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean addAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) ${Type}Traversable values
    ) {
        if (values == this) {
            return false;
        }

        boolean m = false;
        ${Type}Iterator it = values.iterator();
        while (it.hasNext()) {
            if (this.add(it.next${Type}()))
                m = true;
        }
        return m;
    }

    @Override
    @ReplaceWith("add(E)")
    default void plusAssign(${PrimitiveType} value) {
        add(value);
    }

    @Override
    @ReplaceWith("addAll(E[])")
    default void plusAssign(${PrimitiveType} @NotNull [] values) {
        addAll(values);
    }

    @Override
    @ReplaceWith("addAll(Iterable)")
    default void plusAssign(@NotNull ${Type}Traversable values) {
        addAll(values);
    }

    @Contract(mutates = "this")
    void clear();

    @Contract(mutates = "this")
    boolean remove(${PrimitiveType} value);

    @Contract(mutates = "this")
    default boolean removeAll(${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        return removeAll(${Type}ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);
        boolean m = false;
        ${Type}Iterator it = values.iterator();
        while (it.hasNext()) {
            if (this.remove(it.next${Type}()))
                m = true;
        }
        return m;
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default boolean removeAll(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);

        final ${PrimitiveType}[] arr = toArray();
        final int oldSize = arr.length;

        for (${PrimitiveType} e : arr) {
            if (predicate.test(e)) {
                this.remove(e);
            }
        }

        return size() != oldSize;
    }

    @Contract(mutates = "this")
    default boolean retainAll(${PrimitiveType} @NotNull [] values) {
        return retainAll(${Type}ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean retainAll(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);

        if (isEmpty()) {
            return false;
        }

        if (values.isEmpty()) {
            return false;
        }

        throw new UnsupportedOperationException(); // TODO

        /*
        final ${PrimitiveType}[] arr = toArray();
        final int oldSize = arr.length;

        for (${PrimitiveType} value : values) {
            if (!t.contains(value)) {
                this.remove(value);
            }
        }
        return size() != oldSize;
         */
    }

    @Contract(mutates = "this")
    default boolean retainAll(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);

        final ${PrimitiveType}[] arr = toArray();
        final int oldSize = arr.length;

        for (${PrimitiveType} e : arr) {
            if (!predicate.test(e)) {
                this.remove(e);
            }
        }

        return size() != oldSize;
    }

    @Contract(mutates = "this")
    @DelegateBy("retainAll(${Type}Predicate)")
    default void filterInPlace(@NotNull ${Type}Predicate predicate) {
        retainAll(predicate);
    }

    @Contract(mutates = "this")
    @DelegateBy("removeAll(${Type}Predicate)")
    default void filterNotInPlace(@NotNull ${Type}Predicate predicate) {
        removeAll(predicate);
    }
}
