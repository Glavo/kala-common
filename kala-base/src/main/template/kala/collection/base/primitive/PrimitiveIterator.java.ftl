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
package kala.collection.base.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.GenericArrays;
import kala.collection.base.Growable;
import kala.collection.base.Iterators;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import kala.internal.*;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public interface ${Type}Iterator extends PrimitiveIterator<${WrapperType}, ${Type}Consumer><#if IsSpecialized>, java.util.PrimitiveIterator.Of${Type}</#if> {

    static @NotNull ${Type}Iterator empty() {
        return ${Type}Iterators.EMPTY;
    }

    static @NotNull ${Type}Iterator of() {
        return empty();
    }

    static @NotNull ${Type}Iterator of(${PrimitiveType} value) {
        return new ${Type}Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                if (hasNext) {
                    hasNext = false;
                    return value;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public String toString() {
                if (hasNext) {
                    return "${Type}Iterator[" + value + "]";
                } else {
                    return "${Type}Iterator[]";
                }
            }
        };
    }

    static @NotNull ${Type}Iterator of(${PrimitiveType}... values) {
        return ${Type}Arrays.iterator(values);
    }
<#if Type == 'Char'>

    static @NotNull CharIterator of(@NotNull String str) {
        return str.isEmpty() ? ${Type}Iterators.EMPTY : new ${Type}Iterators.OfString(str);
    }
</#if>

    static @NotNull ${Type}Iterator ofIterator(@NotNull Iterator<? extends @NotNull ${WrapperType}> it) {
        Objects.requireNonNull(it);
        if (it instanceof ${Type}Iterator) {
            return (${Type}Iterator) it;
        }
        return new ${Type}Iterator() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                return it.next();
            }

            @Override
            public String toString() {
                return it.toString();
            }
        };
    }

<#if IsSpecialized>
    static @NotNull ${Type}Iterator ofIterator(@NotNull ${PrimitiveIteratorType} it) {
        Objects.requireNonNull(it);
        if (it instanceof ${Type}Iterator) {
            return (${Type}Iterator) it;
        }
        return new ${Type}Iterator() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                return it.next${Type}();
            }

            @Override
            public String toString() {
                return it.toString();
            }
        };
    }
</#if>

    static @NotNull ${Type}Iterator concat(@NotNull ${Type}Iterator it1, @NotNull ${Type}Iterator it2) {
        if (!it1.hasNext()) { //implicit null check of it1
            return Objects.requireNonNull(it2);
        }
        if (!it2.hasNext()) {//implicit null check of it1
            return it1;
        }
        return new ${Type}Iterators.Concat(it1, it2);
    }

    static @NotNull ${Type}Iterator concat(@NotNull ${Type}Iterator... its) {
        return switch (its.length) { // implicit null check of its
            case 0 -> ${Type}Iterator.empty();
            case 1 -> Objects.requireNonNull(its[0]);
            case 2 -> concat(its[0], its[1]);
            default -> new ${Type}Iterators.ConcatAll(GenericArrays.iterator(its));
        };
    }

    static @NotNull ${Type}Iterator concat(@NotNull Iterable<? extends ${Type}Iterator> its) {
        return concat(its.iterator()); // implicit null check of its
    }

    static @NotNull ${Type}Iterator concat(@NotNull Iterator<? extends ${Type}Iterator> its) {
        if (!its.hasNext()) { // implicit null check of its
            return ${Type}Iterator.empty();
        }
        return new ${Type}Iterators.ConcatAll(its);
    }

    /**
     * Returns the next {@code ${PrimitiveType}} element in the iteration.
     *
     * @return the next {@code ${PrimitiveType}} element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    ${PrimitiveType} next${Type}();

    @Override
    default void nextIgnoreResult() {
        next${Type}();
    }

    @Override
    @Deprecated
    @ReplaceWith("next${Type}()")
    default @NotNull ${WrapperType} next() {
        return next${Type}();
    }

    default @NotNull ${Type}Option find(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            ${PrimitiveType} value = next${Type}();
            if (predicate.test(value)) {
                return ${Type}Option.some(value);
            }
        }
        return ${Type}Option.None;
    }

    //region Element Conditions

    default boolean contains(${PrimitiveType} value) {
        while (hasNext()) {
            if (${PrimitiveEquals("value", "next${Type}()")}) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(Object value) {
        if (!(value instanceof ${WrapperType})) {
            return false;
        }

        ${PrimitiveType} v = (${WrapperType}) value;

        while (hasNext()) {
            if (${PrimitiveEquals("v", "next${Type}()")}) {
                return true;
            }
        }
        return false;
    }

<#if Type == "Boolean">
    default boolean containsAll(boolean @NotNull [] values) {
        if (!hasNext()) {
            return values.length == 0;
        }
        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                return true;
            }
        }

        if (containsTrue && containsFalse) {
            throw new AssertionError();
        } else if (containsTrue) {
            for (boolean value : values) {
                if (!value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            for (boolean value : values) {
                if (value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    @Override
    default boolean containsAll(Boolean @NotNull [] values) {
        if (values.length == 0) {
            return true;
        }
        if (!hasNext()) {
            return false;
        }

        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                break;
            }
        }

        if (containsTrue && containsFalse) {
            for (Boolean value : values) {
                if (value == null) {
                    return false;
                }
            }
        } else if (containsTrue) {
            for (Boolean value : values) {
                if (value == null || !value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            for (Boolean value : values) {
                if (value == null || value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    @Override
    default boolean containsAll(@NotNull Iterable<?> values) {
        Iterator<?> it = values.iterator();
        if (!it.hasNext()) {
            return true;
        }
        if (!hasNext()) {
            return false;
        }

        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                break;
            }
        }

        if (containsTrue && containsFalse) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean)) {
                    return false;
                }
            }
        } else if (containsTrue) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean) || !(Boolean) value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean) || (Boolean) value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }
<#else>
    default boolean containsAll(${PrimitiveType} @NotNull [] values) {
        loop:
        while (hasNext()) {
            final ${PrimitiveType} v = next${Type}();
            for (${PrimitiveType} i : values) {
                if (${PrimitiveEquals("i", "v")}) {
                    continue loop;
                }
            }
            return false;
        }
        return true;
    }
</#if>

<#if IsSpecialized>
    default boolean sameElements(@NotNull ${Type}Iterator other) {
        return sameElements((${PrimitiveIteratorType}) other);
    }
</#if>

    default boolean sameElements(@NotNull ${PrimitiveIteratorType} other) {
        while (this.hasNext() && other.hasNext()) {
            if (${PrimitiveNotEquals("this.next${Type}()", "other.next${Type}()")}) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    @Override
    default boolean sameElements(@NotNull Iterator<?> other) {
        if (other instanceof ${PrimitiveIteratorType}) {
            return sameElements((${PrimitiveIteratorType}) other);
        }
        while (this.hasNext() && other.hasNext()) {
            Object value = other.next();
            if(!(value instanceof ${WrapperType})) return false;

            if ((${PrimitiveNotEquals("(${WrapperType}) value", "this.next${Type}()")})) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    default boolean anyMatch(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            if (predicate.test(next${Type}())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            if (!predicate.test(next${Type}())) {
                return false;
            }
        }
        return true;
    }

    default boolean noneMatch(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            if (predicate.test(next${Type}())) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Misc Operations

    @Override
    default @NotNull ${Type}Iterator drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        while (n > 0 && hasNext()) {
            next${Type}();
            --n;
        }
        return this;
    }

    default @NotNull ${Type}Iterator dropWhile(@NotNull ${Type}Predicate predicate) {
        if (!hasNext()) {
            return this;
        }

        ${PrimitiveType} value = ${Values.Default};
        boolean p = false;
        while (hasNext()) {
            if (!predicate.test(value = next${Type}())) {
                p = true;
                break;
            }
        }

        if (p) {
            return hasNext() ? prepended(value) : ${Type}Iterator.of(value);
        } else {
            return this;
        }
    }

    @Override
    default @NotNull ${Type}Iterator take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (!hasNext() || n == 0) {
            return empty();
        }

        return new ${Type}Iterators.Take(this, n);
    }

    default @NotNull ${Type}Iterator takeWhile(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return this;
        }
        return new ${Type}Iterators.TakeWhile(this, predicate);
    }

    default @NotNull ${Type}Iterator updated(int n, ${PrimitiveType} newValue) {
        if (!hasNext() || n < 0) {
            return this;
        }

        if (n == 0) {
            this.next${Type}();
            return prepended(newValue);
        }

        return new ${Type}Iterators.Updated(this, n, newValue);
    }

    default @NotNull ${Type}Iterator prepended(${PrimitiveType} value) {
        return new ${Type}Iterators.Prepended(this, value);
    }

    default @NotNull ${Type}Iterator appended(${PrimitiveType} value) {
        return new ${Type}Iterators.Appended(this, value);
    }

    default @NotNull ${Type}Iterator filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new ${Type}Iterators.Filter(this, predicate, false);
    }

    default @NotNull ${Type}Iterator filterNot(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new ${Type}Iterators.Filter(this, predicate, true);
    }

    default @NotNull ${Type}Iterator map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return this;
        }
        return new Abstract${Type}Iterator() {
            @Override
            public boolean hasNext() {
                return ${Type}Iterator.this.hasNext();
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                return mapper.applyAs${Type}(${Type}Iterator.this.next${Type}());
            }
        };
    }

    default <U> @NotNull Iterator<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new AbstractIterator<U>() {
            @Override
            public boolean hasNext() {
                return ${Type}Iterator.this.hasNext();
            }

            @Override
            public U next() {
                return mapper.apply(${Type}Iterator.this.next${Type}());
            }
        };
    }

    default @NotNull Tuple2<${r'@NotNull'} ${Type}Iterator, @NotNull ${Type}Iterator> span(@NotNull ${Type}Predicate predicate) {
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        }

        Internal${Type}ArrayBuilder builder = new Internal${Type}ArrayBuilder();
        ${Type}Iterator it = this;

        while (it.hasNext()) {
            ${PrimitiveType} e = it.next${Type}();
            if (predicate.test(e)) {
                builder.append(e);
            } else {
                it = it.prepended(e);
                break;
            }
        }

        return Tuple.of(builder.iterator(), it);
    }

    //endregion

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G filterTo(@NotNull G destination, @NotNull ${Type}Predicate predicate) {
        ${Type}Iterator it = this;
        while (it.hasNext()) {
            ${PrimitiveType} e = it.next${Type}();
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G filterNotTo(@NotNull G destination, @NotNull ${Type}Predicate predicate) {
        ${Type}Iterator it = this;
        while (it.hasNext()) {
            ${PrimitiveType} e = it.next${Type}();
            if (!predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends ${Type}Growable> @NotNull G mapTo(@NotNull G destination, @NotNull ${Type}UnaryOperator mapper) {
        ${Type}Iterator it = this;
        while (it.hasNext()) {
            destination.plusAssign(mapper.applyAs${Type}(it.next${Type}()));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapToObjTo(@NotNull G destination, @NotNull ${Type}Function<? extends U> mapper) {
        ${Type}Iterator it = this;
        while (it.hasNext()) {
            destination.plusAssign(mapper.apply(it.next${Type}()));
        }
        return destination;
    }

    //region Aggregate Operations

    default int count(@NotNull ${Type}Predicate predicate) {
        int c = 0;
        while (hasNext()) {
            if (predicate.test(next${Type}())) {
                ++c;
            }
        }
        return c;
    }

    default ${PrimitiveType} max() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
<#if Type == 'Boolean'>

        while (hasNext()) {
            if (nextBoolean()) return true;
        }

        return false;
<#else>
        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.max(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.max(value, next${Type}());
</#if>
        }
        return value;
</#if>
    }

    @Override
    default @Nullable ${WrapperType} maxOrNull() {
        return hasNext() ? max() : null;
    }

    @Override
    default @NotNull ${Type}Option maxOption() {
        return hasNext() ? ${Type}Option.some(max()) : ${Type}Option.none();
    }

    default ${PrimitiveType} min() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
<#if Type == 'Boolean'>

        while (hasNext()) {
            if (!nextBoolean()) return false;
        }

        return true;
<#else>
        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.min(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.min(value, next${Type}());
</#if>
        }
        return value;
</#if>
    }

    @Override
    default @Nullable ${WrapperType} minOrNull() {
        return hasNext() ? min() : null;
    }

    @Override
    default @NotNull ${Type}Option minOption() {
        return hasNext() ? ${Type}Option.some(min()) : ${Type}Option.none();
    }

    default ${PrimitiveType} fold(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return foldLeft(zero, op);
    }

    default ${PrimitiveType} foldLeft(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        while (hasNext()) {
            zero = op.applyAs${Type}(zero, this.next${Type}());
        }
        return zero;
    }

    default <U> U foldLeftToObj(U zero, @NotNull Obj${Type}BiFunction<U, U> op) {
        while (hasNext()) {
            zero = op.apply(zero, this.next${Type}());
        }
        return zero;
    }

    default ${PrimitiveType} foldRight(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        if (!hasNext()) {
            return zero;
        }
        Internal${Type}ArrayBuilder builder = new Internal${Type}ArrayBuilder();
        while (hasNext()) {
            builder.append(next${Type}());
        }
        for (int i = builder.size() - 1; i >= 0; i--) {
            zero = op.applyAs${Type}(builder.get(i), zero);
        }
        return zero;
    }

    default <U> U foldRightToObj(U zero, @NotNull ${Type}ObjBiFunction<U, U> op) {
        if (!hasNext()) {
            return zero;
        }
        Internal${Type}ArrayBuilder builder = new Internal${Type}ArrayBuilder();
        while (hasNext()) {
            builder.append(next${Type}());
        }
        for (int i = builder.size() - 1; i >= 0; i--) {
            zero = op.apply(builder.get(i), zero);
        }
        return zero;
    }

    default ${PrimitiveType} reduce(@NotNull ${Type}BinaryOperator op) {
        return reduceLeft(op);
    }

    default @Nullable ${WrapperType} reduceOrNull(@NotNull ${Type}BinaryOperator op) {
        return reduceLeftOrNull(op);
    }

    default @NotNull ${Type}Option reduceOption(@NotNull ${Type}BinaryOperator op) {
        return reduceLeftOption(op);
    }

    default ${PrimitiveType} reduceLeft(@NotNull ${Type}BinaryOperator op) {
        ${PrimitiveType} e = next${Type}();
        while (hasNext()) {
            e = op.applyAs${Type}(e, next${Type}());
        }
        return e;
    }

    default @Nullable ${WrapperType} reduceLeftOrNull(@NotNull ${Type}BinaryOperator op) {
        return hasNext() ? reduceLeft(op) : null;
    }

    default @NotNull ${Type}Option reduceLeftOption(@NotNull ${Type}BinaryOperator op) {
        return hasNext() ? ${Type}Option.some(reduceLeft(op)) : ${Type}Option.none();
    }

    default ${PrimitiveType} reduceRight(@NotNull ${Type}BinaryOperator op) {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Internal${Type}ArrayBuilder list = new Internal${Type}ArrayBuilder();
        list.append(next${Type}());
        while (hasNext()) {
            list.append(next${Type}());
        }
        final int size = list.size();
        ${PrimitiveType} e = list.get(size - 1);

        for (int i = size - 2; i >= 0; i--) {
            e = op.applyAs${Type}(list.get(i), e);
        }
        return e;
    }

    default @Nullable ${WrapperType} reduceRightOrNull(@NotNull ${Type}BinaryOperator op) {
        return hasNext() ? reduceRight(op) : null;
    }

    default @NotNull ${Type}Option reduceRightOption(@NotNull ${Type}BinaryOperator op) {
        return hasNext() ? ${Type}Option.some(reduceRight(op)) : ${Type}Option.none();
    }

    //endregion

    //region Conversion Operations

    @Override
    default ${PrimitiveType} @NotNull [] toArray() {
        if (!hasNext()) {
            return ${Type}Arrays.EMPTY;
        }
        Internal${Type}ArrayBuilder builder = new Internal${Type}ArrayBuilder();
        while (hasNext()) {
            builder.append(next${Type}());
        }

        return builder.toArray();
    }

    //endregion

    //region Traverse Operations

    @Override
    default void forEach(@NotNull ${Type}Consumer action) {
        while (hasNext()) {
            action.accept(next${Type}());
        }
    }

    @Override
    default void forEachRemaining(@NotNull Consumer<? super ${WrapperType}> action) {
        if (action instanceof ${Type}Consumer) {
            forEach(((${Type}Consumer) action));
        } else {
            forEach(action::accept);
        }
    }

    @Override
    default void forEachRemaining(@NotNull ${Type}Consumer action) {
        forEach(action);
    }

    //endregion

    //region

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        try {
            buffer.append(prefix);
            if (hasNext()) {
                buffer.append(String.valueOf(next${Type}()));
            }
            while (hasNext()) {
                buffer.append(separator).append(String.valueOf(next${Type}()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    //endregion

    @Override
    default int hash() {
        int res = 0;
        while (hasNext()) {
            res = res * 31 + ${WrapperType}.hashCode(next${Type}());
        }
        return res;
    }
}