package asia.kala.collection;

import asia.kala.Equals;
import asia.kala.annotations.Covariant;
import asia.kala.collection.internal.AsJavaConvert;
import asia.kala.collection.immutable.*;
import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.Traversable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Collection<@Covariant E> extends Traversable<E>, Equals {

    int SEQ_HASH_MAGIC = -1140647423;

    int SET_HASH_MAGIC = 1045751549;

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> Collection<E> narrow(Collection<? extends E> collection) {
        return (Collection<E>) collection;
    }

    @NotNull
    @Contract(pure = true)
    static <E> CollectionFactory<E, ?, ? extends Collection<E>> factory() {
        return ImmutableCollection.factory();
    }

    @NotNull
    default Spliterator<E> spliterator() {
        final int knownSize = knownSize();
        if (knownSize != 0) {
            return Spliterators.spliterator(iterator(), knownSize, 0);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }

    @NotNull
    default View<E> view() {
        return new Views.Of<>(this);
    }

    default String className() {
        return "Traversable";
    }

    @NotNull
    default <U> CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @NotNull
    default Stream<E> parallelStream() {
        return stream().parallel();
    }

    @NotNull
    default java.util.Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Collection<?>;
    }

    @NotNull
    default Seq<E> toSeq() {
        return toImmutableSeq();
    }

    @NotNull
    default ImmutableSeq<E> toImmutableSeq() {
        return toImmutableVector();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    default ImmutableArray<E> toImmutableArray() {
        return (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(toArray());
    }

    @NotNull
    default ImmutableList<E> toImmutableList() {
        return ImmutableList.from(this);
    }

    @NotNull
    default ImmutableVector<E> toImmutableVector() {
        return ImmutableVector.from(this);
    }
}
