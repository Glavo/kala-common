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
package kala.range.primitive;

import kala.annotations.UnstableName;
import kala.collection.base.primitive.*;
import kala.range.BoundType;
import kala.range.RangeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
<#if IsSpecialized>
import java.util.function.${Type}Consumer;
<#else>

import kala.function.${Type}Consumer;
</#if>

public final class ${Type}Range extends IntegralRange<${WrapperType}> implements ${Type}Traversable, Serializable {
    @Serial
    private static final long serialVersionUID = ${SerialVersionUID};
    private static final int HASH_MAGIC = ${HashMagic};

    public static final ${StepType} DEFAULT_STEP =1;
    public static final ${StepType} MAX_STEP = ${MaxStep};
    public static final ${StepType} MAX_REVERSE_STEP = ${MaxReverseStep};

    private static final ${Type}Range ALL = new ${Type}Range(RangeType.CLOSED, ${WrapperType}.MIN_VALUE, ${WrapperType}.MAX_VALUE);
    private static final ${Type}Range EMPTY = new ${Type}Range(RangeType.EMPTY, ${Values.Zero}, ${Values.Zero});

    private final @NotNull RangeType type;

    private final ${PrimitiveType} lowerBound;
    private final ${PrimitiveType} upperBound;

    private ${Type}Range(@NotNull RangeType type, ${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        this.type = type;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static @NotNull ${Type}Range all() {
        return ALL;
    }

    public static @NotNull ${Type}Range empty() {
        return EMPTY;
    }

    public static @NotNull ${Type}Range is(${PrimitiveType} value) {
        return new ${Type}Range(RangeType.CLOSED, value, value);
    }

    public static @NotNull ${Type}Range open(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than upperBound");
        }
        return new ${Type}Range(RangeType.OPEN, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range closed(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new ${Type}Range(RangeType.CLOSED, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range openClosed(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new ${Type}Range(RangeType.OPEN_CLOSED, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range closedOpen(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new ${Type}Range(RangeType.CLOSED_OPEN, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range greaterThan(${PrimitiveType} lowerBound) {
        return new ${Type}Range(RangeType.OPEN_CLOSED, lowerBound, ${WrapperType}.MAX_VALUE);
    }

    public static @NotNull ${Type}Range atLeast(${PrimitiveType} lowerBound) {
        return new ${Type}Range(RangeType.CLOSED, lowerBound, ${WrapperType}.MAX_VALUE);
    }

    public static @NotNull ${Type}Range lessThan(${PrimitiveType} upperBound) {
        return new ${Type}Range(RangeType.CLOSED_OPEN, ${WrapperType}.MIN_VALUE, upperBound);
    }

    public static @NotNull ${Type}Range atMost(${PrimitiveType} upperBound) {
        return new ${Type}Range(RangeType.CLOSED, ${WrapperType}.MIN_VALUE,upperBound);
    }

    @Override
    public @NotNull RangeType getType() {
        return type;
    }

    public ${PrimitiveType} getLowerBound() {
        if (!hasLowerBound()) {
            throw new UnsupportedOperationException();
        }
        return lowerBound;
    }

    public ${PrimitiveType} getUpperBound() {
        if (!hasUpperBound()) {
            throw new UnsupportedOperationException();
        }
        return upperBound;
    }

    private ${PrimitiveType} strictLowerBound() {
        // assert this.isNotEmpty();
        return type.getLowerBoundType() == BoundType.OPEN
            ? <#if LiftToInt>(${PrimitiveType}) (</#if>lowerBound + 1<#if LiftToInt>)</#if>
            :lowerBound;
    }

    private ${PrimitiveType} strictUpperBound() {
        // assert this.isNotEmpty();
        return type.getUpperBoundType() == BoundType.OPEN
            ? <#if LiftToInt>(${PrimitiveType}) (</#if>upperBound - 1<#if LiftToInt>)</#if>
            :upperBound;
    }

    @UnstableName
    public ${PrimitiveType} fit(${PrimitiveType} value) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("Range is empty");
        }

        final ${PrimitiveType} strictLowerBound = strictLowerBound();
        final ${PrimitiveType} strictUpperBound = strictUpperBound();

        if (strictLowerBound >= value) {
            return strictLowerBound;
        }

        if (strictUpperBound <= value) {
            return strictUpperBound;
        }

        return value;
    }

    public boolean isEmpty() {
        return type == RangeType.EMPTY
                || (lowerBound == upperBound && type.getLowerBoundType() != type.getUpperBoundType());
    }

    public boolean contains(${PrimitiveType} value) {
        if (this == ALL) {
            return true;
        }
        if (isEmpty()) {
            return false;
        }

        return value >= strictLowerBound() && value <= strictUpperBound();
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        if (isEmpty()) {
            return ${Type}Iterator.empty();
        }
        final ${PrimitiveType} strictLowerBound = strictLowerBound();
        final ${PrimitiveType} strictUpperBound = strictUpperBound();

        if (strictLowerBound == strictUpperBound) {
            return ${Type}Iterator.of(strictLowerBound);
        }

        return new PositiveItr(strictUpperBound, DEFAULT_STEP, strictLowerBound);
    }

    @Override
    public void forEach(@NotNull ${Type}Consumer action) {
        forEachByStep(DEFAULT_STEP, action);
    }

    void forEachByStep(${StepType} step, @NotNull ${Type}Consumer action) {
        Objects.requireNonNull(action);
        if (step == 0) {
            throw new IllegalArgumentException("step mush not be zero");
        }
        if (step > MAX_STEP || step < MAX_REVERSE_STEP) {
            throw new IllegalArgumentException("step too large");
        }
        if (isEmpty()) {
            return;
        }

        final ${PrimitiveType} strictLowerBound = strictLowerBound();
        final ${PrimitiveType} strictUpperBound = strictUpperBound();

        if (step > 0) {
            ${PrimitiveType} value = strictLowerBound;
            while (value <= strictUpperBound) {
                action.accept(value);
                if (${WrapperType}.MAX_VALUE - step < value){
                    break;
                }
                value += step;
            }
        } else {
            ${PrimitiveType} value = strictUpperBound;
            while (value >= strictLowerBound) {
                action.accept(value);
                if (${WrapperType}.MIN_VALUE - step > value){
                    break;
                }
                value += step;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = result * 31 + ${WrapperType}.hashCode(lowerBound);
        result = result * 31 + ${WrapperType}.hashCode(upperBound);
        return result + HASH_MAGIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ${Type}Range)) {
            return false;
        }
        ${Type}Range ${Type}Range = (${Type}Range) o;
        return lowerBound == ${Type}Range.lowerBound && upperBound == ${Type}Range.upperBound && type == ${Type}Range.type;
    }

    private static String prettyToString(${PrimitiveType} value) {
        <#if Type == "Char">
        if (value >= 32 && value <= 126 && value != '\'' && value != '\\') {
            return "'" + value + "'";
        }

        switch (value) {
            case 0:
                return "'\\0'";
            case Character.MAX_VALUE:
                return "Character.MAX_VALUE";
            case '\t':
                return "'\\t'";
            case '\'':
                return "'\\''";
            case '\r':
                return "'\\r'";
            case '\\':
                return "'\\\\'";
            case '\n':
                return "'\\n'";
            case '\f':
                return "'\\f'";
            case '\b':
                return "'\\b'";
        }

        return String.format("'\\u%04X'", (int) value);
        <#else>
        if (value == ${WrapperType}.MAX_VALUE){
            return "${WrapperType}.MAX_VALUE";
        }
        if (value == ${WrapperType}.MIN_VALUE){
            return "${WrapperType}.MIN_VALUE";
        }
        return String.valueOf(value);
        </#if>
    }

    @Override
    public String toString() {
        if (this == EMPTY) {
            return "${Type}Range.Empty";
        }
        if (this == ALL) {
            return "${Type}Range.All";
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        StringBuilder res = new StringBuilder(32);
        res.append("${Type}Range");

        switch (lowerBoundType) {
            case OPEN:
                res.append('(');
                break;
            case CLOSED:
                res.append('[');
                break;
            case INFINITY:
                throw new AssertionError();
        }

        res.append(prettyToString(lowerBound)).append("..").append(prettyToString(upperBound));

        switch (upperBoundType) {
            case OPEN:
                res.append(')');
                break;
            case CLOSED:
                res.append(']');
                break;
            case INFINITY:
                throw new AssertionError();
        }
        return res.toString();
    }

    private static final class PositiveItr extends Abstract${Type}Iterator {
        private ${PrimitiveType} upperBound;
        private final @Range(from = 1, to = MAX_STEP) ${StepType} step;

        private ${PrimitiveType} value;

        PositiveItr(${PrimitiveType} upperBound, ${StepType} step, ${PrimitiveType} initialValue) {
            this.upperBound = upperBound;
            this.step = step;
            this.value = initialValue;
        }

        @Override
        public boolean hasNext() {
            return value <= upperBound;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final ${PrimitiveType} res =value;
            if (${WrapperType}.MAX_VALUE - step < value){
                upperBound = ${WrapperType}.MIN_VALUE;
                value = ${WrapperType}.MAX_VALUE;
            } else{
                value += step;
            }
            return res;
        }
    }

    private static final class ReverseItr extends Abstract${Type}Iterator {
        private ${PrimitiveType} lowerBound;
        private final @Range(from = MAX_REVERSE_STEP, to = -1) ${StepType} step;

        private ${PrimitiveType} value;

        private ReverseItr(${PrimitiveType} lowerBound, @Range(from = ${WrapperType}.MIN_VALUE, to = -1) ${StepType} step, ${PrimitiveType} initialValue) {
            this.lowerBound = lowerBound;
            this.step = step;
            this.value = initialValue;
        }

        @Override
        public boolean hasNext() {
            return value >= lowerBound;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final ${PrimitiveType} res =this.value;
            if (${WrapperType}.MIN_VALUE - step > value){
                lowerBound = ${WrapperType}.MAX_VALUE;
                value = ${WrapperType}.MIN_VALUE;
            } else{
                value += step;
            }
            return res;
        }
    }
}
