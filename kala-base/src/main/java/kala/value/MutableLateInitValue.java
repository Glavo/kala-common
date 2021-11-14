package kala.value;

public final class MutableLateInitValue<T> implements MutableValue<T> {
    private volatile boolean initialized = false;
    private T value;

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void set(T value) {
        this.value = value;
        this.initialized = true;
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
            return "MutableLateInitValue[" + value + ']';
        } else {
            return "MutableLateInitValue[<uninitialized>]";
        }
    }
}
