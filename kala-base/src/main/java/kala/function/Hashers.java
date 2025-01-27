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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

final class Hashers {
    static int improveHash(final int origin) {
        int h = origin + ~(origin << 9);
        h = h ^ (h >>> 14);
        h = h + (h << 4);
        return h ^ (h >>> 10);
    }

    static final Hasher<?> DEFAULT = new Default<>();
    static final Hasher<?> OPTIMIZED = new Optimized<>();
    static final Hasher<?> IDENTITY = new Identity<>();

    private static final class Default<T> implements Hasher<T>, Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return Objects.hashCode(obj);
        }

        @Override
        public String toString() {
            return "Hashers.Default";
        }

        @Serial
        private Object readResolve() {
            return DEFAULT;
        }
    }

    private static final class Optimized<T> implements Hasher<T>, Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            if (obj == null) {
                return 0;
            }

            int originalHash = obj.hashCode();
            return originalHash ^ (originalHash >>> 16);
        }

        @Override
        public String toString() {
            return "Hashers.Optimized";
        }

        @Serial
        private Object readResolve() {
            return OPTIMIZED;
        }
    }

    private static final class Identity<T> implements Hasher<T>, Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(T obj) {
            return System.identityHashCode(obj);
        }

        @Override
        public boolean equals(T t1, T t2) {
            return t1 == t2;
        }

        @Override
        public String toString() {
            return "Hashers.Identity";
        }

        @Serial
        private Object readResolve() {
            return IDENTITY;
        }
    }
}
