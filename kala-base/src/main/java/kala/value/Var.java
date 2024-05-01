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

import java.io.Serial;
import java.io.Serializable;

public final class Var<T> extends AbstractMutableValue<T> implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    public T value;

    public Var() {
    }

    public Var(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Var<T> clone() {
        return new Var<>(value);
    }

    @Override
    public String toString() {
        return "Var[" + value + "]";
    }
}
