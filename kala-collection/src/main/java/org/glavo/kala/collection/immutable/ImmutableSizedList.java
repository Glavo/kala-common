package org.glavo.kala.collection.immutable;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.CollectionLike;
import org.glavo.kala.collection.IndexedSeqLike;
import org.glavo.kala.collection.SeqLike;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.mutable.LinkedBuffer;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableSizedList<E> extends AbstractImmutableSeq<E>
        implements ImmutableSeqOps<E, ImmutableSizedList<?>, ImmutableSizedList<E>> {
    private static final Factory<?> FACTORY = new Factory<>();

    private static final ImmutableSizedList<?> EMPTY = new ImmutableSizedList<>(ImmutableList.NIL, 0);

    private final ImmutableList<E> list;
    private final int size;

    ImmutableSizedList(ImmutableList<E> list, int size) {
        this.list = list;
        this.size = size;
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, ImmutableSizedList<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableSizedList<E>> collector() {
        return factory();
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableSizedList<E> empty() {
        return (ImmutableSizedList<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableSizedList<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableSizedList<E> of(E value1) {
        return new ImmutableSizedList<>(ImmutableList.of(value1), 1);
    }

    public static <E> @NotNull ImmutableSizedList<E> of(E value1, E value2) {
        return new ImmutableSizedList<>(ImmutableList.of(value1, value2), 2);
    }

    public static <E> @NotNull ImmutableSizedList<E> of(E value1, E value2, E value3) {
        return new ImmutableSizedList<>(ImmutableList.of(value1, value2, value3), 3);
    }

    public static <E> @NotNull ImmutableSizedList<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableSizedList<>(ImmutableList.of(value1, value2, value3, value4), 4);
    }

    public static <E> @NotNull ImmutableSizedList<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableSizedList<>(ImmutableList.of(value1, value2, value3, value4, value5), 5);
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableSizedList<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableSizedList<E> from(E @NotNull [] values) {
        final int size = values.length;
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedList<>(ImmutableList.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull IndexedSeqLike<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedList<>(ImmutableList.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull java.util.List<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedList<>(ImmutableList.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull ImmutableSizedList<? extends E> values) {
        return (ImmutableSizedList<E>) values.toImmutableSizedList();
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableSizedList<?>) {
            return ((ImmutableSizedList<E>) values);
        }
        if (values instanceof ImmutableList<?>) {
            return ((ImmutableList<E>) values).toImmutableSizedList();
        }

        return from(values.iterator()); // TODO
    }

    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        final ImmutableList<E> res = new ImmutableList<>(it.next());
        ImmutableList<E> t = res;
        int c = 1;
        while (it.hasNext()) {
            ImmutableList<E> nl = new ImmutableList<>(it.next());
            t.tail = nl;
            t = nl;
            c++;
        }
        t.tail = ImmutableList.nil();
        return new ImmutableSizedList<>(res, c);
    }

    public static <E> @NotNull ImmutableSizedList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableSizedList<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        ImmutableList<E> res = ImmutableList.nil();
        for (int i = 0; i < n; i++) {
            res = new ImmutableList<>(value, res);
        }
        return new ImmutableSizedList<>(res, n);
    }

    public static <E> @NotNull ImmutableSizedList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }
        final ImmutableList<E> res = new ImmutableList<>(supplier.get());
        ImmutableList<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableList<E> nl = new ImmutableList<>(supplier.get());
            t.tail = nl;
            t = nl;
        }
        t.tail = ImmutableList.nil();
        return new ImmutableSizedList<>(res, n);
    }

    public static <E> @NotNull ImmutableSizedList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        final ImmutableList<E> res = new ImmutableList<>(init.apply(0));
        ImmutableList<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableList<E> nl = new ImmutableList<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = ImmutableList.nil();
        return new ImmutableSizedList<>(res, n);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableSizedList";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableSizedList<U>> iterableFactory() {
        return ImmutableSizedList.factory();
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return list.iterator();
    }

    //endregion

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return size == 0;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final int knownSize() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public final E get(int index) {
        Conditions.checkElementIndex(index, size);
        ImmutableList<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return list.head;
    }

    @Override
    public final @Nullable E getOrNull(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        ImmutableList<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return list.head;
    }

    @Override
    public final @NotNull Option<E> getOption(int index) {
        if (index < 0 || index >= size) {
            return Option.none();
        }
        ImmutableList<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return Option.some(list.head);
    }

    //endregion

    //region Reversal Operations

    @Override
    public final @NotNull ImmutableSizedList<E> reversed() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        } else {
            return new ImmutableSizedList<>(list.reversed(), size);
        }
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return list.reverseIterator();
    }

    //endregion

    //region Addition Operations

    public final @NotNull ImmutableSizedList<E> cons(E value) {
        return new ImmutableSizedList<>(list.cons(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> prepended(E value) {
        return new ImmutableSizedList<>(list.prepended(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> prependedAll(E @NotNull [] values) {
        return prependedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> prependedAll(@NotNull Iterable<? extends E> values) {
        return prependedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> appended(E value) {
        return new ImmutableSizedList<>(list.appended(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> appendedAll(E @NotNull [] values) {
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> appendedAll(@NotNull Iterable<? extends E> values) {
        return appendedAllImpl(values);
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    public final E first() {
        return list.first();
    }

    @Override
    public final E first(@NotNull Predicate<? super E> predicate) {
        return list.first(predicate);
    }

    @Override
    public final @Nullable E firstOrNull() {
        return list.firstOrNull();
    }

    @Override
    public @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        return list.firstOrNull(predicate);
    }

    @Override
    public @NotNull Option<E> firstOption() {
        return list.firstOption();
    }

    @Override
    public @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        return list.firstOption(predicate);
    }

    @Override
    public final E last() {
        return list.last();
    }

    @Override
    public E last(@NotNull Predicate<? super E> predicate) {
        return list.last(predicate);
    }

    @Override
    public @Nullable E lastOrNull() {
        return list.lastOrNull();
    }

    @Override
    public @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        return list.lastOrNull(predicate);
    }

    @Override
    public @NotNull Option<E> lastOption() {
        return list.lastOption();
    }

    @Override
    public @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        return list.lastOption(predicate);
    }

    //endregion

    //region Element Conditions

    @Override
    public final boolean contains(Object value) {
        return list.contains(value);
    }

    @Override
    public final boolean containsAll(Object @NotNull [] values) {
        return list.containsAll(values);
    }

    @Override
    public final boolean containsAll(@NotNull Iterable<?> values) {
        return list.containsAll(values);
    }

    @Override
    public final boolean sameElements(@NotNull Iterable<?> other) {
        return list.sameElements(other);
    }

    @Override
    public final boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
        return list.sameElements(other, identity);
    }

    @Override
    public final boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        return list.anyMatch(predicate);
    }

    @Override
    public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
        return list.allMatch(predicate);
    }

    @Override
    public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        return list.noneMatch(predicate);
    }

    //endregion

    //region Search Operations

    @Override
    @Contract(pure = true)
    public final int indexOf(Object value) {
        return list.indexOf(value);
    }

    @Override
    @Contract(pure = true)
    public final int indexWhere(@NotNull Predicate<? super E> predicate) {
        return list.indexWhere(predicate);
    }

    @Override
    @Contract(pure = true)
    public final int lastIndexOf(Object value) {
        return list.lastIndexOf(value);
    }

    @Override
    @Contract(pure = true)
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        return list.lastIndexWhere(predicate);
    }

    //endregion

    //region Misc Operations

    @Override
    public final @NotNull ImmutableSizedList<E> slice(int beginIndex, int endIndex) {
        final int size = this.size;
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        if (endIndex == size) {
            if (beginIndex == 0) {
                return this;
            } else {
                ImmutableList<E> list = this.list;
                int n = beginIndex;
                while (n-- > 0) {
                    list = list.tail;
                }
                return new ImmutableSizedList<>(list, size - beginIndex);
            }
        }
        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return ImmutableSizedList.empty();
        }
        return new ImmutableSizedList<>(list.drop(beginIndex).take(ns), ns);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> drop(int n) {
        final int size = this.size;
        if (n <= 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableSizedList.empty();
        }

        ImmutableList<E> list = this.list;
        for (int i = 0; i < n; i++) {
            list = list.tail;
        }
        return new ImmutableSizedList<>(list, size - n);

    }

    @Override
    public final @NotNull ImmutableSizedList<E> dropLast(int n) {
        final int size = this.size;
        if (n <= 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableSizedList.empty();
        }

        final int ns = size - n;
        return new ImmutableSizedList<>(list.take(ns), ns);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int c = 0;

        ImmutableList<E> list = this.list;
        while (list != ImmutableList.NIL && predicate.test(list.head)) {
            list = list.tail();
            c++;
        }

        if (list == ImmutableList.NIL) {
            return ImmutableSizedList.empty();
        }
        if (c == 0) {
            return this;
        }

        return new ImmutableSizedList<>(list, size - c);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> take(int n) {
        if (n <= 0) {
            return ImmutableSizedList.empty();
        }
        final int size = this.size;
        if (n >= size) {
            return this;
        }
        return new ImmutableSizedList<>(list.take(n), n);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> takeLast(int n) {
        if (n <= 0) {
            return ImmutableSizedList.empty();
        }
        final int size = this.size;
        if (n >= size) {
            return this;
        }
        return new ImmutableSizedList<>(list.drop(size - n), n);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        ImmutableList<E> list = this.list;
        if (list == ImmutableList.NIL || !predicate.test(list.head)) {
            return ImmutableSizedList.empty();
        }

        int c = 0;

        final ImmutableList<E> res = new ImmutableList<>(list.head);
        ImmutableList<E> t = res;

        list = list.tail;

        while (true) {
            if (list == ImmutableList.NIL) {
                return this;
            }
            if (!predicate.test(list.head)) {
                break;
            }
            ImmutableList<E> nl = new ImmutableList<>(list.head);
            t.tail = nl;
            t = nl;
            c++;
            list = list.tail;
        }

        t.tail = ImmutableList.nil();
        return new ImmutableSizedList<>(res, c);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> updated(int index, E newValue) {
        final int size = this.size;
        Conditions.checkElementIndex(index, size);
        return new ImmutableSizedList<>(list.updated(index, newValue), size);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> concat(@NotNull SeqLike<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableSizedList<>(ImmutableList.from(other), size);
        }

        ImmutableList<E> list = this.list;
        final ImmutableList<E> res = new ImmutableList<>(list.head);
        ImmutableList<E> t = res;
        list = list.tail;

        while (list != ImmutableList.NIL) {
            ImmutableList<E> nl = new ImmutableList<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < otherSize; i++) {
                ImmutableList<E> nl = new ImmutableList<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = ImmutableList.nil();
            return new ImmutableSizedList<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                ImmutableList<E> nl = new ImmutableList<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = ImmutableList.nil();
            return new ImmutableSizedList<>(res, c);
        }
    }

    @Override
    public final @NotNull ImmutableSizedList<E> concat(java.util.@NotNull List<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableSizedList<>(ImmutableList.from(other), size);
        }

        ImmutableList<E> list = this.list;
        final ImmutableList<E> res = new ImmutableList<>(list.head);
        ImmutableList<E> t = res;
        list = list.tail;

        while (list != ImmutableList.NIL) {
            ImmutableList<E> nl = new ImmutableList<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < otherSize; i++) {
                ImmutableList<E> nl = new ImmutableList<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = ImmutableList.nil();
            return new ImmutableSizedList<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                ImmutableList<E> nl = new ImmutableList<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = ImmutableList.nil();
            return new ImmutableSizedList<>(res, c);
        }
    }

    @Override
    public final @NotNull ImmutableSizedList<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNotImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> filterNotNull() {
        return filterNotNullImpl();
    }

    @Override
    public final <U> @NotNull ImmutableSizedList<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableSizedList.empty();
        }
        return new ImmutableSizedList<>(list.map(mapper), size);
    }

    @Override
    public final <U> @NotNull ImmutableSizedList<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNullImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedList<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableSizedList.empty();
        }
        return new ImmutableSizedList<>(list.mapIndexed(mapper), size);
    }

    @Override
    public final <U> @NotNull ImmutableSizedList<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return mapIndexedNotNullImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedList<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMapImpl(mapper);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> sorted() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableSizedList<>(list.sorted(), size);
    }

    @Override
    public final @NotNull ImmutableSizedList<E> sorted(@NotNull Comparator<? super E> comparator) {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableSizedList<>(list.sorted(comparator), size);
    }

    //endregion

    @Override
    public final @NotNull ImmutableSizedList<E> toImmutableSizedList() {
        return this;
    }

    static final class Factory<E> implements CollectionFactory<E, LinkedBuffer<E>, ImmutableSizedList<E>> {

        @Override
        public final LinkedBuffer<E> newBuilder() {
            return new LinkedBuffer<>();
        }

        @Override
        public final ImmutableSizedList<E> build(LinkedBuffer<E> builder) {
            return builder.toImmutableSizedList();
        }

        @Override
        public void addToBuilder(@NotNull LinkedBuffer<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public final LinkedBuffer<E> mergeBuilder(@NotNull LinkedBuffer<E> builder1, @NotNull LinkedBuffer<E> builder2) {
            return (LinkedBuffer<E>) ImmutableList.Builder.merge(builder1, builder2);
        }
    }
}
