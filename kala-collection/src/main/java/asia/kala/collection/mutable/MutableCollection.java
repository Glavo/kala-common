package asia.kala.collection.mutable;

import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface MutableCollection<E> extends Collection<E> {

    //region Factory methods

    @NotNull
    static <E> CollectionFactory<E, ?, ? extends MutableCollection<E>> factory() {
        return MutableSeq.factory();
    }

    @NotNull
    static <E> MutableCollection<E> of() {
        return MutableSeq.of();
    }

    @NotNull
    @Contract("_ -> new")
    static <E> MutableCollection<E> of(E value1) {
        return MutableSeq.of(value1);
    }

    @NotNull
    @Contract("_, _ -> new")
    static <E> MutableCollection<E> of(E value1, E value2) {
        return MutableSeq.of(value1, value2);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    static <E> MutableCollection<E> of(E value1, E value2, E value3) {
        return MutableSeq.of(value1, value2, value3);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    static <E> MutableCollection<E> of(E value1, E value2, E value3, E value4) {
        return MutableSeq.of(value1, value2, value3, value4);
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    static <E> MutableCollection<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableSeq.of(value1, value2, value3, value4, value5);
    }

    @NotNull
    @SafeVarargs
    static <E> MutableCollection<E> of(E... values) {
        return from(values);
    }

    @NotNull
    static <E> MutableCollection<E> from(E @NotNull [] values) {
        return MutableSeq.from(values);
    }

    @NotNull
    static <E> MutableCollection<E> from(@NotNull Iterable<? extends E> values) {
        return MutableSeq.from(values);
    }

    //endregion

    @Override
    default String className() {
        return "MutableCollection";
    }

    @NotNull
    @Override
    default <U> CollectionFactory<U, ?, ? extends MutableCollection<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    default MutableCollectionEditor<E, ? extends MutableCollection<E>> edit() {
        return new MutableCollectionEditor<>(this);
    }

    @NotNull
    @Override
    default java.util.Collection<E> asJava() {
        return new AsJavaConvert.MutableCollectionAsJava<>(this);
    }
}
