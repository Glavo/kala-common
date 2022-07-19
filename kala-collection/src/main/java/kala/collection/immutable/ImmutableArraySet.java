package kala.collection.immutable;

import kala.collection.base.ObjectArrays;
import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.MutableArraySet;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class ImmutableArraySet<E> extends AbstractImmutableSet<E> implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final Factory<?> FACTORY = new Factory<>();

    private static final ImmutableArraySet<?> EMPTY = new ImmutableArraySet<>(ObjectArrays.EMPTY);

    private final Object[] elements;

    private ImmutableArraySet(Object[] elements) {
        this.elements = elements;
    }

    //region Static Factories

    public static <E> CollectionFactory<E, ?, ? extends ImmutableArraySet<E>> factory() {
        return (Factory) FACTORY;
    }

    public static <E> @NotNull ImmutableArraySet<E> empty() {
        return (ImmutableArraySet<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableArraySet<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E value1) {
        return new ImmutableArraySet<>(new Object[]{value1});
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E value1, E value2) {
        if (Objects.equals(value1, value2))
            return of(value1);

        return new ImmutableArraySet<>(new Object[]{value1, value2});
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E value1, E value2, E value3) {
        MutableArraySet<E> builder = new MutableArraySet<>();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        return new ImmutableArraySet<>(builder.toArray());
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E value1, E value2, E value3, E value4) {
        MutableArraySet<E> builder = new MutableArraySet<>();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        builder.add(value4);
        return new ImmutableArraySet<>(builder.toArray());
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableArraySet<E> builder = new MutableArraySet<>();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        builder.add(value4);
        builder.add(value5);
        return new ImmutableArraySet<>(builder.toArray());
    }

    public static <E> @NotNull ImmutableArraySet<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableArraySet<E> from(E @NotNull [] values) {
        if (values.length == 0)
            return empty();

        MutableArraySet<E> builder = new MutableArraySet<>();
        for (E value : values) {
            builder.add(value);
        }
        return new ImmutableArraySet<>(builder.toArray());
    }

    public static <E> @NotNull ImmutableArraySet<E> from(@NotNull Iterable<? extends E> values) {
        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableArraySet<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext())
            return empty();

        MutableArraySet<E> builder = new MutableArraySet<>();
        while (it.hasNext()) {
            builder.add(it.next());
        }
        return new ImmutableArraySet<>(builder.toArray());
    }

    public static <E> @NotNull ImmutableArraySet<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(ImmutableArraySet.<E>factory());
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableArraySet" ;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) ObjectArrays.iterator(elements);
    }

    @Override
    public @NotNull ImmutableSet<E> added(E value) {
        if (contains(value))
            return this;

        Object[] newElements = Arrays.copyOf(elements, elements.length + 1);
        newElements[newElements.length - 1] = value;
        return new ImmutableArraySet<>(newElements);
    }

    @Override
    public @NotNull ImmutableSet<E> removed(E value) {
        int idx = ObjectArrays.indexOf(elements, value);
        if (idx < 0)
            return this;
        if (elements.length == 1)
            return empty();

        Object[] newElements = new Object[elements.length - 1];
        System.arraycopy(elements, 0, newElements, 0, idx);
        System.arraycopy(elements, idx + 1, newElements, idx, elements.length - idx - 1);
        return new ImmutableArraySet<>(newElements);
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableArraySet<E>, ImmutableArraySet<E>> {
        @Override
        public MutableArraySet<E> newBuilder() {
            return new MutableArraySet<>();
        }

        @Override
        public void addToBuilder(@NotNull MutableArraySet<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public MutableArraySet<E> mergeBuilder(@NotNull MutableArraySet<E> builder1, @NotNull MutableArraySet<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }

        @Override
        public ImmutableArraySet<E> build(MutableArraySet<E> builder) {
            if (builder.isEmpty())
                return empty();
            return new ImmutableArraySet<>(builder.toArray());
        }
    }
}
