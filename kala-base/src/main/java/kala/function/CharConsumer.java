package kala.function;

import java.util.Objects;

@FunctionalInterface
public interface CharConsumer {
    void accept(char value);

    default CharConsumer andThen(CharConsumer after) {
        Objects.requireNonNull(after);
        return (char t) -> { accept(t); after.accept(t); };
    }
}
