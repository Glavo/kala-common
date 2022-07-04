package kala.collection.immutable;

import kala.collection.base.GenericArrays;
import kala.collection.mutable.MutableTreeSet;
import kala.collection.factory.CollectionFactory;
import kala.internal.ComparableUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableCompactSet<E>
        extends AbstractImmutableSortedSet<E> implements Serializable {

    private static final long serialVersionUID = 418132517516968465L;

    private static final ImmutableCompactSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new ImmutableCompactSet.Factory<>(Comparator.naturalOrder());

    final @Nullable Comparator<? super E> comparator;
    final Object[] elements;

    ImmutableCompactSet(Object[] elements) {
        this(null, elements);
    }

    ImmutableCompactSet(@Nullable Comparator<? super E> comparator, Object[] elements) {
        this.comparator = comparator;
        this.elements = elements;
    }

    //region Static Factories

    public static <E extends Comparable<? super E>> @NotNull CollectionFactory<E, ?, ImmutableCompactSet<E>> factory() {
        return (ImmutableCompactSet.Factory<E>) DEFAULT_FACTORY;
    }

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableCompactSet<E>> factory(Comparator<? super E> comparator) {
        return comparator == null ? (Factory<E>) DEFAULT_FACTORY : new Factory<>(comparator);
    }

    @Contract
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> empty() {
        return ((ImmutableCompactSet<E>) DEFAULT_FACTORY.empty());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of() {
        return empty();
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(@NotNull E value1) {
        Objects.requireNonNull(value1);
        return new ImmutableCompactSet<>(new Object[]{value1});
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2
    ) {
        int c = value1.compareTo(Objects.requireNonNull(value2));
        if (c < 0) {
            return new ImmutableCompactSet<>(new Object[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableCompactSet<>(new Object[]{value2, value1});
        }
        return new ImmutableCompactSet<>(new Object[]{value1});
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);

        return s.isEmpty() ? empty() : new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s.isEmpty() ? empty() : new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4, @NotNull E value5
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s.isEmpty() ? empty() : new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableCompactSet<E> empty(Comparator<? super E> comparator) {
        return comparator == null ? (ImmutableCompactSet<E>) empty() : new ImmutableCompactSet<>(comparator, GenericArrays.EMPTY_OBJECT_ARRAY);
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(Comparator<? super E> comparator) {
        return empty(comparator);
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        if (comparator == null) {
            if (!(value1 instanceof Comparable)) {
                if (value1 == null) {
                    throw new NullPointerException();
                } else {
                    //noinspection RedundantClassCall
                    Comparable.class.cast(value1);
                }
            }
        } else {
            //noinspection ResultOfMethodCallIgnored,EqualsWithItself
            comparator.compare(value1, value1); // check value
        }
        return new ImmutableCompactSet<>(comparator, new Object[]{value1});
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        final int c;
        if (comparator == null) {
            c = ComparableUtils.compare(value1, value2);
        } else {
            c = comparator.compare(value1, value2);
        }

        if (c < 0) {
            return new ImmutableCompactSet<>(comparator, new Object[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableCompactSet<>(comparator, new Object[]{value2, value1});
        }
        return new ImmutableCompactSet<>(comparator, new Object[]{value1});
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, s.toArray());
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, s.toArray());
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4, E value5
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);

        return s.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, s.toArray());
    }

    public static <E> @NotNull ImmutableCompactSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    public static <E> @NotNull ImmutableCompactSet<E> from(kala.collection.@NotNull SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        return values.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, values.toArray());
    }

    public static <E> @NotNull ImmutableCompactSet<E> from(java.util.@NotNull SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        return values.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, values.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty();
        }
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(
            @NotNull Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);

        if (values instanceof kala.collection.SortedSet<?>) {
            kala.collection.SortedSet<E> vs = (kala.collection.SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();
            return vs.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, vs.toArray());
        } else if (values instanceof java.util.SortedSet<?>) {
            java.util.SortedSet<E> vs = (java.util.SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();

            return vs.isEmpty() ? empty(comparator) : new ImmutableCompactSet<>(comparator, vs.toArray());
        }

        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return empty();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(
            @NotNull Iterator<? extends E> it
    ) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(s.toArray());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(
            @NotNull Stream<? extends E> stream
    ) {
        return stream.collect(factory());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        if (values.length == 0) {
            return empty(comparator);
        }
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return new ImmutableCompactSet<>(comparator, s.toArray());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return empty(comparator);
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(comparator, s.toArray());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, @NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty(comparator);
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(comparator, s.toArray());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, @NotNull Stream<? extends E> stream) {
        return stream.collect(factory(comparator));
    }


    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableCompactSet";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableCompactSet<U>> iterableFactory() {
        return ((ImmutableCompactSet.Factory<U>) DEFAULT_FACTORY);
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableCompactSet<U>> iterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public int knownSize() {
        return size();
    }

    @Override
    public @NotNull ImmutableCompactSet<E> added(E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        Comparator<? super E> comparator = this.comparator;

        if (size == 0) {
            if (comparator == null) {
                Objects.requireNonNull(value);
                //noinspection RedundantClassCall
                Comparable.class.cast(value);
            } else {
                //noinspection ResultOfMethodCallIgnored,EqualsWithItself
                comparator.compare(value, value); // check value
            }
            return new ImmutableCompactSet<>(comparator, new Object[]{value});
        }

        int idx = Arrays.binarySearch(elements, value, ((Comparator<Object>) comparator));
        if (idx >= 0) {
            return this;
        }
        idx = -idx - 1;

        Object[] newElements = new Object[size + 1];

        if (idx == 0) {
            System.arraycopy(elements, 0, newElements, 1, size);
            newElements[0] = value;
            return new ImmutableCompactSet<>(comparator, newElements);
        } else if (idx == size) {
            System.arraycopy(elements, 0, newElements, 0, size);
            newElements[size] = value;
            return new ImmutableCompactSet<>(comparator, newElements);
        } else {
            System.arraycopy(elements, 0, newElements, 0, idx);
            System.arraycopy(elements, idx, newElements, idx + 1, size - idx);
            newElements[idx] = value;
            return new ImmutableCompactSet<>(comparator, newElements);
        }
    }

    @Override
    public @NotNull ImmutableCompactSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        final Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return this;
        }

        final Object[] elements = this.elements;

        if (elements.length == 0) {
            return from(comparator, values);
        }

        MutableTreeSet<Object> builder = new MutableTreeSet<>(((Comparator<Object>) comparator));

        builder.addAll(elements);
        builder.addAll(values);
        if (builder.size() == elements.length) {
            return this;
        }

        return new ImmutableCompactSet<>(comparator, builder.toArray());
    }

    @Override
    public @NotNull ImmutableCompactSet<E> addedAll(E @NotNull [] values) {
        final int arrayLength = values.length;
        if (arrayLength == 0) {
            return this;
        }
        if (arrayLength == 1) {
            return added(values[0]);
        }

        final int size = elements.length;

        if (size == 0) {
            return from(comparator, values);
        }

        MutableTreeSet<Object> builder = new MutableTreeSet<>(((Comparator<Object>) comparator));
        builder.addAll(elements);
        builder.addAll(values);
        return new ImmutableCompactSet<>(comparator, builder.toArray());
    }

    @Override
    public E first() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[0];
    }

    @Override
    public E last() {
        final Object[] elements = this.elements;
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[size - 1];
    }

    @Override
    public <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return GenericArrays.joinTo(elements, buffer, separator, prefix, postfix);
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        final Object[] elements = this.elements;
        for (Object e : elements) {
            action.accept((E) e);
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableTreeSet<E>, ImmutableCompactSet<E>> {
        final Comparator<? super E> comparator;

        final ImmutableCompactSet<E> empty;

        Factory(Comparator<? super E> comparator) {
            this.comparator = comparator;
            this.empty = new ImmutableCompactSet<>(comparator, GenericArrays.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }

        @Override
        public ImmutableCompactSet<E> build(@NotNull MutableTreeSet<E> builder) {
            return builder.isEmpty() ? empty : new ImmutableCompactSet<>(comparator, builder.toArray());
        }

        @Override
        public ImmutableCompactSet<E> empty() {
            return empty;
        }

        @Override
        public void addToBuilder(@NotNull MutableTreeSet<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public MutableTreeSet<E> mergeBuilder(@NotNull MutableTreeSet<E> builder1, @NotNull MutableTreeSet<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
