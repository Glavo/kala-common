package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

@FunctionalInterface
public interface Checked${Type}Consumer<Ex extends Throwable> extends ${Type}Consumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> Checked${Type}Consumer<Ex> of(Checked${Type}Consumer<? extends Ex> consumer) {
        return (Checked${Type}Consumer<Ex>) consumer;
    }

    void acceptChecked(${PrimitiveType} value) throws Ex;

    @Override
    default void accept(${PrimitiveType} value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    @Deprecated
    default @NotNull Try<Void> tryAccept(${PrimitiveType} value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
