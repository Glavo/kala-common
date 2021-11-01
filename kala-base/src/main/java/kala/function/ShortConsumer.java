package kala.function;

import java.util.Objects;

@FunctionalInterface
public interface ShortConsumer {
    void accept(short value);

    default ShortConsumer andThen(ShortConsumer after) {
        Objects.requireNonNull(after);
        return (short t) -> { accept(t); after.accept(t); };
    }
}
