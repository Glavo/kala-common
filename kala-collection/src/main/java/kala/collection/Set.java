package kala.collection;

import kala.collection.base.Iterators;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.ImmutableSet;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.internal.view.SetViews;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;

public interface Set<E> extends Collection<E>, SetLike<E>, AnySet<E> {

    //region Static Factories

    static <E> CollectionFactory<E, ?, Set<E>> factory() {
        return CollectionFactory.narrow(ImmutableSet.factory());
    }

    static <E> @NotNull Set<E> wrapJava(java.util.@NotNull Set<E> source) {
        return new FromJavaConvert.SetFromJava<>(source);
    }

    //endregion

    static int hashCode(@NotNull Set<?> set) {
        int h = SET_HASH_MAGIC;
        for (Object e : set) {
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    static boolean equals(@NotNull Set<?> set1, @NotNull Set<?> set2) {
        if (!set1.canEqual(set2) || !set2.canEqual(set1)) {
            return false;
        }
        return set1.size() == set2.size() && set1.containsAll(set2);
    }

    @Override
    default boolean contains(Object value) {
        return Iterators.contains(iterator(), value);
    }

    default Predicate<E> asPredicate() {
        return (Predicate<E> & Serializable) this::contains;
    }

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "Set";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Set<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull Spliterator<E> spliterator() {
        int knownSize = this.knownSize();
        return knownSize >= 0
                ? Spliterators.spliterator(iterator(), knownSize, Spliterator.DISTINCT)
                : Spliterators.spliteratorUnknownSize(iterator(), Spliterator.DISTINCT);
    }

    @Override
    default @NotNull SetView<E> view() {
        return new SetViews.Of<>(this);
    }

    @Override
    default java.util.@NotNull Set<E> asJava() {
        return new AsJavaConvert.SetAsJava<>(this);
    }

    //endregion

    @Override
    default @NotNull ImmutableSet<E> filter(@NotNull Predicate<? super E> predicate) {
        return ImmutableSet.from(view().filter(predicate)); // TODO
    }

    @Override
    default @NotNull ImmutableSet<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return ImmutableSet.from(view().filterNot(predicate)); // TODO
    }
}
