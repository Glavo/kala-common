package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface ByteComparator extends PrimitiveComparator<Byte, ByteComparator> {
    int compare(byte b1, byte b2);

    @Override
    default int compare(Byte b1, Byte b2) {
        return compare(b1.byteValue(), b2.byteValue());
    }

    default @NotNull ByteComparator nullsFirst() {
        return new ByteComparators.NullComparator(true, this);
    }

    default @NotNull ByteComparator nullsLast() {
        return new ByteComparators.NullComparator(false, this);
    }

    @Override
    default @NotNull ByteComparator reversed() {
        return (ByteComparator & Serializable) (b1, b2) -> compare(b2, b1);
    }
}
