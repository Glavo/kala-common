package kala.function;

import java.util.Objects;

@FunctionalInterface
public interface Indexed${Type}UnaryOperator {
    ${PrimitiveType} applyAs${Type}(int index, ${PrimitiveType} operand);
}
