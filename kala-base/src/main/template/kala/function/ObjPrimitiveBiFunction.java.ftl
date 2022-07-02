package kala.function;

@FunctionalInterface
public interface Obj${Type}BiFunction<T, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param v the second function argument
     * @return the function result
     */
    R apply(T t, ${PrimitiveType} v);
}
