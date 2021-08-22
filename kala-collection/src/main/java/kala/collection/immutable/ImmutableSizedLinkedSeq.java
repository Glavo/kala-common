package kala.collection.immutable;

import kala.collection.IndexedSeqLike;
import kala.control.Option;
import kala.function.CheckedFunction;
import kala.function.CheckedIndexedFunction;
import kala.function.CheckedPredicate;
import kala.function.IndexedFunction;
import kala.Conditions;
import kala.collection.SeqLike;
import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.LinkedBuffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableSizedLinkedSeq<E> extends AbstractImmutableSeq<E>
        implements ImmutableSeqOps<E, ImmutableSizedLinkedSeq<?>, ImmutableSizedLinkedSeq<E>> {
    private static final Factory<?> FACTORY = new Factory<>();

    private static final ImmutableSizedLinkedSeq<?> EMPTY = new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.NIL, 0);

    private final ImmutableLinkedSeq<E> list;
    private final int size;

    ImmutableSizedLinkedSeq(ImmutableLinkedSeq<E> list, int size) {
        this.list = list;
        this.size = size;
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, ImmutableSizedLinkedSeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableSizedLinkedSeq<E>> collector() {
        return factory();
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableSizedLinkedSeq<E> empty() {
        return (ImmutableSizedLinkedSeq<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E value1) {
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.of(value1), 1);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E value1, E value2) {
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.of(value1, value2), 2);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.of(value1, value2, value3), 3);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.of(value1, value2, value3, value4), 4);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.of(value1, value2, value3, value4, value5), 5);
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableSizedLinkedSeq<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(E @NotNull [] values) {
        final int size = values.length;
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull IndexedSeqLike<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull java.util.List<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.from(values), size);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull ImmutableSizedLinkedSeq<? extends E> values) {
        return (ImmutableSizedLinkedSeq<E>) values.toImmutableSizedLinkedSeq();
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableSizedLinkedSeq<?>) {
            return ((ImmutableSizedLinkedSeq<E>) values);
        }
        if (values instanceof ImmutableLinkedSeq<?>) {
            return ((ImmutableLinkedSeq<E>) values).toImmutableSizedLinkedSeq();
        }

        return from(values.iterator()); // TODO
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(it.next());
        ImmutableLinkedSeq<E> t = res;
        int c = 1;
        while (it.hasNext()) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(it.next());
            t.tail = nl;
            t = nl;
            c++;
        }
        t.tail = ImmutableLinkedSeq.nil();
        return new ImmutableSizedLinkedSeq<>(res, c);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        ImmutableLinkedSeq<E> res = ImmutableLinkedSeq.nil();
        for (int i = 0; i < n; i++) {
            res = new ImmutableLinkedSeq<>(value, res);
        }
        return new ImmutableSizedLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(supplier.get());
        ImmutableLinkedSeq<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(supplier.get());
            t.tail = nl;
            t = nl;
        }
        t.tail = ImmutableLinkedSeq.nil();
        return new ImmutableSizedLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableSizedLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(init.apply(0));
        ImmutableLinkedSeq<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = ImmutableLinkedSeq.nil();
        return new ImmutableSizedLinkedSeq<>(res, n);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableSizedList";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableSizedLinkedSeq<U>> iterableFactory() {
        return ImmutableSizedLinkedSeq.factory();
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
        ImmutableLinkedSeq<E> list = this.list;
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
        ImmutableLinkedSeq<E> list = this.list;
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
        ImmutableLinkedSeq<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return Option.some(list.head);
    }

    //endregion

    //region Reversal Operations

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> reversed() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        } else {
            return new ImmutableSizedLinkedSeq<>(list.reversed(), size);
        }
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return list.reverseIterator();
    }

    //endregion

    //region Addition Operations

    public final @NotNull ImmutableSizedLinkedSeq<E> cons(E value) {
        return new ImmutableSizedLinkedSeq<>(list.cons(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> prepended(E value) {
        return new ImmutableSizedLinkedSeq<>(list.prepended(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> prependedAll(E @NotNull [] values) {
        return prependedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        return prependedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> appended(E value) {
        return new ImmutableSizedLinkedSeq<>(list.appended(value), size + 1);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> appendedAll(E @NotNull [] values) {
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
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
    public final @NotNull ImmutableSizedLinkedSeq<E> slice(int beginIndex, int endIndex) {
        final int size = this.size;
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        if (endIndex == size) {
            if (beginIndex == 0) {
                return this;
            } else {
                ImmutableLinkedSeq<E> list = this.list;
                int n = beginIndex;
                while (n-- > 0) {
                    list = list.tail;
                }
                return new ImmutableSizedLinkedSeq<>(list, size - beginIndex);
            }
        }
        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return ImmutableSizedLinkedSeq.empty();
        }
        return new ImmutableSizedLinkedSeq<>(list.drop(beginIndex).take(ns), ns);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> drop(int n) {
        final int size = this.size;
        if (n <= 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableSizedLinkedSeq.empty();
        }

        ImmutableLinkedSeq<E> list = this.list;
        for (int i = 0; i < n; i++) {
            list = list.tail;
        }
        return new ImmutableSizedLinkedSeq<>(list, size - n);

    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> dropLast(int n) {
        final int size = this.size;
        if (n <= 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableSizedLinkedSeq.empty();
        }

        final int ns = size - n;
        return new ImmutableSizedLinkedSeq<>(list.take(ns), ns);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int c = 0;

        ImmutableLinkedSeq<E> list = this.list;
        while (list != ImmutableLinkedSeq.NIL && predicate.test(list.head)) {
            list = list.tail();
            c++;
        }

        if (list == ImmutableLinkedSeq.NIL) {
            return ImmutableSizedLinkedSeq.empty();
        }
        if (c == 0) {
            return this;
        }

        return new ImmutableSizedLinkedSeq<>(list, size - c);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> take(int n) {
        if (n <= 0) {
            return ImmutableSizedLinkedSeq.empty();
        }
        final int size = this.size;
        if (n >= size) {
            return this;
        }
        return new ImmutableSizedLinkedSeq<>(list.take(n), n);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> takeLast(int n) {
        if (n <= 0) {
            return ImmutableSizedLinkedSeq.empty();
        }
        final int size = this.size;
        if (n >= size) {
            return this;
        }
        return new ImmutableSizedLinkedSeq<>(list.drop(size - n), n);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        ImmutableLinkedSeq<E> list = this.list;
        if (list == ImmutableLinkedSeq.NIL || !predicate.test(list.head)) {
            return ImmutableSizedLinkedSeq.empty();
        }

        int c = 0;

        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(list.head);
        ImmutableLinkedSeq<E> t = res;

        list = list.tail;

        while (true) {
            if (list == ImmutableLinkedSeq.NIL) {
                return this;
            }
            if (!predicate.test(list.head)) {
                break;
            }
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;
            c++;
            list = list.tail;
        }

        t.tail = ImmutableLinkedSeq.nil();
        return new ImmutableSizedLinkedSeq<>(res, c);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> updated(int index, E newValue) {
        final int size = this.size;
        Conditions.checkElementIndex(index, size);
        return new ImmutableSizedLinkedSeq<>(list.updated(index, newValue), size);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.from(other), size);
        }

        ImmutableLinkedSeq<E> list = this.list;
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(list.head);
        ImmutableLinkedSeq<E> t = res;
        list = list.tail;

        while (list != ImmutableLinkedSeq.NIL) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < otherSize; i++) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = ImmutableLinkedSeq.nil();
            return new ImmutableSizedLinkedSeq<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = ImmutableLinkedSeq.nil();
            return new ImmutableSizedLinkedSeq<>(res, c);
        }
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> concat(java.util.@NotNull List<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableSizedLinkedSeq<>(ImmutableLinkedSeq.from(other), size);
        }

        ImmutableLinkedSeq<E> list = this.list;
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(list.head);
        ImmutableLinkedSeq<E> t = res;
        list = list.tail;

        while (list != ImmutableLinkedSeq.NIL) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < otherSize; i++) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = ImmutableLinkedSeq.nil();
            return new ImmutableSizedLinkedSeq<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = ImmutableLinkedSeq.nil();
            return new ImmutableSizedLinkedSeq<>(res, c);
        }
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public final @NotNull <Ex extends Throwable> ImmutableSizedLinkedSeq<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filter(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNotImpl(predicate);
    }

    @Override
    public final @NotNull <Ex extends Throwable> ImmutableSizedLinkedSeq<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filterNot(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> filterNotUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> filterNotNull() {
        return filterNotNullImpl();
    }

    @Override
    public @NotNull <U> ImmutableSizedLinkedSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableSizedLinkedSeq<U>) filter(clazz::isInstance));
    }

    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableSizedLinkedSeq.empty();
        }
        return new ImmutableSizedLinkedSeq<>(list.map(mapper), size);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableSizedLinkedSeq<U> mapChecked(@NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableSizedLinkedSeq<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableSizedLinkedSeq.empty();
        }
        return new ImmutableSizedLinkedSeq<>(list.mapIndexed(mapper), size);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableSizedLinkedSeq<U> mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexed(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableSizedLinkedSeq<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexed(mapper);
    }


    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNullImpl(mapper);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableSizedLinkedSeq<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableSizedLinkedSeq<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return mapIndexedNotNullImpl(mapper);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableSizedLinkedSeq<@NotNull U> mapIndexedNotNullChecked(
            @NotNull CheckedIndexedFunction<? super E, @Nullable ? extends U, ? extends Ex> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableSizedLinkedSeq<@NotNull U> mapIndexedNotNullUnchecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMapImpl(mapper);
    }

    @Override
    public final <U, Ex extends Throwable> @NotNull ImmutableSizedLinkedSeq<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableSizedLinkedSeq<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> sorted() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableSizedLinkedSeq<>(list.sorted(), size);
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> sorted(@NotNull Comparator<? super E> comparator) {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableSizedLinkedSeq<>(list.sorted(comparator), size);
    }

    //endregion

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> toImmutableSizedLinkedSeq() {
        return this;
    }

    static final class Factory<E> implements CollectionFactory<E, LinkedBuffer<E>, ImmutableSizedLinkedSeq<E>> {

        @Override
        public final LinkedBuffer<E> newBuilder() {
            return new LinkedBuffer<>();
        }

        @Override
        public final ImmutableSizedLinkedSeq<E> build(LinkedBuffer<E> builder) {
            return builder.toImmutableSizedLinkedSeq();
        }

        @Override
        public void addToBuilder(@NotNull LinkedBuffer<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public final LinkedBuffer<E> mergeBuilder(@NotNull LinkedBuffer<E> builder1, @NotNull LinkedBuffer<E> builder2) {
            return (LinkedBuffer<E>) ImmutableLinkedSeq.Builder.merge(builder1, builder2);
        }
    }
}
