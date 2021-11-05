package kala.collection.base.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.Iterators;
import kala.control.primitive.${Type}Option;
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
<#if IsSpecialized>
import java.util.PrimitiveIterator;
<#else>
import kala.function.*;
</#if>

public interface ${Type}Iterator
        extends
        PrimIterator<${WrapperType}, ${Type}Iterator, ${PrimitiveType}[], ${Type}Option, ${Type}Consumer, ${Type}Predicate><#if IsSpecialized>, PrimitiveIterator.Of${Type}</#if> {

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

    static @NotNull ${Type}Iterator ofIterator(@NotNull Iterator<? extends @NotNull ${WrapperType}> it) {
        Objects.requireNonNull(it);
        if (it instanceof ${Type}Iterator) {
            return ((${Type}Iterator) it);
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

    default int size() {
        int i = 0;
        while (hasNext()) {
            next${Type}();
            ++i;
        }
        return i;
    }

    @Override
    @Deprecated
    @ReplaceWith("next${Type}()")
    default @NotNull ${WrapperType} next() {
        return next${Type}();
    }

    @Override
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
            if (next${Type}() == value) {
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
            if (next${Type}() == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean containsAll(${PrimitiveType} @NotNull [] values) {
        loop:
        while (hasNext()) {
            final ${PrimitiveType} v = next${Type}();
            for (${PrimitiveType} i : values) {
                if (i == v) {
                    continue loop;
                }
            }
            return false;
        }
        return true;
    }

    <#if IsSpecialized>
    @Override
    default boolean sameElements(@NotNull ${Type}Iterator other) {
        return sameElements((PrimitiveIterator.Of${Type}) other);
    }

    default boolean sameElements(@NotNull PrimitiveIterator.Of${Type} other) {
        while (this.hasNext() && other.hasNext()) {
            if (this.next${Type}() != other.next${Type}()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }
    <#else>
    @Override
    default boolean sameElements(@NotNull ${Type}Iterator other) {
        while (this.hasNext() && other.hasNext()) {
            if (this.next${Type}() != other.next${Type}()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }
    </#if>

    @Override
    default boolean sameElements(@NotNull Iterator<?> other) {
        <#if IsSpecialized>
        if (other instanceof PrimitiveIterator.Of${Type}) {
            return sameElements(((PrimitiveIterator.Of${Type}) other));
        }
        <#else>
        if (other instanceof ${Type}Iterator) {
            return sameElements(((${Type}Iterator) other));
        }
        </#if>
        while (this.hasNext() && other.hasNext()) {
            Object value = other.next();
            if (!(value instanceof ${WrapperType}) || (${WrapperType}) value != this.next${Type}()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    @Override
    default boolean anyMatch(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            if (predicate.test(next${Type}())) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean allMatch(@NotNull ${Type}Predicate predicate) {
        while (hasNext()) {
            if (!predicate.test(next${Type}())) {
                return false;
            }
        }
        return true;
    }

    @Override
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

    @Contract(mutates = "this")
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

        ${PrimitiveType} value = 0;
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

    @Override
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

    @Override
    default @NotNull ${Type}Iterator filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new ${Type}Iterators.Filter(this, predicate, false);
    }

    @Override
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

    @Override
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

    //region Aggregate Operations

    @Override
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

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.max(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.max(value, next${Type}());
</#if>
        }
        return value;
    }

    default @Nullable ${WrapperType} maxOrNull() {
        if (!hasNext()) {
            return null;
        }

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.max(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.max(value, next${Type}());
</#if>
        }
        return value;
    }

    @Override
    default @NotNull ${Type}Option maxOption() {
        if (!hasNext()) {
            return ${Type}Option.None;
        }

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.max(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.max(value, next${Type}());
</#if>
        }
        return ${Type}Option.some(value);
    }

    default ${PrimitiveType} min() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.min(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.min(value, next${Type}());
</#if>
        }
        return value;
    }

    default @Nullable ${WrapperType} minOrNull() {
        if (!hasNext()) {
            return null;
        }

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.min(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.min(value, next${Type}());
</#if>
        }
        return value;
    }

    @Override
    default @NotNull ${Type}Option minOption() {
        if (!hasNext()) {
            return ${Type}Option.None;
        }

        ${PrimitiveType} value = next${Type}();
        while (hasNext()) {
<#if IsSpecialized || Type == 'Float'>
            value = Math.min(value, next${Type}());
<#else>
            value = (${PrimitiveType}) Math.min(value, next${Type}());
</#if>
        }
        return ${Type}Option.some(value);
    }

    //endregion

    //region Conversion Operations

    @Override
    default ${PrimitiveType} @NotNull [] toArray() {
        if (!hasNext()) {
            return new ${PrimitiveType}[0];
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
}