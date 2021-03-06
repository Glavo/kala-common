package kala.collection.immutable;

import kala.collection.base.Iterators;
import kala.function.IndexedFunction;
import kala.annotations.Covariant;
import kala.Conditions;
import kala.collection.SeqLike;
import kala.collection.factory.CollectionFactory;
import kala.collection.Seq;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "toArray()")
public abstract class AbstractImmutableSeq<@Covariant E> extends AbstractImmutableCollection<E> implements ImmutableSeq<E> {

    static <E, T, Builder> T updated(
            @NotNull ImmutableSeq<? extends E> seq,
            int index,
            E newValue,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int s = seq.size();

        if (index < 0 || index >= s) {
            throw new IndexOutOfBoundsException();
        }

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, s);

        for (E e : seq) {
            if (index-- == 0) {
                factory.addToBuilder(builder, newValue);
            } else {
                factory.addToBuilder(builder, e);
            }
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T slice(
            @NotNull ImmutableSeq<? extends E> seq,
            int beginIndex,
            int endIndex,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        final int size = seq.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int ns = endIndex - beginIndex;
        if (ns == 0) {
            return factory.empty();
        }
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, ns);

        if (seq instanceof RandomAccess) {
            for (int i = beginIndex; i < endIndex; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = Iterators.take(Iterators.drop(seq.iterator(), beginIndex), ns);
            while (it.hasNext()) {
                factory.addToBuilder(builder, it.next());
            }
        }
        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T drop(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        int s = seq.knownSize();
        if (s != -1) {
            factory.sizeHint(builder, Integer.max(s - n, 0));
        }

        for (Iterator<? extends E> it = Iterators.drop(seq.iterator(), n); it.hasNext(); ) {
            E e = it.next();
            factory.addToBuilder(builder, e);
        }
        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T dropLast(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n <= 0) {
            return (T) seq;
        }

        final int ss = seq.size();

        if (n >= ss) {
            return factory.empty();
        }
        final int ns = ss - n;
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, ns);

        if (seq instanceof RandomAccess) {
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = seq.iterator();
            for (int i = 0; i < ns; i++) {
                factory.addToBuilder(builder, it.next());
            }
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T dropWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        factory.addAllToBuilder(builder, Iterators.dropWhile(seq.iterator(), predicate));

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T take(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n <= 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();

        int s = seq.knownSize();
        if (s != -1) {
            factory.sizeHint(builder, Integer.min(s, n));
        }

        int count = 0;
        for (E e : seq) {
            if (++count > n) {
                break;
            }
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T takeLast(
            @NotNull ImmutableSeq<? extends E> seq,
            int n,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        if (n <= 0) {
            return factory.empty();
        }
        final int ss = seq.size();
        if (n >= ss) {
            return (T) seq;
        }

        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, n);

        if (seq instanceof RandomAccess) {
            for (int i = ss - n; i < ss; i++) {
                factory.addToBuilder(builder, seq.get(i));
            }
        } else {
            Iterator<? extends E> it = seq.iterator();
            for (int i = 0; i < ss - n; i++) {
                it.next();
            }

            while (it.hasNext()) {
                factory.addToBuilder(builder, it.next());
            }
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T takeWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(predicate);

        Builder builder = factory.newBuilder();

        for (E e : seq) {
            if (!predicate.test(e)) {
                break;
            }
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T concat(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull SeqLike<? extends E> other,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, other);
        factory.addAllToBuilder(builder, other);

        return factory.build(builder);
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T concat(
            @NotNull ImmutableSeq<? extends E> seq,
            java.util.@NotNull List<? extends E> other,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(other);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, other);
        factory.addAllToBuilder(builder, other);

        return factory.build(builder);
    }

    static <E, T, Builder> T prepended(
            @NotNull ImmutableSeq<? extends E> seq,
            E element,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addToBuilder(builder, element);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values.length);
        for (E e : values) {
            factory.addToBuilder(builder, e);
        }

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T appended(
            @NotNull ImmutableSeq<? extends E> seq,
            E element,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addAllToBuilder(builder, seq);
        factory.addToBuilder(builder, element);


        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, values.length);
        factory.addAllToBuilder(builder, seq);

        for (E e : values) {
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(values);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        return factory.build(builder);
    }

    static <E, T, Builder> T sorted(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Comparator<? super E> comparator,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Object[] arr = seq.toArray();
        Arrays.sort(arr, ((Comparator<? super Object>) comparator));

        return factory.from((E[]) arr);
    }

    static <E, T, Builder> T reversed(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq.reverseIterator());
        return factory.build(builder);
    }

    static <E, U, T, Builder> T mapIndexed(
            @NotNull ImmutableSeq<? extends E> Seq,
            @NotNull IndexedFunction<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, Seq);

        int idx = 0;
        for (E e : Seq) {
            factory.addToBuilder(builder, mapper.apply(idx++, e));
        }
        return factory.build(builder);
    }


    static <E, U, T, Builder> T mapIndexedNotNull(
            @NotNull ImmutableSeq<? extends E> Seq,
            @NotNull IndexedFunction<? super E, ? extends U> mapper,
            @NotNull CollectionFactory<? super U, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, Seq);

        int idx = 0;
        for (E e : Seq) {
            U u = mapper.apply(idx++, e);
            if (u != null) {
                factory.addToBuilder(builder, u
                );
            }
        }
        return factory.build(builder);
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To updatedImpl(int index, E newValue) {
        return (To) AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To sliceImpl(int from, int to) {
        return (To) AbstractImmutableSeq.slice(this, from, to, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To dropImpl(int n) {
        return (To) AbstractImmutableSeq.drop(this, n, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To dropLastImpl(int n) {
        return (To) AbstractImmutableSeq.dropLast(this, n, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To dropWhileImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableSeq.dropWhile(this, predicate, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To takeImpl(int n) {
        return (To) AbstractImmutableSeq.take(this, n, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To takeLastImpl(int n) {
        return (To) AbstractImmutableSeq.takeLast(this, n, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To takeWhileImpl(@NotNull Predicate<? super E> predicate) {
        return (To) AbstractImmutableSeq.takeWhile(this, predicate, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To concatImpl(@NotNull SeqLike<? extends E> other) {
        return (To) AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To concatImpl(java.util.@NotNull List<? extends E> other) {
        return (To) concat(this, other, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedImpl(E value) {
        return (To) AbstractImmutableSeq.appended(this, value, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedAllImpl(@NotNull Iterable<? extends E> values) {
        return (To) appendedAll(this, values, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedAllImpl(E @NotNull [] values) {
        return (To) appendedAll(this, values, iterableFactory());
    }


    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedImpl(E value) {
        return (To) AbstractImmutableSeq.prepended(this, value, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedAllImpl(@NotNull Iterable<? extends E> values) {
        return (To) prependedAll(this, values, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedAllImpl(E @NotNull [] values) {
        return (To) prependedAll(this, values, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To sortedImpl() {
        return (To) this.sorted((Comparator<? super E>) Comparator.naturalOrder());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To sortedImpl(@NotNull Comparator<? super E> comparator) {
        return (To) AbstractImmutableSeq.sorted(this, comparator, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To reversedImpl() {
        return (To) AbstractImmutableSeq.reversed(this, iterableFactory());
    }

    @NotNull
    protected final <U, To extends ImmutableSeq<U>> To mapIndexedImpl(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return (To) AbstractImmutableSeq.mapIndexed(this, mapper, iterableFactory());
    }

    @NotNull
    protected final <U, To extends ImmutableSeq<U>> To mapIndexedNotNullImpl(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return (To) AbstractImmutableSeq.mapIndexedNotNull(this, mapper, iterableFactory());
    }

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Seq<?> && Seq.equals(this, (Seq<?>) obj);
    }
}
