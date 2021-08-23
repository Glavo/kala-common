package kala.value;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

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
            throw new NoSuchElementException("Uninitialized LateInitValue");
        }
        return value;
    }

    public @Nullable T getOrNull() {
        return initialized ? value : null;
    }


    public @NotNull Option<T> getOption() {
        return initialized ? Option.some(value) : Option.none();
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
