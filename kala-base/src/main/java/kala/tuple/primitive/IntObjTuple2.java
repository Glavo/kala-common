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
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

public record IntObjTuple2<T>(int component1, T component2) implements PrimitiveTuple {
    @Serial
    private static final long serialVersionUID = 0L;

    public static <T> @NotNull IntObjTuple2<T> of(int i, T t) {
        return new IntObjTuple2<>(i, t);
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) Integer.valueOf(component1);
            case 1 -> (U) component2;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntObjTuple2<?> other) {
            return component1 == other.component1 && Objects.equals(component2, other.component2);
        }

        if (o instanceof Map.Entry<?, ?> other) {
            return Conditions.equals(component1, other.getKey()) && Conditions.equals(component2, other.getValue());
        }

        if (o instanceof AnyTuple other) {
            return other.arity() == 2
                    && Conditions.equals(component1, other.elementAt(0))
                    && Conditions.equals(component2, other.elementAt(1));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(component1) ^ Objects.hashCode(component2);
    }

    @Override
    public String toString() {
        return "IntObjTuple2(" + component1 + ", " + component2 + ")";
    }
}
