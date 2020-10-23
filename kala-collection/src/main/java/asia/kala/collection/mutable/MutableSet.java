package asia.kala.collection.mutable;

import asia.kala.collection.*;
import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.collection.internal.CollectionHelper;
import asia.kala.factory.CollectionFactory;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface MutableSet<E> extends MutableCollection<E>, Set<E> {

    //region Factory methods

    @NotNull
    @Contract(pure = true)
    static <E> CollectionFactory<E, ?, ? extends MutableSet<E>> factory() {
        return MutableHashSet.factory();
    }

    @NotNull
    @Contract(value = "-> new", pure = true)
    static <E> MutableSet<E> of() {
        return MutableHashSet.of();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static <E> MutableSet<E> of(E value1) {
        return MutableHashSet.of(value1);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    static <E> MutableSet<E> of(E value1, E value2) {
        return MutableHashSet.of(value1, value2);
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    static <E> MutableSet<E> of(E value1, E value2, E value3) {
        return MutableHashSet.of(value1, value2, value3);
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static <E> MutableSet<E> of(E value1, E value2, E value3, E value4) {
        return MutableHashSet.of(value1, value2, value3, value4);
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    static <E> MutableSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableHashSet.of(value1, value2, value3, value4, value5);
    }

    @NotNull
    @SafeVarargs
    @Contract(value = "_ -> new", pure = true)
    static <E> MutableSet<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static <E> MutableSet<E> from(E @NotNull [] values) {
        return MutableHashSet.from(values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static <E> MutableSet<E> from(@NotNull Iterable<? extends E> values) {
        return MutableHashSet.from(values);
    }

    //endregion

    @Contract(mutates = "this")
    boolean add(@Flow(targetIsContainer = true) E value);

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

    @Contract(mutates = "this")
    default boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        Objects.requireNonNull(values);

        return addAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    boolean remove(E value);

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        boolean m = false;
        for (E value : values) {
            if (remove(value)) {
                m = true;
            }
        }
        return m;
    }

    @Contract(mutates = "this")
    default boolean removeAll(E @NotNull [] values) {
        Objects.requireNonNull(values);
        return removeAll(ArraySeq.wrap(values));
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

        Collection<E> t = CollectionHelper.asTraversable(values);
        if (t.isEmpty()) {
            return false;
        }

        Object[] arr = toArray();
        boolean m = false;

        for (Object value : arr) {
            if (!t.contains((E) value)) {
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

    //region MutableCollection members

    @Override
    default String className() {
        return "MutableSet";
    }

    @Override
    @NotNull
    default <U> CollectionFactory<U, ?, ? extends MutableSet<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    default MutableSetEditor<E, ? extends MutableSet<E>> edit() {
        return new MutableSetEditor<>(this);
    }

    @NotNull
    @Override
    default java.util.Set<E> asJava() {
        return new AsJavaConvert.MutableSetAsJava<>(this);
    }

    //endregion
}
