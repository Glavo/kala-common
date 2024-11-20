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
package kala.collection.primitive;

import kala.Conditions;
import kala.collection.SeqView;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.immutable.primitive.Immutable${Type}Seq;
import kala.collection.primitive.internal.view.${Type}SeqViews;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface ${Type}SeqView extends ${Type}SeqLike, ${Type}CollectionView, PrimitiveSeqView<${WrapperType}> {

    static @NotNull ${Type}SeqView empty() {
        return ${Type}SeqViews.EMPTY;
    }

    @Override
    default @NotNull String className() {
        return "${Type}SeqView";
    }

    @Override
    default @NotNull ${Type}SeqView view() {
        return this;
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView slice(int beginIndex, int endIndex) {
        final int ks = this.knownSize();
        if (ks == 0) {
            if (beginIndex != 0) {
                throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
            }
            if (endIndex != 0) {
                throw new IndexOutOfBoundsException("endIndex: " + endIndex);
            }
            return ${Type}SeqView.empty();
        } else if (ks > 0) {
            Conditions.checkPositionIndices(beginIndex, endIndex, ks);
        } else {
            if (beginIndex < 0) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
            }
            if (beginIndex > endIndex) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > endIndex(" + endIndex + ")");
            }
        }

        return new ${Type}SeqViews.Slice(this, beginIndex, endIndex);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return ${Type}SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return ${Type}SeqView.empty();
        }
        return new ${Type}SeqViews.Drop(this, n);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return ${Type}SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return ${Type}SeqView.empty();
        }
        return new ${Type}SeqViews.DropLast(this, n);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView dropWhile(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SeqViews.DropWhile(this, predicate);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return ${Type}SeqView.empty();
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return ${Type}SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return this;
        }
        return new ${Type}SeqViews.Take(this, n);
    }

    default @NotNull ${Type}SeqView takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return ${Type}SeqView.empty();
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return ${Type}SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return this;
        }
        return new ${Type}SeqViews.TakeLast(this, n);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView takeWhile(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SeqViews.TakeWhile(this, predicate);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView updated(int index, ${PrimitiveType} newValue) {
        final int ks = this.knownSize();
        if (ks < 0) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("index(" + index + ") < 0");
            }
        } else {
            Objects.checkIndex(index, ks);
        }
        return new ${Type}SeqViews.Updated(this, index, newValue);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView concat(@NotNull ${Type}SeqLike other) {
        Objects.requireNonNull(other);
        return new ${Type}SeqViews.Concat(this, other.view());
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prepended(${PrimitiveType} value) {
        return new ${Type}SeqViews.Prepended(this, value);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prependedAll(${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        return new ${Type}SeqViews.Concat(${Type}ArraySeq.wrap(values), this);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView prependedAll(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);
        return new ${Type}SeqViews.Concat(values instanceof ${Type}SeqLike ? (${Type}SeqLike) values : Immutable${Type}Seq.from(values), this);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appended(${PrimitiveType} value) {
        return new ${Type}SeqViews.Appended(this, value);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appendedAll(${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        return new ${Type}SeqViews.Concat(this, ${Type}ArraySeq.wrap(values));
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView appendedAll(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);
        return new ${Type}SeqViews.Concat(this, values instanceof ${Type}SeqLike ? (${Type}SeqLike) values : Immutable${Type}Seq.from(values));

    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView sorted() {
        return new ${Type}SeqViews.Sorted(this);
    }

    @Contract(pure = true)
    default @NotNull ${Type}SeqView reversed() {
        return new ${Type}SeqViews.Reversed(this);
    }

    @Override
    @Contract(pure = true)
    default  @NotNull ${Type}SeqView filter(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SeqViews.Filter(this, predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView filterNot(@NotNull ${Type}Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new ${Type}SeqViews.FilterNot(this, predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}SeqViews.Mapped(this, mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull SeqView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}SeqViews.MapToObj<>(this, mapper);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ${Type}SeqView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}SeqViews.FlatMapped(this, mapper);
    }

    @Override
    default @NotNull <T> SeqView<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper) {
        Objects.requireNonNull(mapper);
        return new ${Type}SeqViews.FlatMapToObj<>(this, mapper);
    }
}
