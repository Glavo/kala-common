package kala;

public interface Equatable {
    default boolean canEqual(Object other) {
        return true;
    }

    boolean equals(Object other);
}
