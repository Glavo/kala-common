package kala.function;

@FunctionalInterface
public interface ${Type}ObjBiFunction<T, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param v the first function argument
     * @param t the second function argument
     * @return the function result
     */
    R apply(${PrimitiveType} v, T t);
}
