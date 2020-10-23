package asia.kala.collection.immutable;

import asia.kala.collection.SortedSet;
import asia.kala.collection.mutable.MutableSet;
import asia.kala.collection.mutable.MutableTreeSet;
import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public final class ImmutableArraySet<E>
        extends AbstractImmutableSortedSet<E> implements Serializable {

    private static final long serialVersionUID = 418132517516968465L;

    private static final ImmutableArraySet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new ImmutableArraySet.Factory<>(Comparator.naturalOrder());

    final Comparator<? super E> comparator;
    final Object[] elements;

    ImmutableArraySet(Object[] elements) {
        this(null, elements);
    }

    ImmutableArraySet(Comparator<? super E> comparator, Object[] elements) {
        this.comparator = comparator == null ? (Comparator<E>) Comparator.naturalOrder() : comparator;
        this.elements = elements;
    }

    //region Factory methods

    @NotNull
    public static <E extends Comparable<? super E>> CollectionFactory<E, ?, ImmutableArraySet<E>> factory() {
        return (ImmutableArraySet.Factory<E>) DEFAULT_FACTORY;
    }

    @NotNull
    public static <E> CollectionFactory<E, ?, ImmutableArraySet<E>> factory(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return (ImmutableArraySet.Factory<E>) DEFAULT_FACTORY;
        }
        return new ImmutableArraySet.Factory<>(comparator);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of() {
        return ((ImmutableArraySet<E>) DEFAULT_FACTORY.empty());
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of(@NotNull E value1) {
        Objects.requireNonNull(value1);
        return new ImmutableArraySet<>(new Object[]{value1});
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2
    ) {
        int c = value1.compareTo(Objects.requireNonNull(value2));
        if (c < 0) {
            return new ImmutableArraySet<>(new Object[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableArraySet<>(new Object[]{value2, value1});
        }
        return new ImmutableArraySet<>(new Object[]{value1});
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);

        if (s.isEmpty()) {
            return of();
        } else {
            return new ImmutableArraySet<>(s.toArray());
        }
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of(
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
            return new ImmutableArraySet<>(s.toArray());
        }
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<@NotNull E> of(
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
            return new ImmutableArraySet<>(s.toArray());
        }
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return ((ImmutableArraySet<E>) DEFAULT_FACTORY.empty());
        }
        return new ImmutableArraySet<>(comparator, JavaArray.EMPTY_OBJECT_ARRAY);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        if (comparator == null) {
            comparator = ((Comparator<E>) Comparator.naturalOrder());
        }
        //noinspection ResultOfMethodCallIgnored
        comparator.compare(value1, value1); // check value
        return new ImmutableArraySet<>(comparator, new Object[]{value1});
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        if (comparator == null) {
            comparator = ((Comparator<E>) Comparator.naturalOrder());
        }
        int c = comparator.compare(value1, value2);

        if (c < 0) {
            return new ImmutableArraySet<>(comparator, new Object[]{value1, value2});
        }
        if (c > 0) {
            return new ImmutableArraySet<>(comparator, new Object[]{value2, value1});
        }
        return new ImmutableArraySet<>(comparator, new Object[]{value1});
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);

        if (s.isEmpty()) {
            return of(comparator);
        } else {
            return new ImmutableArraySet<>(comparator, s.toArray());
        }
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(
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
            return new ImmutableArraySet<>(comparator, s.toArray());
        }
    }

    @NotNull
    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(
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
            return new ImmutableArraySet<>(comparator, s.toArray());
        }
    }


    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> ImmutableArraySet<E> from(@NotNull SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        if (values.isEmpty()) {
            return of(comparator);
        }
        return new ImmutableArraySet<>(comparator, values.toArray());
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> ImmutableArraySet<E> from(@NotNull java.util.SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        if (values.isEmpty()) {
            return of(comparator);
        }
        return new ImmutableArraySet<>(comparator, values.toArray());
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<E> from(
            @NotNull Iterable<@NotNull ? extends E> values
    ) {
        Objects.requireNonNull(values);

        if (values instanceof SortedSet<?>) {
            SortedSet<E> vs = (SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();

            if (vs.isEmpty()) {
                return of(comparator);
            }
            return new ImmutableArraySet<>(comparator, vs.toArray());
        } else if (values instanceof java.util.SortedSet<?>) {
            java.util.SortedSet<E> vs = (java.util.SortedSet<E>) values;
            final Comparator<E> comparator = (Comparator<E>) vs.comparator();

            if (vs.isEmpty()) {
                return of(comparator);
            }
            return new ImmutableArraySet<>(comparator, vs.toArray());
        }

        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return of();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableArraySet<>(s.toArray());
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> ImmutableArraySet<E> from(E @NotNull [] values) {
        if (values.length == 0) {
            return of();
        }
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return new ImmutableArraySet<>(s.toArray());
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return of(comparator);
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        while (it.hasNext()) {
            s.add(it.next());
        }
        return new ImmutableArraySet<>(comparator, s.toArray());
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> ImmutableArraySet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        if (values.length == 0) {
            return of(comparator);
        }
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return new ImmutableArraySet<>(comparator, s.toArray());
    }

    //endregion

    @NotNull
    @Override
    public final ImmutableArraySet<E> added(E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        Comparator<? super E> comparator = this.comparator;

        if (size == 0) {
            //noinspection ResultOfMethodCallIgnored
            comparator.compare(value, value); // check value
            return new ImmutableArraySet<>(comparator, new Object[]{value});
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
            return new ImmutableArraySet<>(comparator, newElements);
        } else if (idx == size) {
            System.arraycopy(elements, 0, newElements, 0, size);
            newElements[size] = value;
            return new ImmutableArraySet<>(comparator, newElements);
        } else {
            System.arraycopy(elements, 0, newElements, 0, idx);
            System.arraycopy(elements, idx, newElements, idx + 1, size - idx);
            newElements[idx] = value;
            return new ImmutableArraySet<>(comparator, newElements);
        }
    }

    @NotNull
    @Override
    public final ImmutableArraySet<E> addedAll(@NotNull Iterable<? extends E> values) {
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

        return new ImmutableArraySet<>(comparator, builder.toArray());
    }

    @NotNull
    @Override
    public final ImmutableArraySet<E> addedAll(E @NotNull [] values) {
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
        return new ImmutableArraySet<>(comparator, builder.toArray());
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

    //region ImmutableCollection members


    @Override
    public final String className() {
        return "ImmutableArraySet";
    }

    @NotNull
    @Override
    public final Iterator<E> iterator() {
        return (Iterator<E>) JavaArray.iterator(elements);
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, ImmutableArraySet<U>> iterableFactory() {
        return ((ImmutableArraySet.Factory<U>) DEFAULT_FACTORY);
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, ImmutableArraySet<U>> iterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public final int size() {
        return elements.length;
    }

    @NotNull
    @Override
    public final <A extends Appendable> A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return JavaArray.joinTo(elements, buffer, separator, prefix, postfix);
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        final Object[] elements = this.elements;
        for (Object e : elements) {
            action.accept((E) e);
        }
    }

    //endregion

    private static final class Factory<E> implements CollectionFactory<E, MutableTreeSet<E>, ImmutableArraySet<E>> {
        @NotNull
        final Comparator<? super E> comparator;

        final ImmutableArraySet<E> empty;

        Factory(@NotNull Comparator<? super E> comparator) {
            this.comparator = comparator;
            this.empty = new ImmutableArraySet<>(comparator, JavaArray.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public final MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }

        @Override
        public final ImmutableArraySet<E> build(@NotNull MutableTreeSet<E> builder) {
            if (builder.isEmpty()) {
                return empty;
            }
            return new ImmutableArraySet<>(comparator, builder.toArray());
        }

        @Override
        public final ImmutableArraySet<E> empty() {
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
