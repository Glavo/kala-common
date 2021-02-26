package org.glavo.kala.function;

@FunctionalInterface
public interface IntIntBiFunction<R> {
    R apply(int i1, int i2);
}
