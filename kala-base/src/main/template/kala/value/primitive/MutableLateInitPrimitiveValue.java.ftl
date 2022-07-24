package kala.value.primitive;

public final class MutableLateInit${Type}Value extends Abstract${Type}Value implements Mutable${Type}Value {
    private volatile boolean initialized = false;
    private ${PrimitiveType} value;

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void set(${PrimitiveType} value) {
        this.value = value;
        this.initialized = true;
    }

    @Override
    public ${PrimitiveType} get() {
        if (!initialized) {
            throw new IllegalStateException("Uninitialized LateInitValue");
        }
        return value;
    }

    @Override
    public String toString() {
        if (initialized) {
            return "MutableLateInit${Type}Value[" + value + ']';
        } else {
            return "MutableLateInit${Type}Value[<uninitialized>]";
        }
    }
}
