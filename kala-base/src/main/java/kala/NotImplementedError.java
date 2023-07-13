package kala;

public final class NotImplementedError extends Error {
    public NotImplementedError() {
        super("An operation is not implemented");
    }

    public NotImplementedError(String message) {
        super(message);
    }
}
