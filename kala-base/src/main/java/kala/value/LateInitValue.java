/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.value;

import java.util.function.Supplier;

public final class LateInitValue<T> implements Value<T> {
    private volatile boolean initialized = false;
    private T value;

    public boolean isInitialized() {
        return initialized;
    }

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

    public T computeIfUninitialized(Supplier<? extends T> supplier) {
        if (initialized) {
            return value;
        }

        synchronized (this) {
            if (initialized) {
                return value;
            }

            return value = supplier.get();
        }
    }

    @Override
    public T get() {
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
            return "LateInitValue[" + value + ']';
        } else {
            return "LateInitValue[<uninitialized>]";
        }
    }
}
