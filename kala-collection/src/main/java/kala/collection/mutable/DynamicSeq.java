package kala.collection.mutable;

import kala.Conditions;
import kala.annotations.ReplaceWith;
import kala.collection.ArraySeq;
import kala.collection.Seq;
import kala.collection.base.Growable;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.SeqIterators;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.factory.CollectionFactory;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface DynamicSeq<E> extends MutableSeq<E>, Growable<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, DynamicSeq<E>> factory() {
        return CollectionFactory.narrow(DynamicArray.factory());
    }

    @Contract("-> new")
    static <E> @NotNull DynamicSeq<E> create() {
        return new DynamicArray<>();
    }

    @Contract("-> new")
    static <E> @NotNull DynamicSeq<E> of() {
        return DynamicArray.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull DynamicSeq<E> of(E value1) {
        return DynamicArray.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull DynamicSeq<E> of(E value1, E value2) {
        return DynamicArray.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull DynamicSeq<E> of(E value1, E value2, E value3) {
        return DynamicArray.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull DynamicSeq<E> of(E value1, E value2, E value3, E value4) {
        return DynamicArray.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull DynamicSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return DynamicArray.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull DynamicSeq<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull DynamicSeq<E> from(E @NotNull [] values) {
        return DynamicArray.from(values);
    }

    static <E> @NotNull DynamicSeq<E> from(@NotNull Iterable<? extends E> values) {
        return DynamicArray.from(values);
    }

    static <E> @NotNull DynamicSeq<E> from(@NotNull Iterator<? extends E> it) {
        return DynamicArray.from(it);
    }

    static <E> @NotNull DynamicSeq<E> from(@NotNull Stream<? extends E> stream) {
        return DynamicArray.from(stream);
    }

    @Contract("_ -> new")
    static <E> @NotNull DynamicSeq<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof AsJavaConvert.DynamicSeqAsJava<?, ?>) {
            return ((AsJavaConvert.DynamicSeqAsJava<E, DynamicSeq<E>>) list).source;
        }
        return list instanceof RandomAccess
                ? new FromJavaConvert.DynamicIndexedSeqFromJava<>(list)
                : new FromJavaConvert.DynamicSeqFromJava<>(list);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull
    String className() {
        return "DynamicSeq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends DynamicSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull
    DynamicSeqIterator<E> seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull
    DynamicSeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultDynamicSeqIterator<>(this, index);
    }

    @Override
    default @NotNull
    DynamicSeqEditor<E, ? extends DynamicSeq<E>> edit() {
        return new DynamicSeqEditor<>(this);
    }

    @Override
    default @NotNull
    List<E> asJava() {
        return this instanceof RandomAccess
                ? new AsJavaConvert.DynamicIndexedSeqAsJava<>(this)
                : new AsJavaConvert.DynamicSeqAsJava<>(this);
    }

    //endregion

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void appendAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        final int length = values.length;
        //noinspection StatementWithEmptyBody
        if (length == 0) {
        } else if (length == 1) {
            this.append(values[0]);
        } else {
            this.appendAll(ArraySeq.wrap(values));
        }
    }

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (E e : this.toImmutableSeq()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                this.append(e);
            }
        } else {
            for (E e : values) {
                this.append(e);
            }
        }
    }

    @Override
    @ReplaceWith("append(E)")
    default void plusAssign(E value) {
        append(value);
    }

    @Override
    @ReplaceWith("appendAll(E[])")
    default void plusAssign(E @NotNull [] values) {
        appendAll(values);
    }

    @Override
    @ReplaceWith("appendAll(Iterable<E>)")
    default void plusAssign(@NotNull Iterable<? extends E> values) {
        appendAll(values);
    }

    @Contract(mutates = "this")
    void prepend(E value);

    @Contract(mutates = "this")
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        this.prependAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            Iterator<?> iterator = ((Seq<?>) values).toImmutableArray().reverseIterator(); // avoid mutating under our own iterator
            while (iterator.hasNext()) {
                //noinspection ConstantConditions
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof Seq<?>) {
            Iterator<?> iterator = ((Seq<?>) values).reverseIterator();
            while (iterator.hasNext()) {
                this.prepend((E) iterator.next());
            }
            return;
        }

        if (values instanceof List<?> && values instanceof RandomAccess) {
            List<?> seq = (List<?>) values;
            int s = seq.size();
            for (int i = s - 1; i >= 0; i--) {
                prepend((E) seq.get(i));
            }
            return;
        }

        Object[] cv = CollectionHelper.asArray(values);

        for (int i = cv.length - 1; i >= 0; i--) {
            prepend((E) cv[i]);
        }
    }

    @Contract(mutates = "this")
    void insert(int index, @Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        insertAll(index, ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
        if (values == this) {
            for (E e : this.toImmutableSeq()) { // avoid mutating under our own iterator
                //noinspection ConstantConditions
                insert(index++, e);
            }
        } else {
            for (E e : values) {
                insert(index++, e);
            }
        }
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    E removeAt(@Range(from = 0, to = Integer.MAX_VALUE) int index);

    @Contract(mutates = "this")
    default E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(0);
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull Predicate<? super E> predicate) {
        DynamicSeqIterator<E> it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
            E value = it.next();
            if (predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Contract(mutates = "this")
    default boolean retainAll(@NotNull Predicate<? super E> predicate) {
        DynamicSeqIterator<E> it = this.seqIterator();
        boolean changed = false;
        while (it.hasNext()) {
            E value = it.next();
            if (!predicate.test(value)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Contract(mutates = "this")
    void clear();

    // ---

    @Contract(mutates = "this")
    default void dropInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return;
        }
        DynamicSeqIterator<E> it = this.seqIterator();
        for (int i = 0; i < n; i++) {
            if (!it.hasNext()) {
                break;
            }
            it.next();
            it.remove();
        }
    }

    @Contract(mutates = "this")
    default void takeInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        if (n == 0) {
            clear();
            return;
        }

        final int knownSize = this.knownSize();
        if (knownSize >= 0 && n >= knownSize) {
            return;
        }
        DynamicSeqIterator<E> it = this.seqIterator(n);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Contract(mutates = "this")
    default void filterInPlace(@NotNull Predicate<? super E> predicate) {
        retainAll(predicate);
    }

    @Contract(mutates = "this")
    default void filterNotInPlace(@NotNull Predicate<? super E> predicate) {
        removeAll(predicate);
    }
}
