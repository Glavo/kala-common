package kala.function;

import kala.control.Try;

@FunctionalInterface
public interface CheckedIndexedFunction<T, R, Ex extends Throwable> extends IndexedFunction<T, R> {

    R applyChecked(int index, T t) throws Ex;

    @Override
    default R apply(int index, T t) {
        try {
            return applyChecked(index, t);
        } catch (Throwable ex) {
            throw Try.throwExceptionUnchecked(ex);
        }
    }
}
