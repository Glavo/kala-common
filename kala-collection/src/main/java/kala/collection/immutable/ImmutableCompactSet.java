package kala.collection.immutable;

import kala.collection.base.GenericArrays;
import kala.collection.mutable.MutableTreeSet;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class ImmutableCompactSet<E>
        extends AbstractImmutableSortedSet<E> implements Serializable {

    private static final long serialVersionUID = 418132517516968465L;

    private static final ImmutableCompactSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new ImmutableCompactSet.Factory<>(Comparator.naturalOrder());

    final Comparator<? super E> comparator;
    final Object[] elements;

    ImmutableCompactSet(Object[] elements) {
        this(null, elements);
    }

    ImmutableCompactSet(Comparator<? super E> comparator, Object[] elements) {
        this.comparator = comparator == null ? (Comparator<E>) Comparator.naturalOrder() : comparator;
        this.elements = elements;
    }

    //region Static Factories

    public static <E extends Comparable<? super E>> @NotNull CollectionFactory<E, ?, ImmutableCompactSet<E>> factory() {
        return (ImmutableCompactSet.Factory<E>) DEFAULT_FACTORY;
    }

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableCompactSet<E>> factory(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return (ImmutableCompactSet.Factory<E>) DEFAULT_FACTORY;
        }
        return new ImmutableCompactSet.Factory<>(comparator);
    }

    public static <E extends Comparable<? super E>> @NotNull Collector<E, ?, ImmutableCompactSet<E>> collector() {
        return factory();
    }

    public static <E> @NotNull Collector<E, ?, ImmutableCompactSet<E>> collector(Comparator<? super E> comparator) {
        return factory(comparator);
    }

    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of() {
        return ((ImmutableCompactSet<E>) DEFAULT_FACTORY.empty());
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(@NotNull E value1) {
        Objects.requireNonNull(value1);
        return new ImmutableCompactSet<>(new Object[]{value1});
    }

    @Contract(value = "_, _ -> new", pure = true)
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

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);

        if (s.isEmpty()) {
            return of();
        } else {
            return new ImmutableCompactSet<>(s.toArray());
        }
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);

        if (s.isEmpty()) {
            return of();
        } else {
            return new ImmutableCompactSet<>(s.toArray());
        }
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4, @NotNull E value5
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);

        if (s.isEmpty()) {
            return of();
        } else {
            return new ImmutableCompactSet<>(s.toArray());
        }
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return ((ImmutableCompactSet<E>) DEFAULT_FACTORY.empty());
        }
        return new ImmutableCompactSet<>(comparator, GenericArrays.EMPTY_OBJECT_ARRAY);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        if (comparator == null) {
            comparator = ((Comparator<E>) Comparator.naturalOrder());
        }
        //noinspection ResultOfMethodCallIgnored,EqualsWithItself
        comparator.compare(value1, value1); // check value
        return new ImmutableCompactSet<>(comparator, new Object[]{value1});
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        if (comparator == null) {
            comparator = ((Comparator<E>) Comparator.naturalOrder());
        }
        int c = comparator.compare(value1, value2);

        if (c < 0) {
            return new ImmutableCompactSet<>(comparator, new Object[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableCompactSet<>(comparator, new Object[]{value2, value1});
        }
        return new ImmutableCompactSet<>(comparator, new Object[]{value1});
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);

        if (s.isEmpty()) {
            return of(comparator);
        } else {
            return new ImmutableCompactSet<>(comparator, s.toArray());
        }
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);

        if (s.isEmpty()) {
            return of(comparator);
        } else {
            return new ImmutableCompactSet<>(comparator, s.toArray());
        }
    }

    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
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

        if (s.isEmpty()) {
            return of(comparator);
        } else {
            return new ImmutableCompactSet<>(comparator, s.toArray());
        }
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(@NotNull kala.collection.SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        if (values.isEmpty()) {
            return of(comparator);
        }
        return new ImmutableCompactSet<>(comparator, values.toArray());
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(@NotNull java.util.SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        if (values.isEmpty()) {
            return of(comparator);
        }
        return new ImmutableCompactSet<>(comparator, values.toArray());
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return of();
        }
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return new ImmutableCompactSet<>(s.toArray());
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull ImmutableCompactSet<E> from(
            @NotNull Iterable<@NotNull ? extends E> values
    ) {
        Objects.requireNonNull(values);

        if (values instanceof kala.collection.SortedSet<?>) {
            kala.collection.SortedSet<E> vs = (kala.collection.SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();

            if (vs.isEmpty()) {
                return of(comparator);
            }
            return new ImmutableCompactSet<>(comparator, vs.toArray());
        } else if (values instanceof java.util.SortedSet<?>) {
            java.util.SortedSet<E> vs = (java.util.SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();

            if (vs.isEmpty()) {
                return of(comparator);
            }
            return new ImmutableCompactSet<>(comparator, vs.toArray());
        }

        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return of();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(s.toArray());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        if (values.length == 0) {
            return of(comparator);
        }
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return new ImmutableCompactSet<>(comparator, s.toArray());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableCompactSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return of(comparator);
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableCompactSet<>(comparator, s.toArray());
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableCompactSet";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableCompactSet<U>> iterableFactory() {
        return ((ImmutableCompactSet.Factory<U>) DEFAULT_FACTORY);
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableCompactSet<U>> iterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements);
    }

    //endregion

    @Override
    public final int size() {
        return elements.length;
    }

    @Override
    public final int knownSize() {
        return size();
    }

    @Override
    public final @NotNull ImmutableCompactSet<E> added(E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        Comparator<? super E> comparator = this.comparator;

        if (size == 0) {
            //noinspection ResultOfMethodCallIgnored,EqualsWithItself
            comparator.compare(value, value); // check value
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
    public final @NotNull ImmutableCompactSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        final Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return this;
        }

        final Comparator<? super E> comparator = this.comparator;
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
    public final @NotNull ImmutableCompactSet<E> addedAll(E @NotNull [] values) {
        final int arrayLength = values.length;
        if (arrayLength == 0) {
            return this;
        }
        if (arrayLength == 1) {
            return added(values[0]);
        }

        final Comparator<? super E> comparator = this.comparator;
        final Object[] elements = this.elements;
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
    public final E first() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[0];
    }

    @Override
    public final E last() {
        final Object[] elements = this.elements;
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[size - 1];
    }

    @Override
    public final <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return GenericArrays.joinTo(elements, buffer, separator, prefix, postfix);
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        final Object[] elements = this.elements;
        for (Object e : elements) {
            action.accept((E) e);
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableTreeSet<E>, ImmutableCompactSet<E>> {
        @NotNull
        final Comparator<? super E> comparator;

        final ImmutableCompactSet<E> empty;

        Factory(@NotNull Comparator<? super E> comparator) {
            this.comparator = comparator;
            this.empty = new ImmutableCompactSet<>(comparator, GenericArrays.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public final MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }

        @Override
        public final ImmutableCompactSet<E> build(@NotNull MutableTreeSet<E> builder) {
            if (builder.isEmpty()) {
                return empty;
            }
            return new ImmutableCompactSet<>(comparator, builder.toArray());
        }

        @Override
        public final ImmutableCompactSet<E> empty() {
            return empty;
        }

        @Override
        public final void addToBuilder(@NotNull MutableTreeSet<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public final MutableTreeSet<E> mergeBuilder(@NotNull MutableTreeSet<E> builder1, @NotNull MutableTreeSet<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
