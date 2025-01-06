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
 * A tuple of 9 elements.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 * @param <T9> type of the 9th element
 * @author Glavo
 */
public record Tuple9<@Covariant T1, @Covariant T2, @Covariant T3, @Covariant T4, @Covariant T5, @Covariant T6, @Covariant T7, @Covariant T8, @Covariant T9>(
        T1 component1, T2 component2, T3 component3,
        T4 component4, T5 component5, T6 component6,
        T7 component7, T8 component8, T9 component9
) implements HList<T1, Tuple8<T2, T3, T4, T5, T6, T7, T8, T9>>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> narrow(
            HList<? extends T1, ? extends HList<? extends T2, ? extends HList<? extends T3, ? extends HList<? extends T4, ? extends HList<? extends T5, ? extends HList<? extends T6, ? extends HList<? extends T7, ? extends HList<? extends T8, ? extends HList<? extends T9, ? extends Unit>>>>>>>>> tuple) {
        return (Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>) tuple;
    }

    @Override
    public int arity() {
        return 9;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) component1;
            case 1 -> (U) component2;
            case 2 -> (U) component3;
            case 3 -> (U) component4;
            case 4 -> (U) component5;
            case 5 -> (U) component6;
            case 6 -> (U) component7;
            case 7 -> (U) component8;
            case 8 -> (U) component9;
            default -> throw new IndexOutOfBoundsException("Index out of range: " + index);
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        U[] arr = generator.apply(arity());
        arr[0] = (U) this.component1;
        arr[1] = (U) this.component2;
        arr[2] = (U) this.component3;
        arr[3] = (U) this.component4;
        arr[4] = (U) this.component5;
        arr[5] = (U) this.component6;
        arr[6] = (U) this.component7;
        arr[7] = (U) this.component8;
        arr[8] = (U) this.component9;
        return arr;
    }

    @Override
    public T1 head() {
        return component1;
    }

    @Override
    public @NotNull Tuple8<T2, T3, T4, T5, T6, T7, T8, T9> tail() {
        return Tuple.of(component2, component3, component4, component5, component6, component7, component8, component9);
    }

    @Override
    @Contract("_ -> new")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <H> @NotNull HList<H, Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> cons(H head) {
        Object[] arr = new Object[10];
        arr[0] = head;
        arr[1] = component1;
        arr[2] = component2;
        arr[3] = component3;
        arr[4] = component4;
        arr[5] = component5;
        arr[6] = component6;
        arr[7] = component7;
        arr[8] = component8;
        arr[9] = component9;
        return (HList) new TupleXXL(arr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> other) {
            return Objects.equals(this.component1, other.component1)
                    && Objects.equals(this.component2, other.component2)
                    && Objects.equals(this.component3, other.component3)
                    && Objects.equals(this.component4, other.component4)
                    && Objects.equals(this.component5, other.component5)
                    && Objects.equals(this.component6, other.component6)
                    && Objects.equals(this.component7, other.component7)
                    && Objects.equals(this.component8, other.component8)
                    && Objects.equals(this.component9, other.component9);
        }

        if (o instanceof AnyTuple other) {
            return other.arity() == 9
                    && Objects.equals(this.component1, other.elementAt(0))
                    && Objects.equals(this.component2, other.elementAt(1))
                    && Objects.equals(this.component3, other.elementAt(2))
                    && Objects.equals(this.component4, other.elementAt(3))
                    && Objects.equals(this.component5, other.elementAt(4))
                    && Objects.equals(this.component6, other.elementAt(5))
                    && Objects.equals(this.component7, other.elementAt(6))
                    && Objects.equals(this.component8, other.elementAt(7))
                    && Objects.equals(this.component9, other.elementAt(8));
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + Objects.hashCode(component1);
        hash = 31 * hash + Objects.hashCode(component2);
        hash = 31 * hash + Objects.hashCode(component3);
        hash = 31 * hash + Objects.hashCode(component4);
        hash = 31 * hash + Objects.hashCode(component5);
        hash = 31 * hash + Objects.hashCode(component6);
        hash = 31 * hash + Objects.hashCode(component7);
        hash = 31 * hash + Objects.hashCode(component8);
        hash = 31 * hash + Objects.hashCode(component9);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ", " + component3 + ", " + component4 + ", " + component5 + ", " + component6 + ", " + component7 + ", " + component8 + ", " + component9 + ")" ;
    }

    @Serial
    private Object writeReplace() {
        return new SerializedTuple(toArray());
    }
}
