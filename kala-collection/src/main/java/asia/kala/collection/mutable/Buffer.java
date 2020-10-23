package asia.kala.collection.mutable;

import asia.kala.collection.*;
import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.collection.internal.FromJavaConvert;
import asia.kala.collection.internal.CollectionHelper;
import asia.kala.factory.CollectionFactory;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Predicate;

public interface Buffer<E> extends MutableSeq<E> {

    //region Factory methods

    @NotNull
    static <E> CollectionFactory<E, ?, ? extends Buffer<E>> factory() {
        return ArrayBuffer.factory();
    }

    @NotNull
    @Contract("-> new")
    static <E> Buffer<E> of() {
        return ArrayBuffer.of();
    }

    @NotNull
    @Contract("_ -> new")
    static <E> Buffer<E> of(E value1) {
        return ArrayBuffer.of(value1);
    }

    @NotNull
    @Contract("_, _ -> new")
    static <E> Buffer<E> of(E value1, E value2) {
        return ArrayBuffer.of(value1, value2);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    static <E> Buffer<E> of(E value1, E value2, E value3) {
        return ArrayBuffer.of(value1, value2, value3);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    static <E> Buffer<E> of(E value1, E value2, E value3, E value4) {
        return ArrayBuffer.of(value1, value2, value3, value4);
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    static <E> Buffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ArrayBuffer.of(value1, value2, value3, value4, value5);
    }

    @NotNull
    @SafeVarargs
    static <E> Buffer<E> of(E... values) {
        return from(values);
    }

    @NotNull
    static <E> Buffer<E> from(E @NotNull [] values) {
        return ArrayBuffer.from(values);
    }

    @NotNull
    static <E> Buffer<E> from(@NotNull Iterable<? extends E> values) {
        return ArrayBuffer.from(values);
    }

    @NotNull
    @Contract("_ -> new")
    static <E> Buffer<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof RandomAccess) {
            return new FromJavaConvert.IndexedBufferFromJava<>(list);
        }
        return new FromJavaConvert.BufferFromJava<>(list);
    }

    //endregion

    @Contract(mutates = "this")
    void append(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void appendAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> collection
    ) {
        Objects.requireNonNull(collection);
        for (E e : collection) {
            this.append(e);
        }
    }

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
    void prepend(E value);

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void prependAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);
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
    default void prependAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        this.prependAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    void insert(int index, @Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        Objects.requireNonNull(values);

        for (E e : values) {
            insert(index++, e);
        }
    }

    @Contract(mutates = "this")
    default void insertAll(
            int index,
            @Flow(sourceIsContainer = true, targetIsContainer = true) E @NotNull [] values) {
        insertAll(index, ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    @Flow(sourceIsContainer = true)
    E remove(@Range(from = 0, to = Integer.MAX_VALUE) int index);

    @Contract(mutates = "this")
    default void remove(int index, int count) {
        for (int i = 0; i < count; i++) {
            remove(index);
        }
    }

    @Contract(mutates = "this")
    void clear();

    @Contract(mutates = "this")
    default void dropInPlace(int n) {
        if (n <= 0) {
            return;
        }
        remove(0, Integer.min(n, size()));
    }

    @Contract(mutates = "this")
    default void dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        int idx = indexWhere(predicate.negate());
        if (idx < 0) {
            clear();
        } else {
            dropInPlace(idx);
        }
    }

    @Contract(mutates = "this")
    default void takeInPlace(int n) {
        if (n <= 0) {
            clear();
            return;
        }

        final int size = this.size();
        if (n >= size) {
            return;
        }
        remove(n, size - n);
    }

    @Contract(mutates = "this")
    default void takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        int idx = indexWhere(predicate.negate());
        if (idx >= 0) {
            takeInPlace(idx);
        }
    }

    //region MutableCollection members

    @Override
    default String className() {
        return "Buffer";
    }

    @Override
    @NotNull
    default <U> CollectionFactory<U, ?, ? extends Buffer<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    default BufferEditor<E, ? extends Buffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @NotNull
    @Override
    default List<E> asJava() {
        if (this instanceof IndexedSeq<?>) {
            return new AsJavaConvert.IndexedBufferAsJava<>((Buffer<E> & IndexedSeq<E>) this);
        }
        return new AsJavaConvert.BufferAsJava<>(this);
    }

    //endregion
}
