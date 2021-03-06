package kala.function;

@FunctionalInterface
public interface BooleanFunction<R> {
    R apply(boolean value);
}
