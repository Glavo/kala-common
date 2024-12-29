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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import java.util.Objects;

@ApiStatus.Experimental
public final class IndexRange {

    private static final IndexRange EMPTY = new IndexRange(0, 0);
    private static final IndexRange ALL = new IndexRange(0, ~0);

    public static IndexRange empty() {
        return EMPTY;
    }

    public static IndexRange all() {
        return ALL;
    }

    public static IndexRange of(int beginIndex, int endIndex) {
        if ((beginIndex ^ endIndex) >= 0) {
            if (beginIndex > endIndex) {
                throw new IndexOutOfBoundsException("beginIndex > endIndex");
            }
        }

        return new IndexRange(beginIndex, endIndex);
    }

    public static IndexRange from(int beginIndex) {
        return new IndexRange(beginIndex, ~0);
    }

    public static IndexRange until(int endIndex) {
        return new IndexRange(0, endIndex);
    }

    private final int beginIndex;
    private final int endIndex;

    private IndexRange(int beginIndex, int endIndex) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    private static int check(int index, int length) {
        if (index >= 0) {
            if (index > length) {
                throw new IndexOutOfBoundsException(index);
            }
            return index;
        } else {
            int actualIndex = length - ~index;
            if (actualIndex < 0) {
                throw new IndexOutOfBoundsException("Index out of range: ~" + ~index);
            }
            return actualIndex;
        }
    }

    public IndexRange check(@Range(from = 0, to = Integer.MAX_VALUE) int length) {
        if (beginIndex >= 0 && endIndex >= 0) {
            Objects.checkFromToIndex(beginIndex, endIndex, length);
            return this;
        } else {
            int actualBeginIndex = check(beginIndex, length);
            int actualEndIndex = check(endIndex, length);
            if (actualBeginIndex > actualEndIndex) {
                throw new IndexOutOfBoundsException("beginIndex > endIndex");
            }
            return new IndexRange(actualBeginIndex, actualEndIndex);
        }
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IndexRange that && beginIndex == that.beginIndex && endIndex == that.endIndex;
    }

    @Override
    public int hashCode() {
        return beginIndex ^ endIndex;
    }

    private static void appendIndex(StringBuilder builder, int index) {
        if (index >= 0) {
            builder.append(index);
        } else {
            builder.append('~').append(~index);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        appendIndex(builder, beginIndex);
        builder.append(", ");
        appendIndex(builder, endIndex);
        builder.append(')');
        return builder.toString();
    }
}
