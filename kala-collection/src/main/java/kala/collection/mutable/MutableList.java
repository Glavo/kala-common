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
import kala.control.Option;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface MutableList<E> extends MutableSeq<E>, Growable<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableList<E>> factory() {
        return CollectionFactory.narrow(MutableArrayList.factory());
    }

    @Contract("-> new")
    static <E> @NotNull MutableList<E> create() {
        return new MutableArrayList<>();
    }

    @Contract("-> new")
    static <E> @NotNull MutableList<E> of() {
        return MutableArrayList.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> of(E value1) {
        return MutableArrayList.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2) {
        return MutableArrayList.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3) {
        return MutableArrayList.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3, E value4) {
        return MutableArrayList.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableList<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArrayList.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableList<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableList<E> from(E @NotNull [] values) {
        return MutableArrayList.from(values);
    }

    static <E> @NotNull MutableList<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArrayList.from(values);
    }

    static <E> @NotNull MutableList<E> from(@NotNull Iterator<? extends E> it) {
        return MutableArrayList.from(it);
    }

    static <E> @NotNull MutableList<E> from(@NotNull Stream<? extends E> stream) {
        return MutableArrayList.from(stream);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableList<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof AsJavaConvert.MutableListAsJava<?, ?>) {
            return ((AsJavaConvert.MutableListAsJava<E, MutableList<E>>) list).source;
        }
        return list instanceof RandomAccess
                ? new FromJavaConvert.MutableIndexedListFromJava<>(list)
                : new FromJavaConvert.MutableListFromJava<>(list);
    }

    static <E, C extends MutableList<E>> @NotNull MutableListEditor<E, C> edit(@NotNull C seq) {
        return new MutableListEditor<>(seq);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull
    String className() {
        return "MutableList";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableList<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull
    MutableListIterator<E> seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull
    MutableListIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultMutableListIterator<>(this, index);
    }

    @Override
    default @NotNull List<E> asJava() {
        return this instanceof RandomAccess
                ? new AsJavaConvert.MutableIndexedListAsJava<>(this)
                : new AsJavaConvert.MutableListAsJava<>(this);
    }

    default @NotNull MutableStack<E> asStack() {
        return this instanceof MutableStack ? (MutableStack<E>) this : new MutableListStackAdapter<>(this);
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
    default @Nullable E removeFirstOrNull() {
        return isEmpty() ? null : removeAt(0);
    }

    @Contract(mutates = "this")
    default @NotNull Option<E> removeFirstOption() {
        return isEmpty() ? Option.none() : Option.some(removeAt(0));
    }

    @Contract(mutates = "this")
    default E removeLast() {
        final int size = this.size();
        if (size == 0) {
            throw new NoSuchElementException("Seq is empty");
        }
        return removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @Nullable E removeLastOrNull() {
        final int size = this.size();
        return size == 0 ? null : removeAt(size - 1);
    }

    @Contract(mutates = "this")
    default @NotNull Option<E> removeLastOption() {
        final int size = this.size();
        return size == 0 ? Option.none() : Option.some(removeAt(size - 1));
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull Predicate<? super E> predicate) {
        MutableListIterator<E> it = this.seqIterator();
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
        MutableListIterator<E> it = this.seqIterator();
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
        MutableListIterator<E> it = this.seqIterator();
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
        MutableListIterator<E> it = this.seqIterator(n);
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
