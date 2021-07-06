package kala.value;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public final class LateInitValue<T> implements Value<T> {
    private volatile boolean initialized = false;
    private T value;

    public final void initialize(T value) {
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

    public final boolean isInitialized() {
        return initialized;
    }

    @Override
    public final T get() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    throw new NoSuchElementException("Uninitialized LateInitValue");
                }
            }
        }
        return value;
    }

    public final @Nullable T getOrNull() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    return null;
                }
            }
        }
        return value;
    }


    public final @NotNull Option<T> getOption() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    return Option.none();
                }
            }
        }
        return Option.some(value);
    }

    @Override
    public final String toString() {
        if (initialized) {
            return "LateInitValue[" + value + "]";
        } else {
            return "LateInitValue[<uninitialized>]";
        }
    }
}
