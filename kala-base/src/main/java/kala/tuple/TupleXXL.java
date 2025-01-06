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
package kala.tuple;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A tuple of more than 9 elements.
 *
 * @author Glavo
 */
@SuppressWarnings("unchecked")
final class TupleXXL implements HList<Object, HList<?, ?>>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private final Object @NotNull [] values;

    TupleXXL(Object @NotNull [] values) {
        this.values = values;
    }

    @Override
    public int arity() {
        return values.length;
    }

    @Override
    public <U> U elementAt(int index) {
        try {
            return (U) values[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public <H> @NotNull HList<H, TupleXXL> cons(H head) {
        int arity = arity();
        Object[] arr = new Object[arity + 1];
        arr[0] = head;
        System.arraycopy(values, 0, arr, 1, arity);
        return (HList) new TupleXXL(arr);
    }

    @Override
    public Object @NotNull [] toArray() {
        return values.clone();
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        System.arraycopy(values, 0, arr, 0, arity());
        return arr;
    }

    @Override
    public Object head() {
        return values[0];
    }

    @Override
    public @NotNull HList<?, ?> tail() {
        int arity = arity();
        if (arity == 10) {
            return new Tuple9<>(
                    values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9]
            );
        }
        return new TupleXXL(Arrays.copyOfRange(values, 1, arity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof TupleXXL other) {
            return Arrays.equals(this.values, other.values);
        }

        if (o instanceof AnyTuple other) {
            if (this.arity() != other.arity()) return false;
            for (int i = 0; i < values.length; i++) {
                if (!Objects.equals(values[i], other.elementAt(i))) return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Object value : values) {
            hash = hash * 31 + Objects.hashCode(value);
        }
        return hash;
    }

    @Override
    public String toString() {
        final Object[] values = this.values;
        final int arity = values.length;

        StringBuilder builder = new StringBuilder(6 * arity);
        builder.append('(').append(values[0]);

        for (int i = 1; i < arity; i++) {
            builder.append(", ").append(values[i]);
        }
        builder.append(')');

        return builder.toString();
    }

    @Serial
    private Object writeReplace() {
        return new SerializedTuple(values);
    }
}
