package kala.collection.mutable;

import kala.function.IndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;

class MutableCopyOnWriteSeqBase<E, S extends MutableSeq<E>> extends AbstractMutableSeq<E> {
    S source;
    boolean exclusive = true;

    MutableCopyOnWriteSeqBase(S source, boolean exclusive) {
        this.source = source;
        this.exclusive = exclusive;
    }

    @SuppressWarnings("unchecked")
    void ensureExclusive() {
        if (!exclusive) {
            source = (S) source.clone();
            exclusive = true;
        }
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return source.iterator();
    }

    @Override
    public @NotNull Iterator<E> iterator(int beginIndex) {
        return source.iterator(beginIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return source.spliterator();
    }

    @Override
    public @NotNull Stream<E> stream() {
        return source.stream();
    }

    @Override
    public @NotNull Stream<E> parallelStream() {
        return source.parallelStream();
    }

    //region Size Info


    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public int knownSize() {
        return source.knownSize();
    }

    //endregion

    @Override
    public void set(int index, E newValue) {
        ensureExclusive();
        source.set(index, newValue);
    }

    @Override
    public void swap(int index1, int index2) {
        ensureExclusive();
        source.swap(index1, index2);
    }

    @Override
    public void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        ensureExclusive();
        source.replaceAll(operator);
    }

    @Override
    public void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        ensureExclusive();
        source.replaceAllIndexed(operator);
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        ensureExclusive();
        source.sort(comparator);
    }

    @Override
    public void reverse() {
        ensureExclusive();
        source.reverse();
    }

    @Override
    public void shuffle(@NotNull Random random) {
        ensureExclusive();
        source.shuffle(random);
    }
}
