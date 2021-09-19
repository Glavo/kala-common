package kala.collection.immutable;

import kala.annotations.Covariant;
import kala.annotations.StaticClass;
import kala.collection.*;
import kala.collection.base.AbstractIterator;
import kala.collection.base.AnyTraversable;
import kala.collection.base.Iterators;
import kala.collection.internal.view.SeqViews;
import kala.collection.mutable.AbstractDynamicSeq;
import kala.control.Option;
import kala.function.*;
import kala.Conditions;
import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.DynamicLinkedSeq;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableLinkedSeq<E> extends AbstractImmutableSeq<E>
        implements ImmutableSeqOps<E, ImmutableLinkedSeq<?>, ImmutableLinkedSeq<E>>, Serializable {
    private static final long serialVersionUID = 4185879054671961536L;

    public static final NodeFactory<?> NODE_FACTORY = new NodeFactory<>();
    private static final Factory<?> FACTORY = new Factory<>();

    public static final Node<?> NIL_NODE = new Node<>();
    public static final ImmutableLinkedSeq<?> EMPTY = new ImmutableLinkedSeq<>(NIL_NODE, 0);

    private final Node<E> list;
    private final int size;

    ImmutableLinkedSeq(Node<E> list, int size) {
        this.list = list;
        this.size = size;
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <E> Node<E> narrow(Node<? extends E> node) {
        return (Node<E>) node;
    }

    //endregion

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, ImmutableLinkedSeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableLinkedSeq<E> empty() {
        return (ImmutableLinkedSeq<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1) {
        return new ImmutableLinkedSeq<>(nodeOf(value1), 1);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2) {
        return new ImmutableLinkedSeq<>(nodeOf(value1, value2), 2);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableLinkedSeq<>(nodeOf(value1, value2, value3), 3);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableLinkedSeq<>(nodeOf(value1, value2, value3, value4), 4);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableLinkedSeq<>(nodeOf(value1, value2, value3, value4, value5), 5);
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableLinkedSeq<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(E @NotNull [] values) {
        final int size = values.length;
        if (size == 0) {
            return empty();
        }
        return new ImmutableLinkedSeq<>(nodeFrom(values), size);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull IndexedSeqLike<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableLinkedSeq<>(nodeFrom(values), size);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull java.util.List<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        return new ImmutableLinkedSeq<>(nodeFrom(values), size);
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableLinkedSeq<?>) {
            return ((ImmutableLinkedSeq<E>) values);
        }

        if (values instanceof Node<?>) {
            return from((Node<E>) values);
        }

        return from(values.iterator()); // TODO
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Node<? extends E> values) {
        final int size = values.size();
        return size == 0 ? empty() : new ImmutableLinkedSeq<>((Node<E>) values, size);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        final Node<E> res = new Node<>(it.next());
        Node<E> t = res;
        int c = 1;
        while (it.hasNext()) {
            Node<E> nl = new Node<>(it.next());
            t.tail = nl;
            t = nl;
            c++;
        }
        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, c);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        Node<E> res = nilNode();
        for (int i = 0; i < n; i++) {
            res = new Node<>(value, res);
        }
        return new ImmutableLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }
        final Node<E> res = new Node<>(supplier.get());
        Node<E> t = res;

        for (int i = 1; i < n; i++) {
            Node<E> nl = new Node<>(supplier.get());
            t.tail = nl;
            t = nl;
        }
        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, n);
    }

    public static <E> @NotNull ImmutableLinkedSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        final Node<E> res = new Node<>(init.apply(0));
        Node<E> t = res;

        for (int i = 1; i < n; i++) {
            Node<E> nl = new Node<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, n);
    }

    @StaticClass
    public static final class Unsafe {
        private Unsafe() {
        }

        public static <E> @NotNull ImmutableLinkedSeq<E> build(@NotNull Node<? extends E> node, int size) {
            Objects.requireNonNull(node);
            assert size >= 0;

            return size == 0 ? ImmutableLinkedSeq.empty() : new ImmutableLinkedSeq<>((Node<E>) node, size);
        }
    }

    //endregion

    //region NodeFactories

    @SuppressWarnings("unchecked")
    public static <E> CollectionFactory<E, ?, Node<E>> nodeFactory() {
        return (NodeFactory<E>) NODE_FACTORY;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull Node<E> nilNode() {
        return (Node<E>) NIL_NODE;
    }

    public static <E> @NotNull Node<E> emptyNode() {
        return nilNode();
    }

    public static <E> @NotNull Node<E> nodeOf() {
        return nilNode();
    }

    public static <E> @NotNull Node<E> nodeOf(E value1) {
        return new Node<>(value1, nilNode());
    }

    public static <E> @NotNull Node<E> nodeOf(E value1, E value2) {
        return new Node<>(value1,
                new Node<>(value2, nilNode()));
    }

    public static <E> @NotNull Node<E> nodeOf(E value1, E value2, E value3) {
        return new Node<>(value1,
                new Node<>(value2,
                        new Node<>(value3, nilNode())));
    }

    public static <E> @NotNull Node<E> nodeOf(E value1, E value2, E value3, E value4) {
        return new Node<>(value1,
                new Node<>(value2,
                        new Node<>(value3,
                                new Node<>(value4, nilNode()))));
    }

    public static <E> @NotNull Node<E> nodeOf(E value1, E value2, E value3, E value4, E value5) {
        return new Node<>(value1,
                new Node<>(value2,
                        new Node<>(value3,
                                new Node<>(value4,
                                        new Node<>(value5, nilNode())))));
    }

    @SafeVarargs
    public static <E> @NotNull Node<E> nodeOf(E... values) {
        return nodeFrom(values);
    }

    public static <E> @NotNull Node<E> nodeFrom(E @NotNull [] values) {
        Node<E> res = nilNode();
        for (int i = values.length - 1; i >= 0; i--) {
            res = new Node<>(values[i], res);
        }
        return res;
    }

    public static <E> @NotNull Node<E> nodeFrom(@NotNull IndexedSeqLike<? extends E> values) {
        Node<E> res = nilNode();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = res.cons(values.get(i));
        }
        return res;
    }

    public static <E> @NotNull Node<E> nodeFrom(@NotNull List<? extends E> values) {
        if (values.isEmpty()) {
            return emptyNode();
        }
        return values instanceof RandomAccess
                ? nodeFromRandomAccess(values)
                : nodeFrom(values.iterator());
    }

    private static <E> @NotNull Node<E> nodeFromRandomAccess(@NotNull List<? extends E> values) {
        Node<E> res = nilNode();
        for (int i = values.size() - 1; i >= 0; i--) { // implicit null check of values
            res = new Node<>(values.get(i), res);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull Node<E> nodeFrom(@NotNull Iterable<? extends E> values) {
        if (values instanceof Node<?>) {
            return ((Node<E>) values);
        }
        return nodeFrom(values.iterator()); // implicit null check of values
    }

    public static <E> @NotNull Node<E> nodeFrom(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return nilNode();
        }
        Node<E> cons = new Node<>(it.next());
        cons.appendIterator(it).tail = nilNode();
        return cons;
    }

    public static <E> @NotNull Node<E> nodeFrom(@NotNull Stream<? extends E> stream) {
        return stream.collect(nodeFactory());
    }

    public static <E> @NotNull Node<E> nodeFill(int n, E value) {
        Node<E> res = nilNode();
        for (int i = 0; i < n; i++) {
            res = new Node<>(value, res);
        }
        return res;
    }

    public static <E> @NotNull Node<E> nodeFill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return nilNode();
        }
        final Node<E> res = new Node<>(supplier.get());
        Node<E> t = res;

        for (int i = 1; i < n; i++) {
            Node<E> nl = new Node<>(supplier.get());
            t.tail = nl;
            t = nl;
        }
        t.tail = nilNode();
        return res;
    }

    public static <E> @NotNull Node<E> nodeFill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return nilNode();
        }
        final Node<E> res = new Node<>(init.apply(0));
        Node<E> t = res;

        for (int i = 1; i < n; i++) {
            Node<E> nl = new Node<>(init.apply(i));
            t.tail = nl;
            t = nl;
        }
        t.tail = nilNode();
        return res;
    }


    //endregion

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "ImmutableLinkedSeq";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableLinkedSeq<U>> iterableFactory() {
        return ImmutableLinkedSeq.factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return list.iterator();
    }

    //endregion

    //region Size Info

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int knownSize() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public E get(int index) {
        Conditions.checkElementIndex(index, size);
        Node<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return list.head;
    }

    @Override
    public @Nullable E getOrNull(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return list.head;
    }

    @Override
    public @NotNull Option<E> getOption(int index) {
        if (index < 0 || index >= size) {
            return Option.none();
        }
        Node<E> list = this.list;
        for (int i = 0; i < index; i++) {
            list = list.tail;
        }
        return Option.some(list.head);
    }

    //endregion

    //region Reversal Operations

    @Override
    public @NotNull ImmutableLinkedSeq<E> reversed() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        } else {
            return new ImmutableLinkedSeq<>(list.reversed(), size);
        }
    }

    @Override
    public @NotNull Iterator<E> reverseIterator() {
        return list.reverseIterator();
    }

    //endregion

    //region Addition Operations

    public @NotNull ImmutableLinkedSeq<E> cons(E value) {
        return new ImmutableLinkedSeq<>(list.cons(value), size + 1);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> prepended(E value) {
        return new ImmutableLinkedSeq<>(list.prepended(value), size + 1);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> prependedAll(E @NotNull [] values) {
        return prependedAllImpl(values);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        return prependedAllImpl(values);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> appended(E value) {
        return new ImmutableLinkedSeq<>(list.appended(value), size + 1);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> appendedAll(E @NotNull [] values) {
        return appendedAllImpl(values);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        return appendedAllImpl(values);
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    public E first() {
        return list.first();
    }

    @Override
    public E first(@NotNull Predicate<? super E> predicate) {
        return list.first(predicate);
    }

    @Override
    public @Nullable E firstOrNull() {
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
    public E last() {
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
    public boolean contains(Object value) {
        return list.contains(value);
    }

    @Override
    public boolean containsAll(Object @NotNull [] values) {
        return list.containsAll(values);
    }

    @Override
    public boolean containsAll(@NotNull Iterable<?> values) {
        return list.containsAll(values);
    }

    @Override
    public boolean sameElements(@NotNull Iterable<?> other) {
        return list.sameElements(other);
    }

    @Override
    public boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
        return list.sameElements(other, identity);
    }

    @Override
    public boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        return list.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(@NotNull Predicate<? super E> predicate) {
        return list.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        return list.noneMatch(predicate);
    }

    //endregion

    //region Search Operations

    @Override
    @Contract(pure = true)
    public int indexOf(Object value) {
        return list.indexOf(value);
    }

    @Override
    @Contract(pure = true)
    public int indexWhere(@NotNull Predicate<? super E> predicate) {
        return list.indexWhere(predicate);
    }

    @Override
    @Contract(pure = true)
    public int lastIndexOf(Object value) {
        return list.lastIndexOf(value);
    }

    @Override
    @Contract(pure = true)
    public int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        return list.lastIndexWhere(predicate);
    }

    //endregion

    //region Misc Operations

    @Override
    public @NotNull ImmutableLinkedSeq<E> slice(int beginIndex, int endIndex) {
        final int size = this.size;
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        if (endIndex == size) {
            if (beginIndex == 0) {
                return this;
            } else {
                Node<E> list = this.list;
                int n = beginIndex;
                while (n-- > 0) {
                    list = list.tail;
                }
                return new ImmutableLinkedSeq<>(list, size - beginIndex);
            }
        }
        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return ImmutableLinkedSeq.empty();
        }
        return new ImmutableLinkedSeq<>(list.drop(beginIndex).take(ns), ns);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableLinkedSeq.empty();
        }

        Node<E> list = this.list;
        for (int i = 0; i < n; i++) {
            list = list.tail;
        }
        return new ImmutableLinkedSeq<>(list, size - n);

    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableLinkedSeq.empty();
        }

        final int ns = size - n;
        return new ImmutableLinkedSeq<>(list.take(ns), ns);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int c = 0;

        Node<E> list = this.list;
        while (list != NIL_NODE && predicate.test(list.head)) {
            list = list.tail();
            c++;
        }

        if (list == NIL_NODE) {
            return ImmutableLinkedSeq.empty();
        }
        if (c == 0) {
            return this;
        }

        return new ImmutableLinkedSeq<>(list, size - c);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return ImmutableLinkedSeq.empty();
        }
        if (n >= size) {
            return this;
        }
        return new ImmutableLinkedSeq<>(list.take(n), n);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return ImmutableLinkedSeq.empty();
        }
        final int size = this.size;
        if (n >= size) {
            return this;
        }
        return new ImmutableLinkedSeq<>(list.drop(size - n), n);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        Node<E> list = this.list;
        if (list == NIL_NODE || !predicate.test(list.head)) {
            return ImmutableLinkedSeq.empty();
        }

        int c = 0;

        final Node<E> res = new Node<>(list.head);
        Node<E> t = res;

        list = list.tail;

        while (true) {
            if (list == NIL_NODE) {
                return this;
            }
            if (!predicate.test(list.head)) {
                break;
            }
            Node<E> nl = new Node<>(list.head);
            t.tail = nl;
            t = nl;
            c++;
            list = list.tail;
        }

        t.tail = nilNode();
        return new ImmutableLinkedSeq<>(res, c);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> updated(int index, E newValue) {
        final int size = this.size;
        Conditions.checkElementIndex(index, size);
        return new ImmutableLinkedSeq<>(list.updated(index, newValue), size);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableLinkedSeq<>(nodeFrom(other), size);
        }

        Node<E> list = this.list;
        final Node<E> res = new Node<>(list.head);
        Node<E> t = res;
        list = list.tail;

        while (list != NIL_NODE) {
            Node<E> nl = new Node<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            for (int i = 0; i < otherSize; i++) {
                Node<E> nl = new Node<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = nilNode();
            return new ImmutableLinkedSeq<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                Node<E> nl = new Node<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = nilNode();
            return new ImmutableLinkedSeq<>(res, c);
        }
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> concat(java.util.@NotNull List<? extends E> other) {
        final int size = this.size;
        final int otherSize = other.size();
        if (otherSize == 0) {
            return this;
        }
        if (size == 0) {
            return new ImmutableLinkedSeq<>(nodeFrom(other), size);
        }

        Node<E> list = this.list;
        final Node<E> res = new Node<>(list.head);
        Node<E> t = res;
        list = list.tail;

        while (list != NIL_NODE) {
            Node<E> nl = new Node<>(list.head);
            t.tail = nl;
            t = nl;
            list = list.tail;
        }

        if (other instanceof RandomAccess) {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < otherSize; i++) {
                Node<E> nl = new Node<>(other.get(i));
                t.tail = nl;
                t = nl;
            }
            t.tail = nilNode();
            return new ImmutableLinkedSeq<>(res, size + otherSize);
        } else {
            int c = this.size;
            for (E e : other) {
                Node<E> nl = new Node<>(e);
                t.tail = nl;
                t = nl;
                c++;
            }
            assert c == size + otherSize;
            t.tail = nilNode();
            return new ImmutableLinkedSeq<>(res, c);
        }
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate);
    }

    @Override
    public @NotNull <Ex extends Throwable> ImmutableLinkedSeq<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filter(predicate);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNotImpl(predicate);
    }

    @Override
    public @NotNull <Ex extends Throwable> ImmutableLinkedSeq<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filterNot(predicate);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> filterNotUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> filterNotNull() {
        return filterNotNullImpl();
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableLinkedSeq<U>) filter(clazz::isInstance));
    }

    @Override
    public <U> @NotNull ImmutableLinkedSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableLinkedSeq.empty();
        }
        return new ImmutableLinkedSeq<>(list.map(mapper), size);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    public @NotNull <U, Ex extends Throwable> ImmutableLinkedSeq<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Override
    public <U> @NotNull ImmutableLinkedSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int size = this.size;
        if (size == 0) {
            return ImmutableLinkedSeq.empty();
        }
        return new ImmutableLinkedSeq<>(list.mapIndexed(mapper), size);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    public @NotNull <U, Ex extends Throwable> ImmutableLinkedSeq<U> mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexed(mapper);
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexed(mapper);
    }


    @Override
    public <U> @NotNull ImmutableLinkedSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNullImpl(mapper);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    public @NotNull <U, Ex extends Throwable> ImmutableLinkedSeq<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Override
    public <U> @NotNull ImmutableLinkedSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return mapIndexedNotNullImpl(mapper);
    }

    @Override
    public @NotNull <U, Ex extends Throwable> ImmutableLinkedSeq<@NotNull U> mapIndexedNotNullChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends @Nullable U, ? extends Ex> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<@NotNull U> mapIndexedNotNullUnchecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        final Builder<U> builder = new DynamicLinkedSeq<>();
        Consumer<U> consumer = builder::append;
        for (E e : this) {
            mapper.accept(e, consumer);
        }
        return builder.toImmutableLinkedSeq();
    }

    @Override
    public @NotNull <U> ImmutableLinkedSeq<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        final Builder<U> builder = new DynamicLinkedSeq<>();
        Consumer<U> consumer = builder::append;
        int idx = 0;
        for (E e : this) {
            mapper.accept(idx++, e, consumer);
        }
        return builder.toImmutableLinkedSeq();
    }

    @Override
    public <U> @NotNull ImmutableLinkedSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMapImpl(mapper);
    }

    @Override
    @SuppressWarnings("RedundantThrows")
    public <U, Ex extends Throwable> @NotNull ImmutableLinkedSeq<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Override
    public <U> @NotNull ImmutableLinkedSeq<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> sorted() {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableLinkedSeq<>(list.sorted(), size);
    }

    @Override
    public @NotNull ImmutableLinkedSeq<E> sorted(@NotNull Comparator<? super E> comparator) {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return this;
        }
        return new ImmutableLinkedSeq<>(list.sorted(comparator), size);
    }

    //endregion

    private Object writeReplace() {
        return this == EMPTY ? EmptyReplaced.INSTANCE : this;
    }

    static final class NilNodeReplaced implements Serializable {
        private static final long serialVersionUID = 0L;

        static final NilNodeReplaced INSTANCE = new NilNodeReplaced();

        private NilNodeReplaced() {
        }

        private Object readResolve() {
            return NIL_NODE;
        }
    }

    static final class EmptyReplaced implements Serializable {
        private static final long serialVersionUID = 0L;

        static final EmptyReplaced INSTANCE = new EmptyReplaced();

        private EmptyReplaced() {
        }

        private Object readResolve() {
            return EMPTY;
        }
    }

    static final class Factory<E> implements CollectionFactory<E, DynamicLinkedSeq<E>, ImmutableLinkedSeq<E>> {

        @Override
        public DynamicLinkedSeq<E> newBuilder() {
            return new DynamicLinkedSeq<>();
        }

        @Override
        public ImmutableLinkedSeq<E> build(DynamicLinkedSeq<E> builder) {
            return builder.toImmutableLinkedSeq();
        }

        @Override
        public void addToBuilder(@NotNull DynamicLinkedSeq<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public DynamicLinkedSeq<E> mergeBuilder(@NotNull DynamicLinkedSeq<E> builder1, @NotNull DynamicLinkedSeq<E> builder2) {
            return (DynamicLinkedSeq<E>) Builder.merge(builder1, builder2);
        }
    }

    public static final class NodeFactory<E> implements CollectionFactory<E, DynamicLinkedSeq<E>, Node<E>> {
        NodeFactory() {
        }

        @Override
        public Node<E> empty() {
            return emptyNode();
        }

        @Override
        public Node<E> from(E @NotNull [] values) {
            return nodeFrom(values);
        }

        @Override
        public Node<E> from(@NotNull Iterable<? extends E> values) {
            return nodeFrom(values);
        }

        @Override
        public Node<E> from(@NotNull Iterator<? extends E> it) {
            return nodeFrom(it);
        }

        @Override
        public Node<E> fill(int n, E value) {
            return nodeFill(n, value);
        }

        @Override
        public Node<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return nodeFill(n, supplier);
        }

        @Override
        public Node<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return nodeFill(n, init);
        }

        @Override
        public DynamicLinkedSeq<E> newBuilder() {
            return new DynamicLinkedSeq<>();
        }

        @Override
        public void addToBuilder(@NotNull DynamicLinkedSeq<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public DynamicLinkedSeq<E> mergeBuilder(@NotNull DynamicLinkedSeq<E> builder1, @NotNull DynamicLinkedSeq<E> builder2) {
            return (DynamicLinkedSeq<E>) Builder.merge(builder1, builder2);
        }

        @Override
        public Node<E> build(@NotNull DynamicLinkedSeq<E> builder) {
            return builder.buildNode();
        }
    }

    /**
     * Internal implementation of {@link DynamicLinkedSeq}.
     *
     * @see DynamicLinkedSeq
     */
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static abstract class Builder<E> extends AbstractDynamicSeq<E> {
        Node<E> first = null;
        Node<E> last = null;

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

            final Node<E> b2f = b2.first;
            final Node<E> b2l = b2.last;
            b2.clear();

            b1.last.tail = b2f;
            b1.last = b2l;
            b1.len = b1s + b2s;
            return b1;
        }

        private void ensureUnaliased() {
            if (aliased) {
                Builder<E> buffer = new DynamicLinkedSeq<>();
                buffer.appendAll(this);
                this.first = buffer.first;
                this.last = buffer.last;
                aliased = false;
            }
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            final Node<E> first = this.first;
            return first == null ? Iterators.empty() : first.iterator();
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

            Node<E> list = this.first;
            int i = 0;
            while (i < i1) {
                list = list.tail;
                i++;
            }

            final Node<E> node1 = list;
            while (i < i2) {
                list = list.tail;
                i++;
            }
            final Node<E> node2 = list;

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
            Node<E> first = this.first;
            if (first == null) {
                throw new NoSuchElementException();
            } else {
                return first.head;
            }
        }

        @Override
        public final E last() {
            Node<E> last = this.last;
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
            Node<E> i = nodeOf(value);
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
            Node<E> i = first;
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
            Node<E> i = first;
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
            Conditions.checkElementIndex(index, len);
            if ((len - index) < count) {
                throw new NoSuchElementException();
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
            Node<E> i = first;
            int c = 1;
            while (c++ != index) {
                i = i.tail();
            }

            Node<E> t = i.tail();
            c = count;
            while (c-- > 0) {
                t = t.tail();
            }

            i.tail = t;
            len -= count;
        }

        public final E removeFirst() {
            if (isEmpty()) {
                throw new NoSuchElementException("Seq is empty");
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
            final Node<E> first = this.first;
            if (first == null || first == NIL_NODE) {
                return ImmutableLinkedSeq.empty();
            }
            aliased = true;
            return new ImmutableLinkedSeq<>(first, len);
        }

        public final @NotNull Node<E> buildNode() {
            final Node<E> first = this.first;
            if (first == null || first == NIL_NODE) {
                return nilNode();
            }
            aliased = true;
            return first;
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

            Node<E> l = first;
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
            Node<E> c = first;
            for (Object value : values) {
                c.head = (E) value;
                c = c.tail;
            }
        }

        @Override
        public final void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
            Node<E> n = first;
            if (n == null || n == NIL_NODE) {
                return;
            }
            ensureUnaliased();
            while (n != NIL_NODE) {
                Node<E> c = n;
                c.head = operator.apply(c.head);
                n = c.tail;
            }
        }

        @Override
        public final void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
            Node<E> n = first;
            if (n == null || n == NIL_NODE) {
                return;
            }
            ensureUnaliased();
            int i = 0;
            while (n != NIL_NODE) {
                Node<E> c = n;
                c.head = operator.apply(i++, c.head);
                n = c.tail;
            }
        }

        @Override
        public final void retainAll(@NotNull Predicate<? super E> predicate) {
            ensureUnaliased();
            Node<E> prev = null;
            Node<E> cur = first;
            if (cur == null) {
                return;
            }

            while (cur != NIL_NODE) {
                Node<E> follow = cur.tail;
                if (!predicate.test(cur.head)) {
                    if (prev == null) {
                        first = follow;
                    } else {
                        prev.tail = follow;
                    }
                    --len;
                } else {
                    prev = cur;
                }
                cur = follow;
            }
            last = prev;
        }

        @Override
        public final void reverse() {
            if (len <= 1) {
                return;
            }
            Builder<E> newBuilder = new DynamicLinkedSeq<>();
            for (E e : this) {
                newBuilder.prepend(e);
            }
            this.first = newBuilder.first;
            this.last = newBuilder.last;
            this.aliased = false;
        }

    }

    static final class NodeItr<@Covariant E> extends AbstractIterator<E> {

        private @NotNull Node<? extends E> list;

        NodeItr(@NotNull Node<? extends E> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return list != NIL_NODE;
        }

        @Override
        public E next() {
            if (list == NIL_NODE) {
                throw new NoSuchElementException("ImmutableListIterator.next()");
            }

            E v = list.head();
            list = list.tail();
            return v;
        }
    }

    @SuppressWarnings("unchecked")
    @Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
    public static final class Node<@Covariant E> extends AbstractImmutableSeq<E>
            implements ImmutableSeq<E>, ImmutableSeqOps<E, Node<?>, Node<E>>, Serializable {
        private static final long serialVersionUID = 944030391350569673L;

        E head;

        Node<E> tail;

        Node() {
        }

        Node(E head) {
            this.head = head;
        }

        Node(E head, @NotNull Node<E> tail) {
            this.head = head;
            this.tail = tail;
        }

        Node<E> appendIterator(@NotNull Iterator<? extends E> it) {
            Node<E> node = this;
            while (it.hasNext()) {
                Node<E> nn = new Node<>(it.next());
                node.tail = nn;
                node = nn;
            }
            return node;
        }

        //region Collection Operations

        @Override
        public @NotNull String className() {
            return "ImmutableLinkedSeq.Node";
        }

        @Override
        public <U> @NotNull CollectionFactory<U, ?, Node<U>> iterableFactory() {
            return nodeFactory();
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            if (this == NIL_NODE) {
                return Iterators.empty();
            }
            if (tail == NIL_NODE) {
                return Iterators.of(head);
            }
            return new NodeItr<>(this);
        }

        @Override
        public @NotNull Iterator<E> iterator(int beginIndex) {
            if (beginIndex < 0) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
            }
            if (beginIndex == 0) {
                return iterator();
            }

            int n = beginIndex;
            Node<E> list = this;
            while (n-- > 0) {
                if (list == NIL_NODE) {
                    throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
                }
                list = list.tail;
            }
            return list.iterator();
        }

        @Override
        public @NotNull SeqView<E> view() {
            if (this == NIL_NODE) {
                return SeqView.empty();
            }
            if (tail == NIL_NODE) {
                return new SeqViews.Single<>(head);
            }
            return new SeqViews.WithCachedSize<>(this);
        }

        //endregion

        //region Size Info

        @Override
        public boolean isEmpty() {
            return this == NIL_NODE;
        }

        @Override
        public int size() {
            Node<? extends E> list = this;
            int c = 0;
            while (list != NIL_NODE) {
                ++c;
                list = list.tail();
            }
            return c;
        }

        @Override
        public int knownSize() {
            if (this == NIL_NODE) {
                return 0;
            }
            if (tail == NIL_NODE) {
                return 1;
            }
            return -1;
        }


        //endregion

        //region Size Compare Operations

        @Override
        public int sizeCompare(int otherSize) {
            if (otherSize < 0) {
                return 1;
            }
            int i = 0;
            Node<E> list = this;
            while (list != NIL_NODE) {
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
        public @NotNull Node<E> reversed() {
            if (this == NIL_NODE || this.tail == NIL_NODE) {
                return this;
            }

            Node<? extends E> list = this;
            Node<E> res = nilNode();
            while (list != NIL_NODE) {
                res = new Node<>(list.head, res);
                list = list.tail;
            }
            return res;
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return reversed().iterator();
        }

        //endregion

        //region Element Retrieval Operations

        public E head() {
            if (this == NIL_NODE) {
                throw new NoSuchElementException("ImmutableList.Nil.head()");
            } else {
                return head;
            }
        }

        public @Nullable E headOrNull() {
            return head;
        }

        public @NotNull Option<E> headOption() {
            return this == NIL_NODE ? Option.none() : Option.some(head);
        }

        public @NotNull Node<E> tail() {
            if (this == NIL_NODE) {
                throw new NoSuchElementException("ImmutableList.Nil.tail()");
            } else {
                return tail;
            }
        }

        public @Nullable Node<E> tailOrNull() {
            return tail;
        }

        public @NotNull Option<@NotNull Node<E>> tailOption() {
            return Option.of(tail);
        }

        @Override
        public E first() {
            return head();
        }

        @Override
        public E last() {
            Node<E> node = this;
            while (node.tail() != NIL_NODE) {
                node = node.tail();
            }
            return node.head();
        }

        //endregion

        @Contract("_ -> new")
        public @NotNull Node<E> cons(E element) {
            return new Node<>(element, this);
        }

        @Override
        public @NotNull Node<E> prepended(E value) {
            return cons(value);
        }

        @Override
        public @NotNull Node<E> prependedAll(E @NotNull [] values) {
            int prefixLength = values.length; // implicit null check of prefix
            Node<E> result = this;
            for (int i = prefixLength - 1; i >= 0; i--) {
                result = result.cons(values[i]);
            }
            return result;
        }

        @Override
        public @NotNull Node<E> prependedAll(@NotNull Iterable<? extends E> values) {
            if (values instanceof RandomAccess) {
                if (values instanceof Seq<?>) {
                    Seq<E> seq = (Seq<E>) values;
                    Node<E> res = this;
                    for (int i = seq.size() - 1; i >= 0; i--) {
                        res = res.cons(seq.get(i));
                    }
                    return res;
                }
                if (values instanceof List<?>) {
                    final List<E> list = (List<E>) values;
                    Node<E> res = this;
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
            final Node<E> res = new Node<>(it.next());
            res.appendIterator(it).tail = this;
            return res;
        }

        @Override
        public @NotNull Node<E> appended(E value) {
            if (this == NIL_NODE) {
                return nodeOf(value);
            }
            if (tail == NIL_NODE) {
                return nodeOf(this.head, value);
            }
            return appendedImpl(value);
        }

        @Override
        public @NotNull Node<E> appendedAll(E @NotNull [] values) {
            if (values.length == 0) { // implicit null check of postfix
                return this;
            }
            if (this == NIL_NODE) {
                return nodeFrom(values);
            }
            return appendedAllImpl(values);
        }

        @Override
        public @NotNull Node<E> appendedAll(@NotNull Iterable<? extends E> values) {
            if (AnyTraversable.knownSize(values) == 0) {
                return this;
            }
            if (this == NIL_NODE) {
                return nodeFrom(values);
            }
            if (values instanceof Node) {
                return ((Node<E>) values).prependedAll(this);
            }
            return appendedAllImpl(values);
        }

        @Override
        public @NotNull Node<E> slice(int beginIndex, int endIndex) {
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
                return nilNode();
            }
            if (beginIndex == 0) {
                if (this == NIL_NODE) {
                    throw new IndexOutOfBoundsException("endIndex(" + endIndex + ") > size(0)");
                }
                return take(endIndex);
            }

            final int ns = endIndex - beginIndex;

            int i = 0;

            Node<E> list = this;
            while (list != NIL_NODE && i < beginIndex) {
                list = list.tail;
                ++i;
            }
            if (i != beginIndex) {
                throw new IndexOutOfBoundsException("beginIndex = " + beginIndex);
            }
            if (ns == 1) {
                return nodeOf(list.head());
            }

            i = 0;
            DynamicLinkedSeq<E> buffer = new DynamicLinkedSeq<>();
            while (list != NIL_NODE && i < ns) {
                buffer.append(list.head);
                list = list.tail;
                ++i;
            }
            if (i != ns) {
                throw new IndexOutOfBoundsException("endIndex = " + endIndex);
            }
            return buffer.buildNode();
        }

        @Override
        public @NotNull Node<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0 || this == NIL_NODE) {
                return this;
            }
            Node<E> list = this;
            while (list != NIL_NODE && n-- > 0) {
                list = list.tail;
            }
            return list;
        }

        @Override
        public @NotNull Node<E> dropLast(int n) {
            return dropLastImpl(n);
        }

        @Override
        public @NotNull Node<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            Node<E> list = this;
            while (list != NIL_NODE && predicate.test(list.head)) {
                list = list.tail();
            }
            return list;
        }

        @Override
        public @NotNull Node<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0 || this == NIL_NODE) {
                return nilNode();
            }
            return takeImpl(n);
        }

        @Override
        public @NotNull Node<E> takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return nilNode();
            }

            final int size = this.size();
            if (n >= size) {
                return this;
            }

            return drop(size - n);
        }

        @Override
        public @NotNull Node<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            Node<E> list = this;
            if (list == NIL_NODE || !predicate.test(list.head)) {
                return nilNode();
            }

            final Node<E> res = new Node<>(list.head);
            Node<E> t = res;

            list = list.tail;

            while (true) {
                if (list == NIL_NODE) {
                    return this;
                }
                if (!predicate.test(list.head)) {
                    break;
                }
                Node<E> nl = new Node<>(list.head);
                t.tail = nl;
                t = nl;

                list = list.tail;
            }

            t.tail = nilNode();
            return res;
        }

        @Override
        public @NotNull Node<E> updated(int index, E newValue) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
            Node<E> list = this;
            if (this == NIL_NODE) {
                throw new IndexOutOfBoundsException("index(" + index + ") >= size(" + 0 + ")");
            }
            if (index == 0) {
                return new Node<>(newValue, list.tail);
            }

            final Node<E> res = new Node<>(list.head);
            Node<E> t = res;
            list = list.tail;

            for (int i = 1; i < index; i++) {
                if (list == NIL_NODE) {
                    throw new IndexOutOfBoundsException("Index: " + index);
                }
                Node<E> nl = new Node<>(list.head);
                t.tail = nl;
                t = nl;

                list = list.tail;
            }
            if (list == NIL_NODE) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }

            t.tail = new Node<>(newValue, list.tail);
            return res;
        }

        @Override
        public @NotNull Node<E> concat(@NotNull SeqLike<? extends E> other) {
            if (this == NIL_NODE) {
                return nodeFrom(other);
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


            final Node<E> res = new Node<>(this.head);
            Node<E> t = res;

            Node<E> list = this.tail;
            while (list != NIL_NODE) {
                Node<E> nl = new Node<>(list.head);
                t.tail = nl;
                t = nl;
                list = list.tail;
            }

            if (other instanceof RandomAccess) {
                for (int i = 0; i < other.size(); i++) {
                    Node<E> nl = new Node<>(other.get(i));
                    t.tail = nl;
                    t = nl;
                }
            } else {
                if (it == null) {
                    it = other.iterator();
                }
                while (it.hasNext()) {
                    Node<E> nl = new Node<>(it.next());
                    t.tail = nl;
                    t = nl;
                }
            }
            t.tail = nilNode();
            return res;
        }

        @Override
        public @NotNull Node<E> concat(@NotNull List<? extends E> other) {
            if (this == NIL_NODE) {
                return nodeFrom(other);
            }

            final int os = other.size();
            if (os == 0) {
                return this;
            }

            final Node<E> res = new Node<>(this.head);
            Node<E> t = res;

            Node<E> list = this.tail;
            while (list != NIL_NODE) {
                Node<E> nl = new Node<>(list.head);
                t.tail = nl;
                t = nl;
                list = list.tail;
            }

            if (other instanceof RandomAccess) {
                //noinspection ForLoopReplaceableByForEach
                for (int i = 0; i < os; i++) {
                    Node<E> nl = new Node<>(other.get(i));
                    t.tail = nl;
                    t = nl;
                }
            } else {
                for (E e : other) {
                    Node<E> nl = new Node<>(e);
                    t.tail = nl;
                    t = nl;
                }
            }
            t.tail = nilNode();
            return res;
        }

        @Override
        public @NotNull Node<E> filter(@NotNull Predicate<? super E> predicate) {
            return filterImpl(predicate);
        }

        @Override
        public @NotNull <Ex extends Throwable> Node<E> filterChecked(
                @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
            return filter(predicate);
        }

        @Override
        public @NotNull Node<E> filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
            return filter(predicate);
        }

        @Override
        public @NotNull Node<E> filterNot(@NotNull Predicate<? super E> predicate) {
            return filterNotImpl(predicate);
        }

        @Override
        public @NotNull <Ex extends Throwable> Node<E> filterNotChecked(
                @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
            return filterNot(predicate);
        }

        @Override
        public @NotNull Node<E> filterNotUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
            return filterNot(predicate);
        }

        @Override
        public @NotNull Node<@NotNull E> filterNotNull() {
            return filterNotNullImpl();
        }

        @Override
        public @NotNull <U> Node<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
            return ((Node<U>) filter(clazz::isInstance));
        }

        @Override
        public <U> @NotNull Node<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return mapImpl(mapper);
        }

        @Override
        @SuppressWarnings("RedundantThrows")
        public @NotNull <U, Ex extends Throwable> Node<U> mapChecked(
                @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
            return map(mapper);
        }

        @Override
        public @NotNull <U> Node<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
            return map(mapper);
        }

        @Override
        public <U> @NotNull Node<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return mapIndexedImpl(mapper);
        }

        @Override
        @SuppressWarnings("RedundantThrows")
        public @NotNull <U, Ex extends Throwable> Node<U> mapIndexedChecked(
                @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
            return mapIndexed(mapper);
        }

        @Override
        public @NotNull <U> Node<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
            return mapIndexed(mapper);
        }

        @Override
        public <U> @NotNull Node<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
            return mapNotNullImpl(mapper);
        }

        @Override
        public @NotNull <U, Ex extends Throwable> Node<U> mapNotNullChecked(
                @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
            return mapNotNull(mapper);
        }

        @Override
        public @NotNull <U> Node<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
            return mapNotNull(mapper);
        }

        @Override
        public <U> @NotNull Node<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
            return mapIndexedNotNullImpl(mapper);
        }

        @Override
        public @NotNull <U, Ex extends Throwable> Node<@NotNull U> mapIndexedNotNullChecked(
                @NotNull CheckedIndexedFunction<? super E, ? extends @Nullable U, ? extends Ex> mapper) {
            return mapIndexedNotNull(mapper);
        }

        @Override
        public @NotNull <U> Node<@NotNull U> mapIndexedNotNullUnchecked(
                @NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
            return mapIndexedNotNull(mapper);
        }

        @Override
        public @NotNull <U> Node<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
            final Builder<U> builder = new DynamicLinkedSeq<>();
            Consumer<U> consumer = builder::append;
            for (E e : this) {
                mapper.accept(e, consumer);
            }
            return builder.buildNode();
        }

        @Override
        public @NotNull <U> Node<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
            final Builder<U> builder = new DynamicLinkedSeq<>();
            Consumer<U> consumer = builder::append;
            int idx = 0;
            for (E e : this) {
                mapper.accept(idx++, e, consumer);
            }
            return builder.buildNode();
        }

        @Override
        public <U> @NotNull Node<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
            if (this == NIL_NODE) {
                return nilNode();
            }
            DynamicLinkedSeq<U> buffer = new DynamicLinkedSeq<>();
            Node<E> list = this;
            while (list != NIL_NODE) {
                buffer.appendAll(mapper.apply(list.head));
                list = list.tail;
            }
            return buffer.buildNode();
        }

        @Override
        public <U, Ex extends Throwable> @NotNull Node<U> flatMapChecked(
                @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
            return flatMap(mapper);
        }

        @Override
        public <U> @NotNull Node<U> flatMapUnchecked(
                @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
            return flatMap(mapper);
        }

        @Override
        public @NotNull Node<E> sorted() {
            if (this == NIL_NODE || tail == NIL_NODE) {
                return this;
            }
            Object[] arr = this.toArray();
            Arrays.sort(arr);
            return (Node<E>) nodeFrom(arr);
        }

        @Override
        public @NotNull Node<E> sorted(@NotNull Comparator<? super E> comparator) {
            if (this == NIL_NODE || tail == NIL_NODE) {
                return this;
            }
            Object[] arr = this.toArray();
            Arrays.sort(arr, (Comparator<Object>) comparator);
            return (Node<E>) nodeFrom(arr);
        }

        @Override
        public <U> @NotNull Node<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
            return zipImpl(other);
        }

        @Override
        public @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
            return ImmutableLinkedSeq.from(this);
        }

        @Override
        public void forEach(@NotNull Consumer<? super E> action) {
            Node<E> list = this;
            while (list != NIL_NODE) {
                action.accept(list.head);
                list = list.tail;
            }
        }

        @Override
        public void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            Node<E> list = this;
            int i = 0;
            while (list != NIL_NODE) {
                action.accept(i++, list.head);
                list = list.tail;
            }
        }

        private Object writeReplace() {
            return this == NIL_NODE ? NilNodeReplaced.INSTANCE : this;
        }

    }
}
