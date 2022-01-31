package kala.collection.mutable;

import kala.Conditions;
import kala.collection.Seq;
import kala.collection.base.ObjectArrays;
import kala.collection.internal.SeqIterators;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.function.IndexedFunction;
import kala.comparator.Comparators;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public interface MutableSeq<E> extends MutableCollection<E>, Seq<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableSeq<E>> factory() {
        return CollectionFactory.narrow(MutableArray.factory());
    }

    static <E> @NotNull MutableSeq<E> of() {
        return MutableArray.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1) {
        return MutableArray.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2) {
        return MutableArray.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3) {
        return MutableArray.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return MutableArray.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArray.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableSeq<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableSeq<E> from(E @NotNull [] values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterator<? extends E> it) {
        return MutableArray.from(it);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> wrapJava(java.util.@NotNull List<E> list) {
        Objects.requireNonNull(list);
        return list instanceof RandomAccess
                ? new FromJavaConvert.MutableIndexedSeqFromJava<>(list)
                : new FromJavaConvert.MutableSeqFromJava<>(list);
    }

    static <E, C extends MutableSeq<E>> @NotNull MutableSeqEditor<E, C> edit(@NotNull C seq) {
        return new MutableSeqEditor<>(seq);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "MutableSeq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull MutableSeqIterator<E> seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull MutableSeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultMutableSeqIterator<>(this, index);
    }

    @Override
    default @NotNull java.util.List<E> asJava() {
        return this.supportsFastRandomAccess()
                ? new AsJavaConvert.MutableIndexedSeqAsJava<>(this)
                : new AsJavaConvert.MutableSeqAsJava<>(this);
    }

    //endregion

    @Contract(mutates = "this")
    void set(int index, E newValue);

    default void swap(int index1, int index2) {
        final E old1 = this.get(index1);
        final E old2 = this.get(index2);

        this.set(index1, old2);
        this.set(index2, old1);
    }

    @Contract(mutates = "this")
    default void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.apply(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.apply(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void sort() {
        sort(null);
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void sort(Comparator<? super E> comparator) {
        Object[] values = toArray();
        Arrays.sort(values, (Comparator<? super Object>) comparator);

        for (int i = 0; i < values.length; i++) {
            this.set(i, (E) values[i]);
        }
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size / 2; i++) {
            swap(i, size - i - 1);
        }
    }

    default void shuffle() {
        shuffle(ThreadLocalRandom.current());
    }

    default void shuffle(@NotNull Random random) {
        int ks = this.knownSize();
        if (ks == 0 || ks == 1) {
            return;
        }
        if (supportsFastRandomAccess() || (ks > 0 && ks <= AbstractMutableSeq.SHUFFLE_THRESHOLD)) {
            assert ks > 0;
            for (int i = ks; i > 1; i--) {
                swap(i - 1, random.nextInt(i));
            }
        } else {
            @SuppressWarnings("unchecked") final E[] arr = (E[]) this.toArray();
            ObjectArrays.shuffle(arr, random);
            this.replaceAllIndexed((i, v) -> arr[i]);
        }
    }
}
