package org.glavo.kala.function;

@FunctionalInterface
public interface IntIntFunction<R> {
    R apply(int i1, int i2);
}
