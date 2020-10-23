package asia.kala.comparator;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface BooleanComparator extends PrimitiveComparator<Boolean, BooleanComparator> {

    @NotNull
    static BooleanComparator naturalOrder() {
        return BooleanComparators.NaturalOrderComparator.INSTANCE;
    }

    @NotNull
    static BooleanComparator reverseOrder() {
        return BooleanComparators.ReverseOrderComparator.INSTANCE;
    }

    int compare(boolean b1, boolean b2);

    @Override
    default int compare(Boolean o1, Boolean o2) {
        return compare(o1.booleanValue(), o2.booleanValue());
    }

    @NotNull
    default BooleanComparator nullsFirst() {
        return new BooleanComparators.NullComparator(true, this);
    }

    @NotNull
    default BooleanComparator nullsLast() {
        return new BooleanComparators.NullComparator(false, this);
    }

    @NotNull
    @Override
    default BooleanComparator reversed() {
        return (BooleanComparator & Serializable) (b1, b2) -> compare(b2, b1);
    }
}
