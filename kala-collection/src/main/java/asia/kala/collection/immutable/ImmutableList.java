package asia.kala.collection.immutable;

import asia.kala.collection.internal.CollectionHelper;
import asia.kala.control.Option;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.collection.*;
import asia.kala.collection.mutable.LinkedBuffer;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedFunction;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ImmutableList<@Covariant E> extends AbstractImmutableSeq<E>
        implements ImmutableSeq<E>, ImmutableSeqOps<E, ImmutableList<?>, ImmutableList<E>>, Serializable {

    private static final ImmutableList.Factory<?> FACTORY = new Factory<>();

    ImmutableList() {
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <E> ImmutableList<E> narrow(ImmutableList<? extends E> list) {
        return (ImmutableList<E>) list;
    }

    //endregion

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> CollectionFactory<E, ?, ImmutableList<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableList<E> nil() {
        return (ImmutableList<E>) Nil.INSTANCE;
    }

    public static <E> @NotNull ImmutableList<E> empty() {
        return nil();
    }

    public static <E> @NotNull ImmutableList<E> of() {
        return nil();
    }

    public static <E> @NotNull ImmutableList<E> of(E value1) {
        return new ImmutableCons<>(value1, nil());
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2) {
        return new ImmutableCons<>(value1, new ImmutableCons<>(value2, nil()));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3) {
        return new ImmutableCons<>(value1, new ImmutableCons<>(value2, new ImmutableCons<>(value3, nil())));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableCons<>(value1, new ImmutableCons<>(value2, new ImmutableCons<>(value3, new ImmutableCons<>(value4, nil()))));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableCons<>(value1, new ImmutableCons<>(value2, new ImmutableCons<>(value3, new ImmutableCons<>(value4, new ImmutableCons<>(value5, nil())))));
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableList<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableList<E> from(E @NotNull [] values) {
        ImmutableList<E> list = nil();
        for (int i = values.length - 1; i >= 0; i--) {
            list = list.cons(values[i]);
        }

        return list;
    }

    public static <E> @NotNull ImmutableList<E> from(@NotNull IndexedSeq<? extends E> values) {
        ImmutableList<E> res = nil();
        for (int i = values.size() - 1; i >= 0; i--) {
            res = res.cons(values.get(i));
        }
        return res;
    }

    public static <E> @NotNull ImmutableList<E> from(@NotNull java.util.List<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        ListIterator<? extends E> it = values.listIterator(size);
        ImmutableList<E> res = nil();
        while (it.hasPrevious()) {
            res = res.cons(it.previous());
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableList<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof ImmutableList<?>) {
            return (ImmutableList<E>) values;
        }
        if (values instanceof IndexedSeq<?>) {
            return from((IndexedSeq<E>) values);
        }
        if (values instanceof java.util.List<?>) {
            return from((java.util.List<E>) values);
        }

        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        buffer.appendAll(values);
        return buffer.toImmutableList();
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, E value) {
        ImmutableList<E> res = ImmutableList.nil();
        while (n > 0) {
            res = res.cons(value);
            --n;
        }
        return res;
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        return ImmutableList.<E>factory().fill(n, supplier);
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, @NotNull IntFunction<? extends E> supplier) {
        return ImmutableList.<E>factory().fill(n, supplier);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableList";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableList<U>> iterableFactory() {
        return factory();
    }

    //endregion

    public abstract E head();

    public abstract @NotNull Option<E> headOption();

    public abstract @NotNull ImmutableList<E> tail();

    public abstract @NotNull Option<@NotNull ImmutableList<E>> tailOption();

    @Contract("_ -> new")
    public final @NotNull ImmutableList<E> cons(E element) {
        return new ImmutableCons<>(element, this);
    }

    //region Positional Access Operations

    //endregion

    @Override
    public final E first() {
        return head();
    }

    @Override
    public final @NotNull ImmutableList<E> updated(int index, E newValue) {
        return updatedImpl(index, newValue);
    }

    @Override
    public final @NotNull ImmutableList<E> prepended(E value) {
        return cons(value);
    }

    @Override
    public final @NotNull ImmutableList<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        IndexedSeq<E> s = CollectionHelper.tryToIndexedSeq(prefix);
        if (s == null) {
            return prependedAllImpl(prefix);
        }


        ImmutableList<E> result = this;
        Iterator<E> it = s.reverseIterator();
        while (it.hasNext()) {
            result = result.prepended(it.next());
        }
        return result;
    }

    @Override
    public final @NotNull ImmutableList<E> prependedAll(E @NotNull [] prefix) {
        int prefixLength = prefix.length; // implicit null check of prefix

        ImmutableList<E> result = this;
        for (int i = prefixLength - 1; i >= 0; i--) {
            result = result.cons(prefix[i]);
        }
        return result;
    }

    @Override
    public final @NotNull ImmutableList<E> appended(E value) {
        return appendedImpl(value);
    }

    @Override
    public final @NotNull ImmutableList<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        if (CollectionHelper.knowSize(postfix) == 0) {
            return this;
        }
        return appendedAllImpl(postfix);
    }

    @Override
    public final @NotNull ImmutableList<E> appendedAll(E @NotNull [] postfix) {
        if (postfix.length == 0) { // implicit null check of postfix
            return this;
        }
        if (isEmpty()) {
            return from(postfix);
        }
        return appendedAllImpl(postfix);
    }

    @Override
    public final @NotNull ImmutableList<E> drop(int n) {
        ImmutableList<E> list = this;
        while (list != Nil.INSTANCE && n-- > 0) {
            list = list.tail();
        }
        return list;
    }

    @Override
    public final @NotNull ImmutableList<E> dropLast(int n) {
        return dropLastImpl(n);
    }

    @Override
    public final @NotNull ImmutableList<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        ImmutableList<E> list = this;
        while (list != Nil.INSTANCE && predicate.test(list.head())) {
            list = list.tail();
        }
        return list;
    }

    @Override
    public final @NotNull ImmutableList<E> take(int n) {
        return takeImpl(n);
    }

    @Override
    public final @NotNull ImmutableList<E> takeLast(int n) {
        return takeLastImpl(n);
    }

    @Override
    public final @NotNull ImmutableList<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return takeWhileImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableList<E> concat(@NotNull Seq<? extends E> other) {
        return concatImpl(other);
    }

    @Override
    public final <U> @NotNull ImmutableList<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMapImpl(mapper);
    }

    @Override
    public final @NotNull ImmutableList<E> sorted() {
        return sortedImpl();
    }

    @Override
    public final @NotNull ImmutableList<E> sorted(@NotNull Comparator<? super E> comparator) {
        return sortedImpl();
    }

    @Override
    public final @NotNull ImmutableList<E> reversed() {
        if (this == Nil.INSTANCE || this.tail() == Nil.INSTANCE) {
            return this;
        }

        ImmutableList<? extends E> list = this;
        ImmutableList<E> ans = nil();
        while (list != Nil.INSTANCE) {
            ans = new ImmutableCons<>(list.head(), ans);
            list = list.tail();
        }
        return ans;
    }

    @Override
    public final <U> @NotNull ImmutableList<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return mapImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableList<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return mapIndexedImpl(mapper);
    }

    @Override
    public final @NotNull ImmutableList<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableList<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNotImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableList<@NotNull E> filterNotNull() {
        return this.filter(Objects::nonNull);
    }

    @Override
    public abstract @NotNull Tuple2<@NotNull ImmutableList<E>, @NotNull ImmutableList<E>> span(@NotNull Predicate<? super E> predicate);


    @Override
    public final @NotNull ImmutableList<E> toImmutableList() {
        return this;
    }

    public static final class Nil extends ImmutableList<Object> {
        private static final long serialVersionUID = -7963313933036451568L;

        static final Nil INSTANCE = new Nil();

        @Override
        public final @NotNull Iterator<Object> iterator() {
            return Iterators.empty();
        }

        @Override
        public final boolean isEmpty() {
            return true;
        }

        @Override
        public final int knownSize() {
            return 0;
        }

        @Override
        public final Object head() {
            throw new NoSuchElementException("ImmutableList.Nil.head()");
        }

        @Override
        public final @NotNull Option<Object> headOption() {
            return Option.none();
        }

        @Override
        public final @NotNull ImmutableList<Object> tail() {
            throw new NoSuchElementException("ImmutableList.Nil.tail()");
        }

        @Override
        public final @NotNull Option<ImmutableList<Object>> tailOption() {
            return Option.none();
        }

        @Override
        public final Object get(int index) {
            throw new NoSuchElementException();
        }

        @Override
        public final @NotNull Option<Object> getOption(int index) {
            return Option.none();
        }

        @Override
        public final Object last() {
            throw new NoSuchElementException();
        }

        @Override
        public final @NotNull Tuple2<@NotNull ImmutableList<Object>, @NotNull ImmutableList<Object>> span(@NotNull Predicate<? super Object> predicate) {
            return new Tuple2<>(ImmutableList.nil(), ImmutableList.nil());
        }

        @Override
        public final @NotNull String toString() {
            return "ImmutableList[]";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public static abstract class Cons<E> extends ImmutableList<E> {
        Cons() {
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return new Itr<>(this);
        }

        @Override
        public final boolean isEmpty() {
            return false;
        }

        @Override
        public final int knownSize() {
            int size = 0;
            ImmutableList<E> list = this;
            while (list != Nil.INSTANCE) {
                list = list.tail();
                ++size;
            }
            return size;
        }

        @Override
        public final @NotNull Option<E> headOption() {
            return Option.some(head());
        }

        @Override
        public final @NotNull Option<ImmutableList<E>> tailOption() {
            return Option.some(tail());
        }

        @Override
        public final @NotNull Tuple2<@NotNull ImmutableList<E>, @NotNull ImmutableList<E>> span(@NotNull Predicate<? super E> predicate) {
            return spanImpl(predicate);
        }

    }

    static final class MutableCons<E> extends Cons<E> {
        private static final long serialVersionUID = 3721401019662509067L;

        E head;

        @NotNull ImmutableList<? extends E> tail;

        MutableCons(E head, @NotNull ImmutableList<? extends E> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public final E head() {
            return head;
        }

        @Override
        public final @NotNull ImmutableList<E> tail() {
            return ImmutableList.narrow(tail);
        }
    }

    static final class ImmutableCons<E> extends Cons<E> {
        private static final long serialVersionUID = -1987307027661389715L;

        final E head;

        final @NotNull ImmutableList<? extends E> tail;

        ImmutableCons(E head, @NotNull ImmutableList<? extends E> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public final E head() {
            return head;
        }

        @Override
        public final @NotNull ImmutableList<E> tail() {
            return ImmutableList.narrow(tail);
        }
    }

    static final class Itr<@Covariant E> implements Iterator<E> {

        private @NotNull ImmutableList<? extends E> list;

        Itr(@NotNull ImmutableList<? extends E> list) {
            this.list = list;
        }

        @Override
        public final boolean hasNext() {
            return list != Nil.INSTANCE;
        }

        @Override
        public final E next() {
            if (list == Nil.INSTANCE) {
                throw new NoSuchElementException("ImmutableListIterator.next()");
            }

            E v = list.head();
            list = list.tail();
            return v;
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, LinkedBuffer<E>, ImmutableList<E>> {
        Factory() {
        }

        @Override
        public final ImmutableList<E> empty() {
            return ImmutableList.empty();
        }

        @Override
        public final ImmutableList<E> from(E @NotNull [] values) {
            return ImmutableList.from(values);
        }

        @Override
        public final ImmutableList<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableList.from(values);
        }

        @Override
        public final LinkedBuffer<E> newBuilder() {
            return new LinkedBuffer<>();
        }

        @Override
        public final void addToBuilder(@NotNull LinkedBuffer<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public final LinkedBuffer<E> mergeBuilder(@NotNull LinkedBuffer<E> builder1, @NotNull LinkedBuffer<E> builder2) {
            if (((ImmutableInternal.LinkedBufferImpl<E>) builder2).first != null) {
                for (E e : ((ImmutableInternal.LinkedBufferImpl<E>) builder2).first) {
                    builder1.append(e);
                }
            }
            return builder1;
        }

        @Override
        public final ImmutableList<E> build(@NotNull LinkedBuffer<E> builder) {
            return builder.toImmutableList();
        }
    }
}
