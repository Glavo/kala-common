package kala.collection.primitive;

import kala.collection.AnySet;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.primitive.*;
import kala.collection.primitive.internal.view.${Type}SetViews;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.*;

public interface ${Type}Set extends PrimitiveSet<${WrapperType}>, ${Type}Collection, ${Type}SetLike  {

    //region Static Factories

    static ${Type}CollectionFactory<?, ${Type}Set> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Set.factory());
    }

    static @NotNull ${Type}Set empty() {
        return Immutable${Type}Set.empty();
    }

    static @NotNull ${Type}Set of() {
        return Immutable${Type}Set.of();
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1) {
        return Immutable${Type}Set.of(value1);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Set.of(value1, value2);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Set.of(value1, value2, value3);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Set.of(value1, value2, value3, value4);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Set.of(value1, value2, value3, value4, value5);
    }

    static @NotNull ${Type}Set of(${PrimitiveType}... values) {
        return Immutable${Type}Set.of(values);
    }

    static @NotNull ${Type}Set from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Set.from(values);
    }

    static @NotNull ${Type}Set from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Set.from(values);
    }

    static @NotNull ${Type}Set from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Set.from(it);
    }

    //endregion

    static int hashCode(@NotNull ${Type}Set set) {
        int h = SET_HASH_MAGIC;
        ${Type}Iterator it = set.iterator();
        while (it.hasNext()) {
            h += ${WrapperType}.hashCode(it.next${Type}());
        }
        return h;
    }

    static boolean equals(@NotNull ${Type}Set set1, @NotNull AnySet<?> set2) {
        if (set1 == set2) return true;
        if (!set1.canEqual(set2) || !set2.canEqual(set1)) return false;

        if (set1.size() != set2.size()) return false;

        if (set2 instanceof ${Type}Set) {
            return set1.containsAll((${Type}Set) set2);
        } else {
            // TODO: return set1.containsAll(set2.asGeneric());
            for (Object v : set2.asGeneric()) {
                if (!(v instanceof ${WrapperType}) || !set1.contains((${WrapperType}) v))
                    return false;
            }
            return true;
        }
    }

    @Override
    default boolean contains(${PrimitiveType} value) {
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
        return new ${Type}SetViews.Of<>(this);
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
