package kala.collection.immutable;

import kala.collection.IndexedSeqLike;
import kala.collection.Seq;
import kala.collection.SeqLike;
import kala.collection.SeqView;
import kala.collection.base.AnyTraversable;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.Conditions;
import org.glavo.kala.collection.*;
import kala.collection.internal.view.SeqViews;
import kala.collection.mutable.AbstractBuffer;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.collection.mutable.LinkedBuffer;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public final class ImmutableLinkedSeq<@Covariant E> extends AbstractImmutableSeq<E>
        implements ImmutableSeq<E>, ImmutableSeqOps<E, ImmutableLinkedSeq<?>, ImmutableLinkedSeq<E>>, Serializable {
    private static final long serialVersionUID = 944030391350569673L;

    private static final ImmutableLinkedSeq.Factory<?> FACTORY = new Factory<>();

    public static final ImmutableLinkedSeq<?> NIL = new ImmutableLinkedSeq<>();

    E head;

    ImmutableLinkedSeq<E> tail;

    ImmutableLinkedSeq() {
    }

    ImmutableLinkedSeq(E head) {
        this.head = head;
    }

    ImmutableLinkedSeq(E head, @NotNull ImmutableLinkedSeq<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <E> ImmutableLinkedSeq<E> narrow(ImmutableLinkedSeq<? extends E> seq) {
        return (ImmutableLinkedSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> CollectionFactory<E, ?, ImmutableLinkedSeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableLinkedSeq<E>> collector() {
        return factory();
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableLinkedSeq<E> nil() {
        return (ImmutableLinkedSeq<E>) NIL;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> empty() {
        return nil();
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of() {
        return nil();
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1) {
        return new ImmutableLinkedSeq<>(value1, nil());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2) {
        return new ImmutableLinkedSeq<>(value1,
                new ImmutableLinkedSeq<>(value2, nil()));
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableLinkedSeq<>(value1,
                new ImmutableLinkedSeq<>(value2,
                        new ImmutableLinkedSeq<>(value3, nil())));
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableLinkedSeq<>(value1,
                new ImmutableLinkedSeq<>(value2,
                        new ImmutableLinkedSeq<>(value3,
                                new ImmutableLinkedSeq<>(value4, nil()))));
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableLinkedSeq<>(value1,
                new ImmutableLinkedSeq<>(value2,
                        new ImmutableLinkedSeq<>(value3,
                                new ImmutableLinkedSeq<>(value4,
                                        new ImmutableLinkedSeq<>(value5, nil())))));
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableLinkedSeq<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(E @NotNull [] values) {
        ImmutableLinkedSeq<E> res = nil();
        for (int i = values.length - 1; i >= 0; i--) {
            res = new ImmutableLinkedSeq<>(values[i], res);
        }
        return res;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull IndexedSeqLike<? extends E> values) {
        ImmutableLinkedSeq<E> res = nil();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = res.cons(values.get(i));
        }
        return res;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull java.util.List<? extends E> values) {
        if (values.isEmpty()) {
            return empty();
        }
        return values instanceof RandomAccess
                ? fromRandomAccess(values)
                : from(values.iterator());
    }

    private static <E> @NotNull ImmutableLinkedSeq<E> fromRandomAccess(@NotNull java.util.List<? extends E> values) {
        ImmutableLinkedSeq<E> res = nil();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = new ImmutableLinkedSeq<>(values.get(i), res);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableLinkedSeq<?>) {
            return ((ImmutableLinkedSeq<E>) values);
        }
        return from(values.iterator()); // implicit null check of values
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return nil();
        }
        ImmutableLinkedSeq<E> cons = new ImmutableLinkedSeq<>(it.next());
        cons.appendIterator(it).tail = nil();
        return cons;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, E value) {
        ImmutableLinkedSeq<E> res = ImmutableLinkedSeq.nil();
        for (int i = 0; i < n; i++) {
            res = new ImmutableLinkedSeq<>(value, res);
        }
        return res;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return nil();
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(supplier.get());
        ImmutableLinkedSeq<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(supplier.get());
            t.tail = nl;
            t = nl;
        }
        t.tail = nil();
        return res;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return nil();
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(init.apply(0));
        ImmutableLinkedSeq<E> t = res;

        for (int i = 1; i < n; i++) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = nil();
        return res;
    }

    //endregion

    private ImmutableLinkedSeq<E> appendIterator(@NotNull Iterator<? extends E> it) {
        ImmutableLinkedSeq<E> node = this;
        while (it.hasNext()) {
            ImmutableLinkedSeq<E> nn = new ImmutableLinkedSeq<>(it.next());
            node.tail = nn;
            node = nn;
        }
        return node;
    }

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableLinkedSeq";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableLinkedSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        if (this == NIL) {
            return Iterators.empty();
        }
        if (tail == NIL) {
            return Iterators.of(head);
        }
        return new Itr<>(this);
    }

    @Override
    public final @NotNull Iterator<E> iterator(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
        }
        if (beginIndex == 0) {
            return iterator();
        }

        int n = beginIndex;
        ImmutableLinkedSeq<E> list = this;
        while (n-- > 0) {
            if (list == NIL) {
                throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
            }
            list = list.tail;
        }
        return list.iterator();
    }

    @Override
    public final @NotNull SeqView<E> view() {
        if (this == NIL) {
            return SeqView.empty();
        }
        if (tail == NIL) {
            return new SeqViews.Single<>(head);
        }
        return new SeqViews.WithCachedSize<>(this);
    }

    //endregion

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return this == NIL;
    }

    @Override
    public final int size() {
        ImmutableLinkedSeq<? extends E> list = this;
        int c = 0;
        while (list != NIL) {
            ++c;
            list = list.tail();
        }
        return c;
    }

    @Override
    public final int knownSize() {
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
        ImmutableLinkedSeq<E> list = this;
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
    public final @NotNull ImmutableLinkedSeq<E> reversed() {
        if (this == NIL || this.tail == NIL) {
            return this;
        }

        ImmutableLinkedSeq<? extends E> list = this;
        ImmutableLinkedSeq<E> res = nil();
        while (list != NIL) {
            res = new ImmutableLinkedSeq<>(list.head, res);
            list = list.tail;
        }
        return res;
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

    public final @Nullable E headOrNull() {
        return head;
    }

    public final @NotNull Option<E> headOption() {
        return this == NIL ? Option.none() : Option.some(head);
    }

    public final @NotNull ImmutableLinkedSeq<E> tail() {
        if (this == NIL) {
            throw new NoSuchElementException("ImmutableList.Nil.tail()");
        } else {
            return tail;
        }
    }

    public final @Nullable ImmutableLinkedSeq<E> tailOrNull() {
        return tail;
    }

    public final @NotNull Option<@NotNull ImmutableLinkedSeq<E>> tailOption() {
        return Option.of(tail);
    }

    @Override
    public final E first() {
        return head();
    }

    @Override
    public final E last() {
        ImmutableLinkedSeq<E> node = this;
        while (node.tail() != NIL) {
            node = node.tail();
        }
        return node.head();
    }

    //endregion

    @Contract("_ -> new")
    public final @NotNull ImmutableLinkedSeq<E> cons(E element) {
        return new ImmutableLinkedSeq<>(element, this);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> prepended(E value) {
        return cons(value);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> prependedAll(E @NotNull [] values) {
        int prefixLength = values.length; // implicit null check of prefix
        ImmutableLinkedSeq<E> result = this;
        for (int i = prefixLength - 1; i >= 0; i--) {
            result = result.cons(values[i]);
        }
        return result;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        if (values instanceof RandomAccess) {
            if (values instanceof Seq<?>) {
                Seq<E> seq = (Seq<E>) values;
                ImmutableLinkedSeq<E> res = this;
                for (int i = seq.size() - 1; i >= 0; i--) {
                    res = res.cons(seq.get(i));
                }
                return res;
            }
            if (values instanceof java.util.List<?>) {
                final List<E> list = (List<E>) values;
                ImmutableLinkedSeq<E> res = this;
                for (int i = list.size() - 1; i >= 0; i--) {
                    res = res.cons(list.get(i));
                }
                return res;
            }
        }

        Iterator<? extends E> it = values.iterator(); // implicit null check of values
        if (!it.hasNext()) {
            return this;
        }
        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(it.next());
        res.appendIterator(it).tail = this;
        return res;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> appended(E value) {
        if (this == NIL) {
            return of(value);
        }
        if (tail == NIL) {
            return of(this.head, value);
        }
        return appendedImpl(value);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> appendedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of postfix
            return this;
        }
        if (this == NIL) {
            return from(values);
        }
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        if (AnyTraversable.knownSize(values) == 0) {
            return this;
        }
        if (this == NIL) {
            return from(values);
        }
        if (values instanceof ImmutableLinkedSeq) {
            return ((ImmutableLinkedSeq<E>) values).prependedAll(this);
        }
        return appendedAllImpl(values);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> slice(int beginIndex, int endIndex) {
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

        ImmutableLinkedSeq<E> list = this;
        while (list != NIL && i < beginIndex) {
            list = list.tail;
            ++i;
        }
        if (i != beginIndex) {
            throw new IndexOutOfBoundsException("beginIndex = " + beginIndex);
        }
        if (ns == 1) {
            return ImmutableLinkedSeq.of(list.head());
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
        return buffer.toImmutableLinkedSeq();
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> drop(int n) {
        if (n <= 0 || this == NIL) {
            return this;
        }
        ImmutableLinkedSeq<E> list = this;
        while (list != NIL && n-- > 0) {
            list = list.tail;
        }
        return list;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> dropLast(int n) {
        return dropLastImpl(n);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        ImmutableLinkedSeq<E> list = this;
        while (list != NIL && predicate.test(list.head)) {
            list = list.tail();
        }
        return list;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> take(int n) {
        if (n <= 0 || this == NIL) {
            return nil();
        }
        return takeImpl(n);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> takeLast(int n) {
        if (n <= 0) {
            return nil();
        }

        final int size = this.size();
        if (n >= size) {
            return this;
        }

        return drop(size - n);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        ImmutableLinkedSeq<E> list = this;
        if (list == NIL || !predicate.test(list.head)) {
            return nil();
        }

        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(list.head);
        ImmutableLinkedSeq<E> t = res;

        list = list.tail;

        while (true) {
            if (list == NIL) {
                return this;
            }
            if (!predicate.test(list.head)) {
                break;
            }
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;

            list = list.tail;
        }

        t.tail = nil();
        return res;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> updated(int index, E newValue) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index(" + index + ") < 0");
        }
        ImmutableLinkedSeq<E> list = this;
        if (this == NIL) {
            throw new IndexOutOfBoundsException("index(" + index + ") >= size(" + 0 + ")");
        }
        if (index == 0) {
            return new ImmutableLinkedSeq<>(newValue, list.tail);
        }

        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(list.head);
        ImmutableLinkedSeq<E> t = res;
        list = list.tail;

        for (int i = 1; i < index; i++) {
            if (list == NIL) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;

            list = list.tail;
        }
        if (list == NIL) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        t.tail = new ImmutableLinkedSeq<>(newValue, list.tail);
        return res;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        if (this == NIL) {
            return ImmutableLinkedSeq.from(other);
        }

        final int oks = other.knownSize();
        Iterator<? extends E> it = null;
        if (oks == 0) {
            return this;
        } else if (oks < 0) {
            it = other.iterator();
            if (!it.hasNext()) {
                return this;
            }
        }


        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(this.head);
        ImmutableLinkedSeq<E> t = res;

        ImmutableLinkedSeq<E> list = this.tail;
        while (list != NIL) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < other.size(); i++) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
        } else {
            if (it == null) {
                it = other.iterator();
            }
            while (it.hasNext()) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(it.next());
                t.tail = nl;
                t = nl;
            }
        }
        t.tail = nil();
        return res;
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> concat(@NotNull List<? extends E> other) {
        if (this == NIL) {
            return ImmutableLinkedSeq.from(other);
        }

        final int os = other.size();
        if (os == 0) {
            return this;
        }

        final ImmutableLinkedSeq<E> res = new ImmutableLinkedSeq<>(this.head);
        ImmutableLinkedSeq<E> t = res;

        ImmutableLinkedSeq<E> list = this.tail;
        while (list != NIL) {
            ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < os; i++) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
        } else {
            for (E e : other) {
                ImmutableLinkedSeq<E> nl = new ImmutableLinkedSeq<>(e);
                t.tail = nl;
                t = nl;
            }
        }
        t.tail = nil();
        return res;
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        if (this == NIL) {
            return nil();
        }
        LinkedBuffer<U> buffer = new LinkedBuffer<>();
        ImmutableLinkedSeq<E> list = this;
        while (list != NIL) {
            buffer.appendAll(mapper.apply(list.head));
            list = list.tail;
        }
        return buffer.toImmutableLinkedSeq();
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> sorted() {
        if (this == NIL || tail == NIL) {
            return this;
        }
        Object[] arr = this.toArray();
        Arrays.sort(arr);
        return (ImmutableLinkedSeq<E>) from(arr);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> sorted(@NotNull Comparator<? super E> comparator) {
        if (this == NIL || tail == NIL) {
            return this;
        }
        Object[] arr = this.toArray();
        Arrays.sort(arr, (Comparator<Object>) comparator);
        return (ImmutableLinkedSeq<E>) from(arr);
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return mapImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return mapIndexedImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNullImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return mapIndexedNotNullImpl(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableLinkedSeq<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zipImpl(other);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNotImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<@NotNull E> filterNotNull() {
        return filterNotNullImpl();
    }

    @Override
    public final @NotNull <U> ImmutableLinkedSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableLinkedSeq<U>) filter(clazz::isInstance));
    }

    @Override
    public final @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
        return this;
    }

    @Override
    public final @NotNull ImmutableSizedLinkedSeq<E> toImmutableSizedLinkedList() {
        if (this == NIL) {
            return ImmutableSizedLinkedSeq.empty();
        }
        return new ImmutableSizedLinkedSeq<>(this, size());
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        ImmutableLinkedSeq<E> list = this;
        while (list != NIL) {
            action.accept(list.head);
            list = list.tail;
        }
    }

    @Override
    public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        ImmutableLinkedSeq<E> list = this;
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

        private @NotNull ImmutableLinkedSeq<? extends E> list;

        Itr(@NotNull ImmutableLinkedSeq<? extends E> list) {
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

    private static final class Factory<E> implements CollectionFactory<E, LinkedBuffer<E>, ImmutableLinkedSeq<E>> {
        Factory() {
        }

        @Override
        public final ImmutableLinkedSeq<E> empty() {
            return ImmutableLinkedSeq.empty();
        }

        @Override
        public final ImmutableLinkedSeq<E> from(E @NotNull [] values) {
            return ImmutableLinkedSeq.from(values);
        }

        @Override
        public final ImmutableLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableLinkedSeq.from(values);
        }

        @Override
        public final ImmutableLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableLinkedSeq.from(it);
        }

        @Override
        public final ImmutableLinkedSeq<E> fill(int n, E value) {
            return ImmutableLinkedSeq.fill(n, value);
        }

        @Override
        public final ImmutableLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ImmutableLinkedSeq.fill(n, supplier);
        }

        @Override
        public final ImmutableLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableLinkedSeq.fill(n, init);
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
            return (LinkedBuffer<E>) Builder.merge(builder1, builder2);
        }

        @Override
        public final ImmutableLinkedSeq<E> build(@NotNull LinkedBuffer<E> builder) {
            return builder.toImmutableLinkedSeq();
        }
    }

    /**
     * Internal implementation of {@link LinkedBuffer}.
     *
     * @see LinkedBuffer
     */
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static abstract class Builder<E> extends AbstractBuffer<E> {
        ImmutableLinkedSeq<E> first = null;
        ImmutableLinkedSeq<E> last = null;

        int len = 0;

        private boolean aliased = false;

        static <E> Builder<E> merge(@NotNull Builder<E> b1, @NotNull Builder<E> b2) {
            final int b1s = b1.len;
            if (b1s == 0) {
                return b2;
            } else if (b1s == 1) {
                b2.prepend(b1.first.head);
                return b2;
            }

            final int b2s = b2.len;
            if (b2s == 0) {
                return b1;
            } else if (b2s == 1) {
                b1.append(b2.first.head);
                return b1;
            }

            b1.ensureUnaliased();
            b2.ensureUnaliased();

            final ImmutableLinkedSeq<E> b2f = b2.first;
            final ImmutableLinkedSeq<E> b2l = b2.last;
            b2.clear();

            b1.last.tail = b2f;
            b1.last = b2l;
            b1.len = b1s + b2s;
            return b1;
        }

        private void ensureUnaliased() {
            if (aliased) {
                Builder<E> buffer = new LinkedBuffer<>();
                buffer.appendAll(this);
                this.first = buffer.first;
                this.last = buffer.last;
                aliased = false;
            }
        }

        @Override
        public final E get(int index) {
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            if (index == len - 1) {
                return last.head;
            }

            return first.get(index);
        }

        @Override
        public final void swap(int index1, int index2) {
            final int size = this.len;
            Conditions.checkElementIndex(index1, size);
            Conditions.checkElementIndex(index2, size);
            if (index1 == index2) {
                return;
            }
            ensureUnaliased();

            final int i1 = Integer.min(index1, index2);
            final int i2 = Integer.max(index1, index2);

            ImmutableLinkedSeq<E> list = this.first;
            int i = 0;
            while (i < i1) {
                list = list.tail;
                i++;
            }

            final ImmutableLinkedSeq<E> node1 = list;
            while (i < i2) {
                list = list.tail;
                i++;
            }
            final ImmutableLinkedSeq<E> node2 = list;

            final E tmp = node1.head;
            node1.head = node2.head;
            node2.head = tmp;
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            if (index < 0 || index >= len) {
                return Option.none();
            }
            if (index == len - 1) {
                return Option.some(last.head);
            }

            return Option.some(first.get(index));
        }

        @Override
        public final E first() {
            ImmutableLinkedSeq<E> first = this.first;
            if (first == null) {
                throw new NoSuchElementException();
            } else {
                return first.head;
            }
        }

        @Override
        public final E last() {
            ImmutableLinkedSeq<E> last = this.last;
            if (last == null) {
                throw new NoSuchElementException();
            } else {
                return last.head;
            }
        }

        @Override
        public final boolean isEmpty() {
            return len == 0;
        }

        @Override
        public final void append(E value) {
            ImmutableLinkedSeq<E> i = of(value);
            if (len == 0) {
                first = i;
            } else {
                last.tail = i;
            }
            last = i;
            ++len;
        }

        @Override
        public final void prepend(E value) {
            ensureUnaliased();
            if (len == 0) {
                append(value);
                return;
            }
            first = first.cons(value);
            ++len;
        }

        @Override
        public void insert(int index, E value) {
            ensureUnaliased();
            if (index < 0 || index > len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            if (index == len) {
                append(value);
                return;
            }

            if (index == 0) {
                prepend(value);
                return;
            }
            ensureUnaliased();
            ImmutableLinkedSeq<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail();
            }

            i.tail = i.tail().cons(value);
            ++len;
        }

        @Override
        public final E removeAt(int index) {
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            if (index == 0) {
                E v = first.head;
                if (len == 1) {
                    first = last = null;
                    aliased = false;
                } else {
                    first = first.tail;
                }
                --len;
                return v;
            }

            ensureUnaliased();
            ImmutableLinkedSeq<E> i = first;
            int c = 1;

            while (c++ != index) {
                i = i.tail();
            }
            E v = i.tail().head();
            i.tail = i.tail().tail();
            --len;
            return v;
        }

        @Override
        public final void removeAt(int index, int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count: " + count);
            }
            if (index < 0 || index + count > len) {
                throw new IndexOutOfBoundsException(String.format("%d to %d is out of bounds", index, index + count));
            }

            if (count == 0) {
                return;
            }
            if (count == 1) {
                removeAt(index);
                return;
            }
            if (count == len) {
                clear();
                return;
            }
            if (index == 0) {
                int c = count;
                while (c-- > 0) {
                    first = first.tail;
                }
                len -= count;
                return;
            }

            ensureUnaliased();
            ImmutableLinkedSeq<E> i = first;
            int c = 1;
            while (c++ != index) {
                i = i.tail();
            }

            ImmutableLinkedSeq<E> t = i.tail();
            c = count;
            while (c-- > 0) {
                t = t.tail();
            }

            i.tail = t;
            len -= count;
        }

        public final E removeFirst() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }

            E v = first.head;
            if (len == 1) {
                first = last = null;
                aliased = false;
            } else {
                first = first.tail;
            }
            --len;
            return v;
        }

        public final void clear() {
            first = last = null;
            len = 0;
            aliased = false;
        }

        @Override
        public final @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
            final ImmutableLinkedSeq<E> first = this.first;
            if (first == null) {
                return empty();
            }
            aliased = true;
            return first;
        }

        @Override
        public final @NotNull ImmutableSizedLinkedSeq<E> toImmutableSizedLinkedList() {
            final ImmutableLinkedSeq<E> first = this.first;
            if (first == null) {
                return ImmutableSizedLinkedSeq.empty();
            }
            aliased = true;
            return new ImmutableSizedLinkedSeq<>(first, len);
        }

        @Override
        public final void set(int index, E newValue) {
            final int len = this.len;
            if (index < 0 || index >= len) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }

            ensureUnaliased();
            if (index == len - 1) {
                last.head = newValue;
                return;
            }

            ImmutableLinkedSeq<E> l = first;
            while (--index >= 0) {
                l = l.tail();
            }
            l.head = newValue;
        }

        @Override
        public final void sort(@NotNull Comparator<? super E> comparator) {
            if (len == 0) {
                return;
            }
            Object[] values = toArray();
            Arrays.sort(values, (Comparator<? super Object>) comparator);

            ensureUnaliased();
            ImmutableLinkedSeq<E> c = first;
            for (Object value : values) {
                c.head = (E) value;
                c = c.tail;
            }
        }

        @Override
        public final void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
            ImmutableLinkedSeq<E> n = first;
            if (n == null || n == NIL) {
                return;
            }
            ensureUnaliased();
            while (n != NIL) {
                ImmutableLinkedSeq<E> c = n;
                c.head = operator.apply(c.head);
                n = c.tail;
            }
        }

        @Override
        public final void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
            ImmutableLinkedSeq<E> n = first;
            if (n == null || n == NIL) {
                return;
            }
            ensureUnaliased();
            int i = 0;
            while (n != NIL) {
                ImmutableLinkedSeq<E> c = n;
                c.head = operator.apply(i++, c.head);
                n = c.tail;
            }
        }

        @Override
        public final void reverse() {
            if (len <= 1) {
                return;
            }
            Builder<E> newBuffer = new LinkedBuffer<>();
            for (E e : this) {
                newBuffer.prepend(e);
            }
            this.first = newBuffer.first;
            this.last = newBuffer.last;
            this.aliased = false;
        }

        @Override
        public final int size() {
            return len;
        }

        @Override
        public final int knownSize() {
            return len;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            final ImmutableLinkedSeq<E> first = this.first;
            return first == null ? Iterators.empty() : first.iterator();
        }
    }
}
