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
import kala.collection.base.Mappable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A tuple of 1 element.
 *
 * @param <T1> type of the 1st element
 * @author Glavo
 */
public record Tuple1<@Covariant T1>(T1 component1) implements HList<T1, Unit>, Mappable<T1>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <T1> Tuple1<T1> narrow(HList<? extends T1, ? extends Unit> tuple) {
        return (Tuple1<T1>) tuple;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        if (index == 0) {
            return (U) component1;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this.component1;
        return arr;
    }

    @Override
    public T1 head() {
        return component1;
    }

    @Override
    public @NotNull Unit tail() {
        return Unit.INSTANCE;
    }

    @Override
    @Contract("_ -> new")
    public <H> @NotNull Tuple2<H, T1> cons(H head) {
        return new Tuple2<>(head, component1);
    }

    @Override
    public <U> @NotNull Tuple1<U> map(@NotNull Function<? super T1, ? extends U> mapper) {
        return new Tuple1<>(mapper.apply(component1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple1<?> other) {
            return Objects.equals(this.component1, other.component1);
        }

        if (o instanceof AnyTuple other) {
            return other.arity() == 1 && Objects.equals(this.component1, other.elementAt(0));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(component1);
    }

    @Override
    public String toString() {
        return "(" + component1 + ")" ;
    }

    @Serial
    private Object writeReplace() {
        return new SerializedTuple(toArray());
    }
}
