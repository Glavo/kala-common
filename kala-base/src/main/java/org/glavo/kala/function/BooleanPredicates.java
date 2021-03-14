package org.glavo.kala.function;

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
            return IS_FALSE;
        }

        private Object readResolve() {
            return IS_TRUE;
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
            return IS_TRUE;
        }

        private Object readResolve() {
            return IS_FALSE;
        }
    }
}
