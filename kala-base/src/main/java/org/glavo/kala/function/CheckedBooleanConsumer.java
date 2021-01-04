package org.glavo.kala.function;

import org.glavo.kala.control.Try;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface CheckedBooleanConsumer<Ex extends Throwable> extends BooleanConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedBooleanConsumer<Ex> of(CheckedBooleanConsumer<? extends Ex> consumer) {
        return (CheckedBooleanConsumer<Ex>) consumer;
    }

    void acceptChecked(boolean value) throws Ex;

    default void accept(boolean value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            throw Try.throwExceptionUnchecked(e);
        }
    }
}
