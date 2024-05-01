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

public enum RangeType {
    OPEN(BoundType.OPEN, BoundType.OPEN),
    CLOSED(BoundType.CLOSED, BoundType.CLOSED),
    OPEN_CLOSED(BoundType.OPEN, BoundType.CLOSED),
    CLOSED_OPEN(BoundType.CLOSED, BoundType.OPEN),
    GREATER_THAN(BoundType.OPEN, BoundType.INFINITY),
    AT_LEAST(BoundType.CLOSED, BoundType.INFINITY),
    LESS_THAN(BoundType.INFINITY, BoundType.OPEN),
    AT_MOST(BoundType.INFINITY, BoundType.CLOSED),
    EMPTY(BoundType.OPEN, BoundType.OPEN),
    ALL(BoundType.INFINITY, BoundType.INFINITY);

    private final BoundType lowerBoundType;
    private final BoundType upperBoundType;

    RangeType(BoundType lowerBoundType, BoundType upperBoundType) {
        this.lowerBoundType = lowerBoundType;
        this.upperBoundType = upperBoundType;
    }

    public BoundType getLowerBoundType() {
        return lowerBoundType;
    }

    public BoundType getUpperBoundType() {
        return upperBoundType;
    }
}
