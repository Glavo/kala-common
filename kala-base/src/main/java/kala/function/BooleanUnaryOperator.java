package kala.function;

@FunctionalInterface
public interface BooleanUnaryOperator {
    boolean applyAsBoolean(boolean operand);
}
