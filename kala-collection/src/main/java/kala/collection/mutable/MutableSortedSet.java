package kala.collection.mutable;

import kala.collection.SortedSet;
import kala.collection.internal.convert.AsJavaConvert;
import org.jetbrains.annotations.NotNull;

public interface MutableSortedSet<E> extends MutableSet<E>, SortedSet<E> {
    @Override
    default @NotNull java.util.SortedSet<E> asJava() {
        return new AsJavaConvert.MutableSortedSetAsJava<>(this);
    }
}
