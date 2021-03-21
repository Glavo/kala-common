package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.base.Growable;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.internal.CollectionHelper;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.Collection;
import org.glavo.kala.collection.Set;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collector;

public interface MutableSet<E> extends MutableCollection<E>, Set<E>, Growable<E> {

    //region Static Factories

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, MutableSet<E>> factory() {
        return CollectionFactory.narrow(MutableHashSet.factory());
    }

    static <E> @NotNull Collector<E, ?, MutableSet<E>> collector() {
        return factory();
    }

    @Contract(value = "-> new", pure = true)
    static <E> @NotNull MutableSet<E> of() {
        return MutableHashSet.of();
    }

    @Contract(value = "_ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E value1) {
        return MutableHashSet.of(value1);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E value1, E value2) {
        return MutableHashSet.of(value1, value2);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3) {
        return MutableHashSet.of(value1, value2, value3);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3, E value4) {
        return MutableHashSet.of(value1, value2, value3, value4);
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableHashSet.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    @Contract(value = "_ -> new", pure = true)
    static <E> @NotNull MutableSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    static <E> @NotNull MutableSet<E> from(E @NotNull [] values) {
        return MutableHashSet.from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    static <E> @NotNull MutableSet<E> from(@NotNull Iterable<? extends E> values) {
        return MutableHashSet.from(values);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "MutableSet";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableSet<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull MutableSetEditor<E, ? extends MutableSet<E>> edit() {
        return new MutableSetEditor<>(this);
    }

    @Override
    default @NotNull java.util.Set<E> asJava() {
        return new AsJavaConvert.MutableSetAsJava<>(this);
    }

    @Override
    default @NotNull MutableSet<E> asSynchronized() {
        return new Synchronized.SynchronizedSet<>(this);
    }

    @Override
    default @NotNull MutableSet<E> asSynchronized(@NotNull Object mutex) {
        Objects.requireNonNull(mutex);
        return new Synchronized.SynchronizedSet<>(this, mutex);
    }

    //endregion

    @Contract(mutates = "this")
    boolean add(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        Objects.requireNonNull(values);
        return addAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean addAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        boolean m = false;
        for (E value : values) {
            if (this.add(value)) {
                m = true;
            }
        }
        return m;
    }

    @Override
    default void plusAssign(E value) {
        add(value);
    }

    @Override
    default void plusAssign(E @NotNull [] values) {
        addAll(values);
    }

    @Override
    default void plusAssign(@NotNull Iterable<? extends E> values) {
        addAll(values);
    }

    @Contract(mutates = "this")
    boolean remove(Object value);

    @Contract(mutates = "this")
    default boolean removeAll(Object @NotNull [] values) {
        Objects.requireNonNull(values);
        return removeAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull Iterable<?> values) {
        Objects.requireNonNull(values);
        boolean m = false;
        for (Object value : values) {
            if (remove(value)) {
                m = true;
            }
        }
        return m;
    }

    @Contract(mutates = "this")
    void clear();

    @Contract(mutates = "this")
    default boolean retainIf(@NotNull Predicate<? super E> predicate) {
        int oldSize = size();
        filterInPlace(predicate);
        return size() != oldSize;
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default boolean retainAll(@NotNull Iterable<? super E> values) {
        Objects.requireNonNull(values);

        if (isEmpty()) {
            return false;
        }

        Collection<E> t = CollectionHelper.asCollection(values);
        if (t.isEmpty()) {
            return false;
        }

        Object[] arr = toArray();
        boolean m = false;

        for (Object value : arr) {
            if (!t.contains(value)) {
                this.remove((E) value);
                m = true;
            }
        }
        return m;
    }

    @Contract(mutates = "this")
    default boolean retainAll(E @NotNull [] values) {
        return retainAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void filterInPlace(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        if (isEmpty()) {
            return;
        }

        Object[] arr = toArray();
        for (Object e : arr) {
            if (!predicate.test((E) e)) {
                this.remove((E) e);
            }
        }
    }
}
