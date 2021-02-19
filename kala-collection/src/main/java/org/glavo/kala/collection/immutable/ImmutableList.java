package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.control.Option;
import org.glavo.kala.Tuple2;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.mutable.LinkedBuffer;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.AnyTraversable;
import org.glavo.kala.collection.Seq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings("unchecked")
public final class ImmutableList<@Covariant E> extends AbstractImmutableSeq<E>
        implements ImmutableSeq<E>, ImmutableSeqOps<E, ImmutableList<?>, ImmutableList<E>>, Serializable {
    private static final long serialVersionUID = 944030391350569673L;

    private static final ImmutableList.Factory<?> FACTORY = new Factory<>();

    public static final ImmutableList<?> NIL = new ImmutableList<>();

    E head;

    ImmutableList<E> tail;

    private ImmutableList() {
    }

    private ImmutableList(E head) {
        this.head = head;
    }

    private ImmutableList(E head, @NotNull ImmutableList<E> tail) {
        this.head = head;
        this.tail = tail;
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
        return (ImmutableList<E>) NIL;
    }

    public static <E> @NotNull ImmutableList<E> empty() {
        return nil();
    }

    public static <E> @NotNull ImmutableList<E> of() {
        return nil();
    }

    public static <E> @NotNull ImmutableList<E> of(E value1) {
        return new ImmutableList<>(value1, nil());
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2) {
        return new ImmutableList<>(value1,
                new ImmutableList<>(value2, nil()));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3) {
        return new ImmutableList<>(value1,
                new ImmutableList<>(value2,
                        new ImmutableList<>(value3, nil())));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableList<>(value1,
                new ImmutableList<>(value2,
                        new ImmutableList<>(value3,
                                new ImmutableList<>(value4, nil()))));
    }

    public static <E> @NotNull ImmutableList<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableList<>(value1,
                new ImmutableList<>(value2,
                        new ImmutableList<>(value3,
                                new ImmutableList<>(value4,
                                        new ImmutableList<>(value5, nil())))));
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableList<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableList<E> from(E @NotNull [] values) {
        ImmutableList<E> res = nil();
        for (int i = values.length - 1; i >= 0; i--) {
            res = new ImmutableList<>(values[i], res);
        }
        return res;
    }

    public static <E> @NotNull ImmutableList<E> from(@NotNull IndexedSeq<? extends E> values) {
        ImmutableList<E> res = nil();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = res.cons(values.get(i));
        }
        return res;
    }

    public static <E> @NotNull ImmutableList<E> from(@NotNull java.util.List<? extends E> values) {
        if (values.isEmpty()) {
            return empty();
        }
        return values instanceof RandomAccess
                ? fromRandomAccess(values)
                : from(values.iterator());
    }

    private static <E> @NotNull ImmutableList<E> fromRandomAccess(@NotNull java.util.List<? extends E> values) {
        ImmutableList<E> res = nil();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = new ImmutableList<>(values.get(i), res);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableList<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableList<?>) {
            return ((ImmutableList<E>) values);
        }
        return from(values.iterator()); // implicit null check of values
    }

    public static <E> @NotNull ImmutableList<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        ImmutableList<E> cons = new ImmutableList<>(it.next());
        cons.appendIterator(it).tail = nil();
        return cons;
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, E value) {
        ImmutableList<E> res = ImmutableList.nil();
        while (n-- > 0) {
            res = new ImmutableList<>(value, res);
        }
        return res;
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return nil();
        }
        final ImmutableList<E> res = new ImmutableList<>(supplier.get());
        ImmutableList<E> tail = res;

        while (--n > 0) {
            ImmutableList<E> c = new ImmutableList<>(supplier.get());
            tail.tail = c;
            tail = c;
        }
        tail.tail = nil();

        return res;
    }

    public static <E> @NotNull ImmutableList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return nil();
        }
        final ImmutableList<E> res = new ImmutableList<>(init.apply(0));
        ImmutableList<E> tail = res;

        int i = 0;
        while (--n > 0) {
            ImmutableList<E> c = new ImmutableList<>(init.apply(++i));
            tail.tail = c;
            tail = c;
        }
        tail.tail = nil();

        return res;
    }

    //endregion

    private ImmutableList<E> appendIterator(@NotNull Iterator<? extends E> it) {
        ImmutableList<E> node = this;
        while (it.hasNext()) {
            ImmutableList<E> nn = new ImmutableList<>(it.next());
            node.tail = nn;
            node = nn;
        }
        return node;
    }

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableList";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableList<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return this == NIL ? Iterators.empty() : new Itr<>(this);
    }

    //endregion

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return this == NIL;
    }

    @Override
    public final int size() {
        ImmutableList<? extends E> list = this;
        int c = 0;
        while (list != NIL) {
            ++c;
            list = list.tail();
        }
        return c;
    }

    @Override
    public final @Range(from = -1, to = Integer.MAX_VALUE) int knownSize() {
        if (this == NIL) {
            return 0;
        }
        if (tail == NIL) {
            return 1;
        }
        return -1;
    }


    //endregion

    //region Size Compare Operations

    @Override
    public final int sizeCompare(int otherSize) {
        if (otherSize < 0) {
            return 1;
        }
        int i = 0;
        ImmutableList<E> list = this;
        while (list != NIL) {
            if (i == otherSize) {
                return 1;
            }
            list = list.tail;
            ++i;
        }
        return i - otherSize;
    }

    //endregion

    //region Reversal Operations

    @Override
    public final @NotNull ImmutableList<E> reversed() {
        if (this == NIL || this.tail == NIL) {
            return this;
        }

        ImmutableList<? extends E> list = this;
        ImmutableList<E> ans = nil();
        while (list != NIL) {
            ans = new ImmutableList<>(list.head, ans);
            list = list.tail;
        }
        return ans;
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return reversed().iterator();
    }

    //endregion

    //region Element Retrieval Operations

    public final E head() {
        if (this == NIL) {
            throw new NoSuchElementException("ImmutableList.Nil.head()");
        } else {
            return head;
        }
    }

    public final @NotNull Option<E> headOption() {
        return this == NIL ? Option.none() : Option.some(head);
    }

    public final @NotNull ImmutableList<E> tail() {
        if (this == NIL) {
            throw new NoSuchElementException("ImmutableList.Nil.tail()");
        } else {
            return tail;
        }
    }

    public final @NotNull Option<@NotNull ImmutableList<E>> tailOption() {
        return this == NIL ? Option.none() : Option.some(tail);
    }

    @Override
    public final E first() {
        return head();
    }

    @Override
    public final E last() {
        ImmutableList<E> node = this;
        while (node.tail() != NIL) {
            node = node.tail();
        }
        return node.head();
    }

    //endregion

    @Contract("_ -> new")
    public final @NotNull ImmutableList<E> cons(E element) {
        return new ImmutableList<>(element, this);
    }

    @Override
    public final @NotNull ImmutableList<E> appended(E value) {
        if (this == NIL) {
            return of(value);
        }
        if (tail == NIL) {
            return of(this.head, value);
        }
        return appendedImpl(value);
    }

    @Override
    public final @NotNull ImmutableList<E> appendedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of postfix
            return this;
        }
        if (this == NIL) {
            return from(values);
        }
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableList<E> appendedAll(@NotNull Iterable<? extends E> values) {
        if (AnyTraversable.knownSize(values) == 0) {
            return this;
        }
        if (this == NIL) {
            return from(values);
        }
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableList<E> prepended(E value) {
        return cons(value);
    }

    @Override
    public final @NotNull ImmutableList<E> prependedAll(E @NotNull [] values) {
        int prefixLength = values.length; // implicit null check of prefix
        ImmutableList<E> result = this;
        for (int i = prefixLength - 1; i >= 0; i--) {
            result = result.cons(values[i]);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final @NotNull ImmutableList<E> prependedAll(@NotNull Iterable<? extends E> values) {
        if (values instanceof IndexedSeq<?>) {
            IndexedSeq<E> seq = (IndexedSeq<E>) values;
            ImmutableList<E> res = this;
            for (int i = seq.size() - 1; i >= 0; i--) {
                res = res.cons(seq.get(i));
            }
            return res;
        }
        if (values instanceof java.util.List<?> && values instanceof RandomAccess) {
            final List<E> list = (List<E>) values;
            final int listSize = list.size();

            if (listSize == 0) {
                return this;
            }

            ImmutableList<E> res = this;
            for (int i = listSize - 1; i >= 0; i--) {
                res = res.cons(list.get(i));
            }
            return res;
        }
        Iterator<? extends E> it = values.iterator(); // implicit null check of values
        if (!it.hasNext()) {
            return this;
        }
        final ImmutableList<E> res = new ImmutableList<>(it.next());
        res.appendIterator(it).tail = nil();
        return res;
    }

    @Override
    public final @NotNull ImmutableList<E> slice(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
        }
        if (endIndex < 0) {
            throw new IndexOutOfBoundsException("endIndex(" + endIndex + ") < 0");
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > endIndex(" + endIndex + ")");
        }
        if (beginIndex == endIndex) {
            if (sizeLessThan(beginIndex)) {
                throw new IndexOutOfBoundsException();
            }
            return nil();
        }
        if (beginIndex == 0) {
            if (this == NIL) {
                throw new IndexOutOfBoundsException("endIndex(" + endIndex + ") > size(0)");
            }
            return take(endIndex);
        }

        final int ns = endIndex - beginIndex;

        int i = 0;

        ImmutableList<E> list = this;
        while (list != NIL && i < beginIndex) {
            list = list.tail;
            ++i;
        }
        if (i != beginIndex) {
            throw new IndexOutOfBoundsException("beginIndex = " + beginIndex);
        }
        if (ns == 1) {
            return ImmutableList.of(list.head());
        }

        i = 0;
        LinkedBuffer<E> buffer = new LinkedBuffer<>();
        while (list != NIL && i < ns) {
            buffer.append(list.head);
            list = list.tail;
            ++i;
        }
        if (i != ns) {
            throw new IndexOutOfBoundsException("endIndex = " + endIndex);
        }
        return buffer.toImmutableList();
    }

    @Override
    public final @NotNull ImmutableList<E> drop(int n) {
        if (n <= 0 || this == NIL) {
            return this;
        }
        ImmutableList<E> list = this;
        while (list != NIL && n-- > 0) {
            list = list.tail;
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
        while (list != NIL && predicate.test(list.head)) {
            list = list.tail();
        }
        return list;
    }

    @Override
    public final @NotNull ImmutableList<E> take(int n) {
        if (n <= 0 || this == NIL) {
            return nil();
        }
        return takeImpl(n);
    }

    @Override
    public final @NotNull ImmutableList<E> takeLast(int n) {
        if (n <= 0 || this == NIL) {
            return nil();
        }
        return takeLastImpl(n);
    }

    @Override
    public final @NotNull ImmutableList<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return takeWhileImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableList<E> updated(int index, E newValue) {
        return updatedImpl(index, newValue);
    }

    @Override
    public final @NotNull ImmutableList<E> concat(@NotNull Seq<? extends E> other) {
        return concatImpl(other);
    }

    @Override
    public final <U> @NotNull ImmutableList<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        if (this == NIL) {
            return nil();
        }
        LinkedBuffer<U> buffer = new LinkedBuffer<>();
        ImmutableList<E> list = this;
        while (list != NIL) {
            buffer.appendAll(mapper.apply(list.head));
            list = list.tail;
        }
        return buffer.toImmutableList();
    }

    @Override
    public final @NotNull ImmutableList<E> sorted() {
        if (this == NIL) {
            return nil();
        }
        Object[] arr = this.toArray();
        Arrays.sort(arr);
        return (ImmutableList<E>) from(arr);
    }

    @Override
    public final @NotNull ImmutableList<E> sorted(@NotNull Comparator<? super E> comparator) {
        if (this == NIL) {
            return nil();
        }
        Object[] arr = this.toArray();
        Arrays.sort(arr, (Comparator<Object>) comparator);
        return (ImmutableList<E>) from(arr);
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
    public final <U> @NotNull ImmutableList<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zipImpl(other);
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
        return filterNotNullImpl();
    }

    @Override
    public final @NotNull ImmutableList<E> toImmutableList() {
        return this;
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        ImmutableList<E> list = this;
        while (list != NIL) {
            action.accept(list.head);
            list = list.tail;
        }
    }

    @Override
    public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        ImmutableList<E> list = this;
        int i = 0;
        while (list != NIL) {
            action.accept(i++, list.head);
            list = list.tail;
        }
    }

    private Object writeReplace() {
        return this == NIL ? NilReplaced.INSTANCE : this;
    }

    static final class Itr<@Covariant E> implements Iterator<E> {

        private @NotNull ImmutableList<? extends E> list;

        Itr(@NotNull ImmutableList<? extends E> list) {
            this.list = list;
        }

        @Override
        public final boolean hasNext() {
            return list != NIL;
        }

        @Override
        public final E next() {
            if (list == NIL) {
                throw new NoSuchElementException("ImmutableListIterator.next()");
            }

            E v = list.head();
            list = list.tail();
            return v;
        }
    }

    static final class NilReplaced implements Serializable {
        private static final long serialVersionUID = 0L;

        static final NilReplaced INSTANCE = new NilReplaced();

        private NilReplaced() {
        }

        private Object readResolve() {
            return NIL;
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
        public final ImmutableList<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableList.from(it);
        }

        @Override
        public final ImmutableList<E> fill(int n, E value) {
            return ImmutableList.fill(n, value);
        }

        @Override
        public final ImmutableList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ImmutableList.fill(n, supplier);
        }

        @Override
        public final ImmutableList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableList.fill(n, init);
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
