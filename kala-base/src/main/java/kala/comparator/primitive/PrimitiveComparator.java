package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface PrimitiveComparator<T, C extends PrimitiveComparator<T, C>>
        extends Comparator<T> {

    @NotNull C nullsFirst();

    @NotNull C nullsLast();

    @Override
    @NotNull C reversed();
}
