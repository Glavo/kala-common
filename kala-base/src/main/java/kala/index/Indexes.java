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
package kala.index;

import org.jetbrains.annotations.Range;

public final class Indexes {
    private static IndexOutOfBoundsException outOfBounds(int index, int length) {
        return new IndexOutOfBoundsException("Index %s out of bounds for length %d".formatted(
                index >= 0 ? index : "~" + ~index,
                length
        ));
    }

    private static int normalizeElementIndex(int index, int length) {
        int actualIndex = length - ~index;
        if (actualIndex < 0) {
            throw outOfBounds(index, length);
        }
        return actualIndex;
    }

    public static int checkElementIndex(@Index int index, @Range(from = 0, to = Integer.MAX_VALUE) int length) {
        if (index >= length) {
            throw outOfBounds(index, length);
        }
        if (index >= 0) {
            return index;
        }
        return normalizeElementIndex(index, length);
    }

    private static int normalizePositionIndex(int index, int length) {
        int actualIndex = length - ~index;
        if (actualIndex <= 0) {
            throw outOfBounds(index, length);
        }
        return actualIndex;
    }

    public static int checkPositionIndex(@Index int index, @Range(from = 0, to = Integer.MAX_VALUE) int length) {
        if (index > length) {
            throw outOfBounds(index, length);
        }
        if (index >= 0) {
            return index;
        }
        return normalizePositionIndex(index, length);
    }


}
