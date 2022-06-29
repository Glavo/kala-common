package kala.function;

import java.io.Serializable;

@FunctionalInterface
public interface ${Type}Hasher extends Hasher<${WrapperType}> {
    ${Type}Hasher DEFAULT = new Default();

    static ${Type}Hasher defaultHasher() {
        return DEFAULT;
    }

    int hash(${PrimitiveType} value);

    default boolean equals(${PrimitiveType} a, ${PrimitiveType} b) {
        return a == b;
    }

    @Override
    default int hash(${WrapperType} value) {
        return hash(value.${PrimitiveType}Value());
    }

    @Override
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
}
