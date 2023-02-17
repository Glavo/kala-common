package kala.function;

import kala.annotations.ReplaceWith;

import java.util.function.Function;

@FunctionalInterface
public interface Converter<A, B> extends Function<A, B> {
    B convert(A a);

    @Override
    @Deprecated
    @ReplaceWith("convert(A)")
    default B apply(A a) {
        return convert(a);
    }
}
