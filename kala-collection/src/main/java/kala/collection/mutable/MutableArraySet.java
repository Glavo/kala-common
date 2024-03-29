package kala.collection.mutable;

import kala.collection.base.OrderedTraversable;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public final class MutableArraySet<E> extends AbstractMutableListSet<E, MutableArrayList<E>> implements OrderedTraversable<E>, Serializable {
    private static final long serialVersionUID = 3355455494869072611L;

    private static final Factory<?> FACTORY = new Factory<>();

    public MutableArraySet() {
        super(new MutableArrayList<>());
    }

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

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableArraySet<E>> {
        @Override
        public MutableArraySet<E> newBuilder() {
            return new MutableArraySet<>();
        }
    }
}
