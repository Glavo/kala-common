package kala.value;

public final class LateInitValue<T> implements Value<T> {
    private volatile boolean initialized = false;
    private T value;

    public void initialize(T value) {
        if (initialized) {
            throw new IllegalStateException("Value is initialized");
        }
        synchronized (this) {
            if (initialized) {
                throw new IllegalStateException("Value is initialized");
            }
            this.value = value;
            this.initialized = true;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public T get() {
        if (!initialized) {
            throw new IllegalStateException("Uninitialized LateInitValue");
        }
        return value;
    }

    @Override
    public String toString() {
        if (initialized) {
            return "LateInitValue[" + value + ']';
        } else {
            return "LateInitValue[<uninitialized>]";
        }
    }
}
