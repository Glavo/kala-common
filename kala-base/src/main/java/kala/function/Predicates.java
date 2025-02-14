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
package kala.function;

import kala.annotations.StaticClass;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@StaticClass
@SuppressWarnings("unchecked")
public final class Predicates {
    private Predicates() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Predicate<T> of(Predicate<? extends T> predicate) {
        return (Predicate<T>) predicate;
    }

    public static <T> @NotNull Predicate<T> alwaysTrue() {
        return (Predicate<T>) AlwaysTrue.INSTANCE;
    }

    public static <T> @NotNull Predicate<T> alwaysFalse() {
        return (Predicate<T>) AlwaysFalse.INSTANCE;
    }

    public static <T> @NotNull Predicate<T> isNull() {
        return (Predicate<T>) IsNull.INSTANCE;
    }

    public static <T> @NotNull Predicate<T> isNotNull() {
        return (Predicate<T>) IsNotNull.INSTANCE;
    }

    public static <T> @NotNull Predicate<T> isEqual(Object target) {
        return target == null ? isNull() : new IsEqual<>(target);
    }

    public static <T> @NotNull Predicate<T> isNotEqual(Object target) {
        return target == null ? isNotNull() : new IsNotEqual<>(target);
    }

    public static <T> @NotNull Predicate<T> isSame(Object target) {
        return target == null ? isNull() : new IsSame<>(target);
    }

    public static <T> @NotNull Predicate<T> isNotSame(Object target) {
        return target == null ? isNotNull() : new IsNotSame<>(target);
    }

    public static <T> @NotNull Predicate<T> isInstance(@NotNull Class<? extends T> type) {
        Objects.requireNonNull(type);
        return new IsInstance<>(type);
    }

    @SafeVarargs
    public static <T> @NotNull Predicate<T> and(Predicate<? super T> @NotNull ... predicates) {
        if (predicates.length == 0) {
            throw new IllegalArgumentException();
        }
        return new And<>(predicates.clone());
    }

    @SafeVarargs
    public static <T> @NotNull Predicate<T> or(Predicate<? super T> @NotNull ... predicates) {
        if (predicates.length == 0) {
            throw new IllegalArgumentException();
        }
        return new Or<>(predicates.clone());
    }

    public static <T, U> @NotNull Predicate<Tuple2<T, U>> tupled(@NotNull BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return tuple -> biPredicate.test(tuple.component1(), tuple.component2());
    }

    public static <T, U> @NotNull BiPredicate<T, U> untupled(@NotNull Predicate<? super Tuple2<? extends T, ? extends U>> predicate) {
        Objects.requireNonNull(predicate);
        return (t, u) -> predicate.test(Tuple.of(t, u));
    }

    private enum AlwaysTrue implements Predicate<Object> {
        INSTANCE;

        @Override
        public final boolean test(Object o) {
            return true;
        }

        @Override
        public final @NotNull Predicate<Object> negate() {
            return AlwaysFalse.INSTANCE;
        }

        @Override
        public final @NotNull Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return other;
        }

        @Override
        public final @NotNull Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @Override
        public final String toString() {
            return "Predicates.AlwaysTrue";
        }
    }

    private enum AlwaysFalse implements Predicate<Object> {
        INSTANCE;

        @Override
        public final boolean test(Object o) {
            return false;
        }

        @Override
        public final @NotNull Predicate<Object> negate() {
            return AlwaysTrue.INSTANCE;
        }

        @Override
        public final @NotNull Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @Override
        public final @NotNull Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return other;
        }

        @Override
        public final String toString() {
            return "Predicates.AlwaysFalse";
        }
    }

    private enum IsNull implements Predicate<Object> {
        INSTANCE;

        @Override
        public final boolean test(Object o) {
            return o == null;
        }

        @Override
        public final @NotNull Predicate<Object> negate() {
            return IsNotNull.INSTANCE;
        }

        @Override
        public final String toString() {
            return "Predicates.IsNull";
        }
    }

    private enum IsNotNull implements Predicate<Object> {
        INSTANCE;

        @Override
        public final boolean test(Object o) {
            return o != null;
        }

        @Override
        public final @NotNull Predicate<Object> negate() {
            return IsNull.INSTANCE;
        }

        @Override
        public final String toString() {
            return "Predicates.IsNotNull";
        }
    }

    private record IsEqual<T>(@NotNull Object target) implements Predicate<T>, Serializable {
        @Override
        public boolean test(T t) {
            return target.equals(t);
        }

        @Override
        public String toString() {
            return "Predicates.IsEqual[" + target + ']';
        }
    }

    private record IsNotEqual<T>(@NotNull Object target) implements Predicate<T>, Serializable {
        @Override
        public boolean test(T t) {
            return !target.equals(t);
        }

        @Override
        public String toString() {
            return "Predicates.IsNotEqual[" + target + ']';
        }
    }

    private record IsSame<T>(@NotNull Object target) implements Predicate<T>, Serializable {

        @Override
        public boolean test(T t) {
            return target == t;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof IsSame<?> other && this.target == other.target;
        }

        @Override
        public String toString() {
            return "Predicates.IsSame[" + target + ']';
        }
    }

    private record IsNotSame<T>(@NotNull Object target) implements Predicate<T>, Serializable {

        @Override
        public boolean test(T t) {
            return target != t;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof IsNotSame<?> other && this.target == other.target;
        }

        @Override
        public String toString() {
            return "Predicates.IsNotSame[" + target + ']';
        }
    }

    private record IsInstance<T>(@NotNull Class<?> type) implements Predicate<T>, Serializable {
        @Override
        public boolean test(T t) {
            return type.isInstance(t);
        }

        @Override
        public String toString() {
            return "Predicates.IsInstance[" + type + ']';
        }
    }

    private record And<T>(Predicate<? super T>[] predicates) implements Predicate<T>, Serializable {
        @Override
        public boolean test(T t) {
            for (Predicate<? super T> predicate : predicates) {
                if (!predicate.test(t)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof And<?> other && Arrays.equals(predicates, other.predicates);
        }

        @Override
        public String toString() {
            return "Predicates.And" + Arrays.toString(predicates);
        }
    }

    private record Or<T>(Predicate<? super T>[] predicates) implements Predicate<T>, Serializable {
        @Override
        public boolean test(T t) {
            for (Predicate<? super T> predicate : predicates) {
                if (predicate.test(t)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof Or<?> other && Arrays.equals(predicates, other.predicates);
        }

        @Override
        public String toString() {
            return "Predicates.Or" + Arrays.toString(predicates);
        }
    }
}
