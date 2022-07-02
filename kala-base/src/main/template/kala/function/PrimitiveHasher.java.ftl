package kala.function;

import java.io.Serializable;

@FunctionalInterface
public interface ${Type}Hasher extends Hasher<${WrapperType}> {
    ${Type}Hasher DEFAULT = new Default();
<#if IsFloating>
    ${Type}Hasher RAW_BITS = new RawBits();
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
