package asia.kala.collection.immutable;

import asia.kala.annotations.Covariant;
import asia.kala.collection.*;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedFunction;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutableSeq<@Covariant E> extends AbstractImmutableCollection<E> implements ImmutableSeq<E> {

    static <E, T, Builder> T updated(
            @NotNull ImmutableSeq<? extends E> seq,
            int index,
            E newValue,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;

        int s = seq.size();

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
        return factory.from(seq.view().dropLast(n)); // TODO
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T dropWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;

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
        return factory.from(seq.view().takeLast(n)); // TODO
    }

    static <E, T extends ImmutableSeq<? extends E>, Builder> T takeWhile(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Predicate<? super E> predicate,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;

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
            @NotNull Seq<? extends E> other,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;

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
        assert seq != null;
        assert factory != null;

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addToBuilder(builder, element);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> prefix,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;
        Objects.requireNonNull(prefix);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, prefix);
        factory.addAllToBuilder(builder, prefix);

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T prependedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] prefix,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;
        Objects.requireNonNull(prefix);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, prefix.length);
        for (E e : prefix) {
            factory.addToBuilder(builder, e);
        }

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        return factory.build(builder);
    }

    static <E, T, Builder> T appended(
            @NotNull ImmutableSeq<? extends E> seq,
            E element,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq, 1);

        factory.addAllToBuilder(builder, seq);
        factory.addToBuilder(builder, element);


        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Iterable<? extends E> postfix,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;
        Objects.requireNonNull(postfix);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, postfix);
        factory.addAllToBuilder(builder, postfix);

        return factory.build(builder);
    }

    static <E, T, Builder> T appendedAll(
            @NotNull ImmutableSeq<? extends E> seq,
            E @NotNull [] postfix,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        assert seq != null;
        assert factory != null;
        Objects.requireNonNull(postfix);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, seq);
        factory.addAllToBuilder(builder, seq);

        factory.sizeHint(builder, postfix.length);
        for (E e : postfix) {
            factory.addToBuilder(builder, e);
        }

        return factory.build(builder);
    }

    static <E, T, Builder> T sorted(
            @NotNull ImmutableSeq<? extends E> seq,
            @NotNull Comparator<? super E> comparator,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Objects.requireNonNull(comparator);

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
        assert Seq != null;
        assert factory != null;
        Objects.requireNonNull(mapper);

        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, Seq);

        int idx = 0;
        for (E e : Seq) {
            factory.addToBuilder(builder, mapper.apply(idx++, e));
        }
        return factory.build(builder);
    }


    @NotNull
    protected final <To extends ImmutableSeq<E>> To updatedImpl(int index, E newValue) {
        return (To) AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
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
    protected final <To extends ImmutableSeq<E>> To concatImpl(@NotNull Seq<? extends E> other) {
        return (To) AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedImpl(E element) {
        return (To) AbstractImmutableSeq.prepended(this, element, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedAllImpl(@NotNull Iterable<? extends E> prefix) {
        return (To) AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To prependedAllImpl(E @NotNull [] prefix) {
        return (To) AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedImpl(E element) {
        return (To) AbstractImmutableSeq.prepended(this, element, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedAllImpl(@NotNull Iterable<? extends E> postfix) {
        return (To) AbstractImmutableSeq.appendedAll(this, postfix, iterableFactory());
    }

    @NotNull
    protected final <To extends ImmutableSeq<E>> To appendedAllImpl(E @NotNull [] postfix) {
        return (To) AbstractImmutableSeq.appendedAll(this, postfix, iterableFactory());
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

    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + Collection.SEQ_HASH_MAGIC;
    }
}
