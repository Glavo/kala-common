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

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * A tuple of 3 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @author Glavo
 */
public record Tuple3<@Covariant T1, @Covariant T2, @Covariant T3>(
        T1 component1, T2 component2, T3 component3) implements HList<T1, Tuple2<T2, T3>>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3> Tuple3<T1, T2, T3> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends Unit>>> tuple) {
        return (Tuple3<T1, T2, T3>) tuple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) component1;
            case 1 -> (U) component2;
            case 2 -> (U) component3;
            default -> throw new IndexOutOfBoundsException("Index out of range: " + index);
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this.component1;
        arr[1] = (U) this.component2;
        arr[2] = (U) this.component3;
        return arr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T1 head() {
        return component1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tuple2<T2, T3> tail() {
        return Tuple.of(component2, component3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract("_ -> new")
    public <H> @NotNull Tuple4<H, T1, T2, T3> cons(H head) {
        return new Tuple4<>(head, component1, component2, component3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple3<?, ?, ?> other) {
            return Objects.equals(this.component1, other.component1)
                    && Objects.equals(this.component2, other.component2)
                    && Objects.equals(this.component3, other.component3);
        }

        if (o instanceof AnyTuple other) {
            return other.arity() == 3
                    && Objects.equals(this.component1, other.elementAt(0))
                    && Objects.equals(this.component2, other.elementAt(1))
                    && Objects.equals(this.component3, other.elementAt(2));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(component1);
        hash = 31 * hash + Objects.hashCode(component2);
        hash = 31 * hash + Objects.hashCode(component3);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ", " + component3 + ")";
    }

    @Serial
    private Object writeReplace() {
        return new SerializedTuple(toArray());
    }
}
