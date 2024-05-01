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

import org.jetbrains.annotations.ApiStatus;

import java.io.Serializable;

@FunctionalInterface
public interface ${Type}Hasher extends Hasher<${WrapperType}> {

    @ApiStatus.Internal
    ${Type}Hasher DEFAULT = new Default();
<#if IsFloating>

    @ApiStatus.Internal
    ${Type}Hasher RAW_BITS = new RawBits();

    @ApiStatus.Internal
    ${Type}Hasher PRIMITIVE = new Primitive();
</#if>

    static ${Type}Hasher defaultHasher() {
        return DEFAULT;
    }

<#if IsFloating>
    static ${Type}Hasher rawBitsHasher() {
        return RAW_BITS;
    }

    static ${Type}Hasher primitiveHasher() {
        return PRIMITIVE;
    }

</#if>
    int hash(${PrimitiveType} value);

    default boolean equals(${PrimitiveType} a, ${PrimitiveType} b) {
        return ${PrimitiveEquals("a", "b")};
    }

    @Override
    @Deprecated
    default int hash(${WrapperType} value) {
        return hash(value.${PrimitiveType}Value());
    }

    @Override
    @Deprecated
    default boolean equals(${WrapperType} a, ${WrapperType} b) {
        return equals(a.${PrimitiveType}Value(), b.${PrimitiveType}Value());
    }

    @ApiStatus.Internal
    final class Default implements ${Type}Hasher, Serializable {
        private static final long serialVersionUID = 0L;

        @Override
        public int hash(${PrimitiveType} value) {
            return ${WrapperType}.hashCode(value);
        }

        private Object readResolve() {
            return DEFAULT;
        }

        @Override
        public String toString() {
            return "${Type}Hasher.Default";
        }
    }
<#if IsFloating>

    @ApiStatus.Internal
    final class RawBits implements ${Type}Hasher, Serializable {
        private static final long serialVersionUID = 0L;

        RawBits() {
        }

        @Override
        public int hash(${PrimitiveType} value) {
            return ${BitsType.WrapperType}.hashCode(${ToRawBits}(value));
        }

        @Override
        public boolean equals(${PrimitiveType} a, ${PrimitiveType} b) {
            return ${ToRawBits}(a) == ${ToRawBits}(b);
        }

        private Object readResolve() {
            return RAW_BITS;
        }

        @Override
        public String toString() {
            return "${Type}Hasher.RawBits";
        }
    }

    @ApiStatus.Internal
    final class Primitive implements ${Type}Hasher, Serializable {
        private static final long serialVersionUID = 0L;

        Primitive() {
        }

        @Override
        public int hash(${PrimitiveType} value) {
            return ${BitsType.WrapperType}.hashCode(${ToRawBits}(value));
        }

        @Override
        public boolean equals(${PrimitiveType} a, ${PrimitiveType} b) {
            return a == b;
        }

        private Object readResolve() {
            return PRIMITIVE;
        }

        @Override
        public String toString() {
            return "${Type}Hasher.Primitive";
        }
    }
</#if>
}
