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
package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public record IntTuple3(int component1, int component2, int component3) implements PrimitiveTuple {
    @Serial
    private static final long serialVersionUID = 0L;

    public static @NotNull IntTuple3 of(int i1, int i2, int i3) {
        return new IntTuple3(i1, i2, i3);
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) Integer.valueOf(component1);
            case 1 -> (U) Integer.valueOf(component2);
            case 2 -> (U) Integer.valueOf(component3);
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public @NotNull Tuple3<@NotNull Integer, @NotNull Integer, @NotNull Integer> toTuple3() {
        return Tuple.of(component1, component2, component3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntTuple3 other) {
            return component1 == other.component1 && component2 == other.component2 && this.component3 == other.component3;
        }

        if (o instanceof AnyTuple other) {
            return other.arity() == 3
                    && Conditions.equals(component1, other.elementAt(0))
                    && Conditions.equals(component2, other.elementAt(1))
                    && Conditions.equals(component3, other.elementAt(2));
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + component1;
        hash = 31 * hash + component2;
        hash = 31 * hash + component3;
        return hash;
    }

    @Override
    public String toString() {
        return "IntTuple3(" + component1 + ", " + component2 + ", " + component3 + ")" ;
    }
}
