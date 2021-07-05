package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface BooleanComparator extends PrimitiveComparator<Boolean, BooleanComparator> {

    static @NotNull BooleanComparator naturalOrder() {
        return BooleanComparators.NaturalOrderComparator.INSTANCE;
    }

    static @NotNull BooleanComparator reverseOrder() {
        return BooleanComparators.ReverseOrderComparator.INSTANCE;
    }

    int compare(boolean b1, boolean b2);

    @Override
    default int compare(Boolean o1, Boolean o2) {
        return compare(o1.booleanValue(), o2.booleanValue());
    }

    default @NotNull BooleanComparator nullsFirst() {
        return new BooleanComparators.NullComparator(true, this);
    }

    default @NotNull BooleanComparator nullsLast() {
        return new BooleanComparators.NullComparator(false, this);
    }

    @Override
    default @NotNull BooleanComparator reversed() {
        return (BooleanComparator & Serializable) (b1, b2) -> compare(b2, b1);
    }
}
