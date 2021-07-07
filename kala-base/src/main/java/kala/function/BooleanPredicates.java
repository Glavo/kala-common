package kala.function;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

final class BooleanPredicates {
    static final class IsTrue implements BooleanPredicate, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public final boolean test(boolean v) {
            return v;
        }

        @Override
        public final @NotNull BooleanPredicate negate() {
            return BooleanPredicate.IS_FALSE;
        }

        @Override
        public final String toString() {
            return "BooleanPredicate.IS_TRUE";
        }

        private Object readResolve() {
            return BooleanPredicate.IS_TRUE;
        }
    }

    static final class IsFalse implements BooleanPredicate, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public final boolean test(boolean v) {
            return !v;
        }

        @Override
        public final @NotNull BooleanPredicate negate() {
            return BooleanPredicate.IS_TRUE;
        }

        @Override
        public final String toString() {
            return "BooleanPredicate.IS_FALSE";
        }

        private Object readResolve() {
            return BooleanPredicate.IS_FALSE;
        }
    }
}
