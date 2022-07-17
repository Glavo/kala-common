package kala.collection;

import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.primitive.*;
import kala.collection.primitive.internal.view.${Type}SetViews;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.*;

public interface ${Type}Set extends Collection<E>, SetLike<E>, Any${Type}Set {

    //region Static Factories

    static ${Type}CollectionFactory<?, ${Type}Set> factory() {
        return CollectionFactory.narrow(Immutable${Type}Set.factory());
    }

    static <E> @NotNull ${Type}Set empty() {
        return Immutable${Type}Set.empty();
    }

    static <E> @NotNull ${Type}Set of() {
        return Immutable${Type}Set.of();
    }

    static <E> @NotNull ${Type}Set of(E value1) {
        return Immutable${Type}Set.of(value1);
    }

    static <E> @NotNull ${Type}Set of(E value1, E value2) {
        return Immutable${Type}Set.of(value1, value2);
    }

    static <E> @NotNull ${Type}Set of(E value1, E value2, E value3) {
        return Immutable${Type}Set.of(value1, value2, value3);
    }

    static <E> @NotNull ${Type}Set of(E value1, E value2, E value3, E value4) {
        return Immutable${Type}Set.of(value1, value2, value3, value4);
    }

    static <E> @NotNull ${Type}Set of(E value1, E value2, E value3, E value4, E value5) {
        return Immutable${Type}Set.of(value1, value2, value3, value4, value5);
    }

    static <E> @NotNull ${Type}Set of(E... values) {
        return Immutable${Type}Set.of(values);
    }

    static <E> @NotNull ${Type}Set from(E @NotNull [] values) {
        return Immutable${Type}Set.from(values);
    }

    static <E> @NotNull ${Type}Set from(@NotNull Iterable<? extends E> values) {
        return Immutable${Type}Set.from(values);
    }

    static <E> @NotNull ${Type}Set from(@NotNull Iterator<? extends E> it) {
        return Immutable${Type}Set.from(it);
    }

    //endregion

    static int hashCode(@NotNull ${Type}Set set) {
        int h = SET_HASH_MAGIC;
        for (Object e : set) {
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    static boolean equals(@NotNull ${Type}Set set1, @NotNull AnySet<?> set2) {
        if (set1 == set2) return true;
        if (!set1.canEqual(seq2) || !set2.canEqual(seq1)) return false;

        if (set1.size() != set2.size()) return false;

        if (set2 instanceof ${Type}Set) {
            return set1.containsAll(set2);
        } else {
            return set1.containsAll(set2.asGeneric());
        }
    }

    @Override
    default boolean contains(Object value) {
        return iterator().contains(value);
    }

    @Override
    default @NotNull String className() {
        return "${Type}Set";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends ${Type}Set> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull ${Type}SetView view() {
        return new ${Type}SetViews.Of(this);
    }

    @Override
    default @NotNull Immutable${Type}Set filter(@NotNull ${Type}Predicate predicate) {
        return Immutable${Type}Set.from(view().filter(predicate));
    }

    @Override
    default @NotNull Immutable${Type}Set filterNot(@NotNull ${Type}Predicate predicate) {
        return Immutable${Type}Set.from(view().filterNot(predicate));
    }
}
