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

import java.util.Map;

public record ${ClassName}(${PrimitiveType1} component1, ${PrimitiveType2} component2) implements PrimitiveTuple, Map.Entry<${WrapperType1}, ${WrapperType2}> {
    private static final long serialVersionUID = 0L;

    public static @NotNull ${ClassName} of(${PrimitiveType1} v1, ${PrimitiveType2} v2) {
        return new ${ClassName}(v1, v2);
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        return switch (index) {
            case 0 -> (U) (Object) component1;
            case 1 -> (U) (Object) component2;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public @NotNull Tuple2<${"@NotNull"} ${WrapperType1}, @NotNull ${WrapperType2}> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public ${WrapperType1} getKey() {
        return component1;
    }

    @Override
    public ${WrapperType2} getValue() {
        return component2;
    }

    @Override
    @Deprecated
    public ${WrapperType2} setValue(${WrapperType2} value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ${ClassName} other) {
            return Conditions.equals(component1, other.component1) && Conditions.equals(component2, other.component2);
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
        return ${WrapperType1}.hashCode(component1) ^ ${WrapperType2}.hashCode(component2);
    }

    @Override
    public String toString() {
        return "${ClassName}(" + component1 + ", " + component2 + ")" ;
    }
}
