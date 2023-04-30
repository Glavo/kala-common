package kala.function;

import kala.annotations.StaticClass;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
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

    public static <T> @NotNull Predicate<T> isSame(Object target) {
        return target == null ? isNull() : new IsSame<>(target);
    }

    public static <T> @NotNull Predicate<T> instanceOf(@NotNull Class<? extends T> type) {
        Objects.requireNonNull(type);
        return type == Object.class ? isNotNull() : new InstanceOf<>(type);
    }

    public static <T> @NotNull Predicate<T> and(@NotNull Predicate<? super T> predicate1, @NotNull Predicate<? super T> predicate2) {
        return ((Predicate<T>) predicate1).and(predicate2);
    }

    public static <T> @NotNull Predicate<T> and(Predicate<? super T> @NotNull ... predicates) {
        if (predicates.length < 2) {
            throw new IllegalArgumentException();
        }
        return new And<>(predicates.clone());
    }

    public static <T> @NotNull Predicate<T> or(@NotNull Predicate<? super T> predicate1, @NotNull Predicate<? super T> predicate2) {
        return ((Predicate<T>) predicate1).or(predicate2);
    }

    public static <T> @NotNull Predicate<T> or(Predicate<? super T> @NotNull ... predicates) {
        if (predicates.length < 2) {
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

    private static final class IsEqual<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = -9166392848522200720L;
        private static final int HASH_MAGIC = -793051311;

        private final @NotNull Object target;

        IsEqual(@NotNull Object target) {
            this.target = target;
        }

        @Override
        public boolean test(T t) {
            return target.equals(t);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IsEqual)) {
                return false;
            }
            return target.equals(((IsEqual<?>) o).target);
        }

        @Override
        public int hashCode() {
            return HASH_MAGIC + target.hashCode();
        }

        @Override
        public String toString() {
            return "Predicates.IsEqual[" + target + ']';
        }
    }

    private static final class IsSame<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 8158787942500779468L;
        private static final int HASH_MAGIC = 1242821667;

        private final @NotNull Object target;

        IsSame(@NotNull Object target) {
            this.target = target;
        }

        @Override
        public boolean test(T t) {
            return target == t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IsEqual)) {
                return false;
            }
            return target == ((IsEqual<?>) o).target;
        }

        @Override
        public int hashCode() {
            return HASH_MAGIC + target.hashCode();
        }

        @Override
        public String toString() {
            return "Predicates.IsSame[" + target + ']';
        }
    }

    private static final class InstanceOf<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = -8612667810008827121L;
        private static final int HASH_MAGIC = 990779813;

        private final @NotNull Class<?> type;

        InstanceOf(@NotNull Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean test(T t) {
            return type.isInstance(t);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof InstanceOf)) {
                return false;
            }
            return type.equals(((InstanceOf<?>) o).type);
        }

        @Override
        public int hashCode() {
            return HASH_MAGIC + type.hashCode();
        }

        @Override
        public String toString() {
            return "Predicates.InstanceOf[" + type + ']';
        }
    }

    private static final class And<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = -5136772671431728242L;

        private static final int HASH_MAGIC = 67623;

        private final Predicate<? super T>[] predicates;

        And(Predicate<? super T>[] predicates) {
            this.predicates = predicates;
        }

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
            if (this == o) {
                return true;
            }
            if (!(o instanceof And)) {
                return false;
            }
            And<?> and = (And<?>) o;
            return Arrays.equals(predicates, and.predicates);
        }

        @Override
        public int hashCode() {
            return HASH_MAGIC + Arrays.hashCode(predicates);
        }

        @Override
        public String toString() {
            return "Predicates.And" + Arrays.toString(predicates);
        }
    }

    private static final class Or<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = -3902328690682783689L;

        private static final int HASH_MAGIC = -2329323;

        private final Predicate<? super T>[] predicates;

        Or(Predicate<? super T>[] predicates) {
            this.predicates = predicates;
        }

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
            if (this == o) {
                return true;
            }
            if (!(o instanceof Or)) {
                return false;
            }
            Or<?> and = (Or<?>) o;
            return Arrays.equals(predicates, and.predicates);
        }

        @Override
        public int hashCode() {
            return HASH_MAGIC + Arrays.hashCode(predicates);
        }

        @Override
        public String toString() {
            return "Predicates.Or" + Arrays.toString(predicates);
        }
    }
}
