package asia.kala.collection.mutable;

import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.collection.internal.FromJavaConvert;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.IndexedSeq;
import asia.kala.collection.Seq;
import asia.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public interface MutableSeq<E> extends MutableCollection<E>, Seq<E> {

    //region Factory methods

    @NotNull
    static <E> CollectionFactory<E, ?, ? extends MutableSeq<E>> factory() {
        return MutableArray.factory();
    }

    @NotNull
    static <E> MutableSeq<E> of() {
        return MutableArray.of();
    }

    @NotNull
    @Contract("_ -> new")
    static <E> MutableSeq<E> of(E value1) {
        return MutableArray.of(value1);
    }

    @NotNull
    @Contract("_, _ -> new")
    static <E> MutableSeq<E> of(E value1, E value2) {
        return MutableArray.of(value1, value2);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    static <E> MutableSeq<E> of(E value1, E value2, E value3) {
        return MutableArray.of(value1, value2, value3);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    static <E> MutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return MutableArray.of(value1, value2, value3, value4);
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    static <E> MutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArray.of(value1, value2, value3, value4, value5);
    }

    @NotNull
    @SafeVarargs
    static <E> MutableSeq<E> of(E... values) {
        return from(values);
    }

    @NotNull
    static <E> MutableSeq<E> from(E @NotNull [] values) {
        return MutableArray.from(values);
    }

    @NotNull
    static <E> MutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArray.from(values);
    }

    @NotNull
    @Contract("_ -> new")
    static <E> MutableSeq<E> wrapJava(@NotNull List<E> list) {
        Objects.requireNonNull(list);
        if (list instanceof RandomAccess) {
            return new FromJavaConvert.MutableIndexedSeqFromJava<>(list);
        }
        return new FromJavaConvert.MutableSeqFromJava<>(list);
    }

    //endregion

    @Contract(mutates = "this")
    void set(int index, E newValue);

    @Contract(mutates = "this")
    default void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void mapInPlaceIndexed(@NotNull IndexedFunction<? super E, ? extends E> mapper) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, mapper.apply(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void sort() {
        sort((Comparator<? super E>) Comparator.naturalOrder());
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

    //
    // -- MutableCollection
    //

    @Override
    default String className() {
        return "MutableSeq";
    }

    @NotNull
    @Override
    default <U> CollectionFactory<U, ?, ? extends MutableSeq<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    default MutableSeqEditor<E, ? extends MutableSeq<E>> edit() {
        return new MutableSeqEditor<>(this);
    }

    @NotNull
    @Override
    default List<E> asJava() {
        if (this instanceof IndexedSeq<?>) {
            return new AsJavaConvert.MutableIndexedSeqAsJava<>((MutableSeq<E> & IndexedSeq<E>) this);
        }
        return new AsJavaConvert.MutableSeqAsJava<>(this);
    }
}
