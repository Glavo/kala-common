package asia.kala.comparator;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface ByteComparator extends PrimitiveComparator<Byte, ByteComparator> {
    int compare(byte b1, byte b2);

    @Override
    default int compare(Byte b1, Byte b2) {
        return compare(b1.byteValue(), b2.byteValue());
    }

    @NotNull
    default ByteComparator nullsFirst() {
        return new ByteComparators.NullComparator(true, this);
    }

    @NotNull
    default ByteComparator nullsLast() {
        return new ByteComparators.NullComparator(false, this);
    }

    @NotNull
    @Override
    default ByteComparator reversed() {
        return (ByteComparator & Serializable) (b1, b2) -> compare(b2, b1);
    }
}
