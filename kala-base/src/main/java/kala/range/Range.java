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
package kala.range;

import kala.range.primitive.PrimitiveRange;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public sealed interface Range<T> extends Serializable permits GenericRange, PrimitiveRange {

    @NotNull RangeType getType();

    default BoundType getLowerBoundType() {
        return getType().getLowerBoundType();
    }

    default BoundType getUpperBoundType() {
        return getType().getUpperBoundType();
    }

    default boolean hasLowerBound() {
        return getType() != RangeType.EMPTY && getLowerBoundType() != BoundType.INFINITY;
    }

    default boolean hasUpperBound() {
        return getType() != RangeType.EMPTY && getUpperBoundType() != BoundType.INFINITY;
    }
}
