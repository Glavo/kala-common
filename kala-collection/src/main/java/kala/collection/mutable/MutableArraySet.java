package kala.collection.mutable;

import kala.collection.base.OrderedTraversable;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;

public final class MutableArraySet<E> extends AbstractMutableSet<E> implements OrderedTraversable<E>, Serializable {
    private static final long serialVersionUID = 0L;

    private static final Factory<?> FACTORY = new Factory<>();

    private final MutableArrayList<E> values = new MutableArrayList<>();

    //region Static Factories

    @Contract(pure = true)
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E> @NotNull CollectionFactory<E, ?, MutableArraySet<E>> factory() {
        return (Factory) FACTORY;
    }

    @Contract(value = "-> new")
    public static <E> @NotNull MutableArraySet<E> create() {
        return new MutableArraySet<>();
    }

    @Contract(value = "-> new")
    public static <E> @NotNull MutableArraySet<E> of() {
        return new MutableArraySet<>();
    }

    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E value1) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.add(value1);
        return res;
    }

    @Contract(value = "_, _ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E value1, E value2) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.add(value1);
        res.add(value2);
        return res;
    }

    @Contract(value = "_, _, _ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E value1, E value2, E value3) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        return res;
    }

    @Contract(value = "_, _, _, _ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E value1, E value2, E value3, E value4) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        return res;
    }

    @Contract(value = "_, _, _, _, _ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        res.add(value5);
        return res;
    }

    @SafeVarargs
    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableArraySet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableArraySet<E> from(E @NotNull [] values) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.addAll(values);
        return res;
    }

    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableArraySet<E> from(@NotNull Iterable<? extends E> values) {
        MutableArraySet<E> res = new MutableArraySet<>();
        res.addAll(values);
        return res;
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableArraySet" ;
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableArraySet<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public int knownSize() {
        return values.size();
    }

    //region Modification Operations

    @Override
    public boolean add(E value) {
        if (values.contains(value))
            return false;

        values.append(value);
        return true;
    }

    @Override
    public boolean remove(Object value) {
        return values.remove(value);
    }

    @Override
    public void clear() {
        values.clear();
    }

    //endregion

    //region Element Conditions

    @Override
    public boolean contains(Object value) {
        return values.contains(value);
    }

    //endregion


    @Override
    public Object @NotNull [] toArray() {
        return values.toArray();
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableArraySet<E>> {
        @Override
        public MutableArraySet<E> newBuilder() {
            return new MutableArraySet<>();
        }
    }
}
