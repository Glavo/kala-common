package kala.value.primitive;

public final class LateInit${Type}Value implements ${Type}Value {
    private volatile boolean initialized = false;
    private ${PrimitiveType} value;

    public void initialize(${PrimitiveType} value) {
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
    public ${PrimitiveType} get() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    throw new IllegalStateException("Uninitialized LateInitValue");
                }
            }
        }
        return value;
    }

    @Override
    public String toString() {
        if (initialized) {
            return "LateInit${Type}Value[" + value + ']';
        } else {
            return "LateInit${Type}Value[<uninitialized>]";
        }
    }
}
